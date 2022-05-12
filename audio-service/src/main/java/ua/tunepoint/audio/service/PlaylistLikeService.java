package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLike;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;
import ua.tunepoint.audio.data.repository.PlaylistLikeRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaylistLikeService {

    private final PlaylistLikeRepository likeRepository;

    public Set<Long> likedFromBulk(Set<Long> playlists, Long clientId) {
        return likeRepository.likedBulk(playlists, clientId);
    }

    public boolean isLiked(Long playlistId, Long clientId) {
        return likeRepository.existsById(new PlaylistLikeIdentity(playlistId, clientId));
    }

    public Page<PlaylistLike> getRecentLikes(Long playlistId, Pageable pageable) {
        return likeRepository.findPlaylistLikesByPlaylistId(playlistId, pageable);
    }
}
