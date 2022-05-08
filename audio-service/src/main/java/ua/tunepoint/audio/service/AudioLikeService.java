package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.audio.AudioLikeIdentity;
import ua.tunepoint.audio.data.repository.AudioLikeRepository;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AudioLikeService {

    private final AudioLikeRepository audioLikeRepository;

    public Set<Long> likedFromBulk(Collection<Long> audios, Long clientId) {
         return audioLikeRepository.likedBulk(audios, clientId);
    }

    public boolean isLiked(Long audioId, Long clientId) {
        return audioLikeRepository.existsByLikeIdentity(new AudioLikeIdentity(audioId, clientId));
    }
}
