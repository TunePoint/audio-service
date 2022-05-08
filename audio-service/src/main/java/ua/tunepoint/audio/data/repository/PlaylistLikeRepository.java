package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLike;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;

import java.util.Collection;
import java.util.Set;

public interface PlaylistLikeRepository extends PagingAndSortingRepository<PlaylistLike, PlaylistLikeIdentity> {

    @Query("SELECT pl.identity.playlistId FROM PlaylistLike pl WHERE pl.identity.userId = :userId AND pl.identity.playlistId IN :playlists")
    Set<Long> likedBulk(Collection<Long> playlists, Long userId);
}
