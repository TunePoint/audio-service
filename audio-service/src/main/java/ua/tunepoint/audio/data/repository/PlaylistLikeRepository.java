package ua.tunepoint.audio.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLike;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;

public interface PlaylistLikeRepository extends PagingAndSortingRepository<PlaylistLike, PlaylistLikeIdentity> {
}
