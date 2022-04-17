package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.audio.Audio;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AudioRepository extends PagingAndSortingRepository<Audio, Long> {

    Page<Audio> findAudioByAuthorIdAndIsPrivateFalse(Long authorId, Pageable pageable);

    Page<Audio> findAudioByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT a FROM Audio a JOIN PlaylistAudio pa ON pa.id.audioId = a.id WHERE pa.id.playlistId = :playlistId AND (a.isPrivate = FALSE OR a.authorId = :userId)")
    Page<Audio> findAudioByPlaylistWithAccess(Long playlistId, Long userId, Pageable pageable);

    <T> Set<T> findAllByIdIn(Set<Long> ids, Class<T> projection);

    <T> Optional<T> findById(Long id, Class<T> projection);
}
