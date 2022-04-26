package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.audio.AudioListening;
import ua.tunepoint.audio.data.entity.audio.AudioListeningId;
import ua.tunepoint.audio.data.repository.AudioListeningRepository;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.model.event.audio.AudioListenEvent;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.web.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.AUDIO;

@Service
@RequiredArgsConstructor
public class ListeningService {

    private final AudioRepository audioRepository;
    private final AudioListeningRepository audioListeningRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public void recordListening(Long audioId, Long userId) {
        var audio = requireAudioExists(audioId);

        var listeningId = new AudioListeningId(userId, audioId);

        var listeningOptional = audioListeningRepository.findById(listeningId);

        LocalDateTime now = LocalDateTime.now();

        if (listeningOptional.isPresent()) {
            var listening = listeningOptional.get();

            if (listening.getLastListeningTime().isBefore(now.minusMinutes(1))) {
                listening.recordListening(now);
                audioListeningRepository.save(listening);

                publishListenEvent(audio, userId);
            }
        } else {
            var listening = AudioListening.create(
                    listeningId,
                    now
            );
            audioListeningRepository.save(listening);
            publishListenEvent(audio, userId);
        }
    }

    private void publishListenEvent(AccessibleEntity audio, Long userId) {
        eventPublisher.publish(
                AUDIO.getName(),
                singletonList(
                        AudioListenEvent.builder()
                                .userId(userId)
                                .audioId(audio.getId())
                                .audioOwnerId(audio.getOwnerId())
                                .build()
                )
        );
    }

    private AccessibleEntity requireAudioExists(Long id) {
        return audioRepository.findById(id, AccessibleEntity.class)
                .orElseThrow(
                        () -> new NotFoundException("audio with id " + id + " was not found")
                );
    }
}
