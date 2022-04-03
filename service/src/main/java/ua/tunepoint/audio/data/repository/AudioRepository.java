package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.Audio;

import java.util.List;

public interface AudioRepository extends PagingAndSortingRepository<Audio, Long> {

//    @Query("SELECT Audio FROM Audio JOIN AudioStatistics " +
//            "WHERE Audio.authorId = :authorId AND (NOT Audio.isPrivate OR Audio.authorId = :userId) ")
//    List<Audio> findAudioWithAccessControl(Long authorId, Long userId);

    Page<Audio> findAudioByAuthorIdAndIsPrivateFalse(Long authorId, Pageable pageable);

    Page<Audio> findAudioByAuthorId(Long authorId, Pageable pageable);
}
