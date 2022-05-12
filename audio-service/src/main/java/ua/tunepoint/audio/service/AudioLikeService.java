package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.audio.AudioLike;
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

    public Page<AudioLike> getRecentLikes(Long audioId, Pageable pageable) {
        return audioLikeRepository.findAudioLikesByAudioId(audioId, pageable);
    }
}
