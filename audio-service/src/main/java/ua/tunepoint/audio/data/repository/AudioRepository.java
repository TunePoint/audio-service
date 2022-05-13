package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.audio.Audio;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AudioRepository extends PagingAndSortingRepository<Audio, Long> {

    Page<Audio> findAudioByOwnerIdAndIsPrivateFalse(Long ownerId, Pageable pageable);

    Page<Audio> findAudioByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT a FROM Audio a WHERE a.id IN :ids")
    List<Audio> findBulk(List<Long> ids);

    @Query(
            "SELECT a FROM Audio a JOIN PlaylistAudio pa ON pa.id.audioId = a.id AND pa.id.playlistId = :playlistId " +
            "WHERE (a.isPrivate = FALSE OR a.ownerId = :userId) " +
            "ORDER BY pa.addedAt DESC"
    )
    Page<Audio> findAudioFromPlaylistProtected(Long playlistId, Long userId, Pageable pageable);

    @Query("SELECT a FROM Audio a JOIN AudioLike al " +
            "ON al.likeIdentity.audioId = a.id AND al.likeIdentity.userId = :userId AND (a.isPrivate = FALSE OR a.ownerId = :clientId) " +
            "ORDER BY al.createdAt DESC")
    Page<Audio> findAudioLikedByUserProtected(Long userId, Long clientId, Pageable pageable);

    @Query("SELECT a FROM Audio a JOIN PlaylistAudio pa ON pa.id.audioId = a.id WHERE pa.id.playlistId = :playlistId")
    Page<Audio> findAudioFromPlaylist(Long playlistId, Pageable pageable);

    <T> Set<T> findAllByIdIn(Set<Long> ids, Class<T> projection);

    <T> Optional<T> findById(Long id, Class<T> projection);

    @Query(
        """
            SELECT a FROM Audio a JOIN AudioListening l ON l.listeningIdentity.userId = :userId AND a.id = l.listeningIdentity.audioId
            WHERE a.isPrivate = false OR a.ownerId = :userId 
            ORDER BY l.lastListeningTime DESC
        """
    )
    Page<Audio> findRecentListenings(Long userId, Pageable pageable);
}
