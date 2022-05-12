package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.audio.AudioLike;
import ua.tunepoint.audio.data.entity.audio.AudioLikeIdentity;

import java.util.Collection;
import java.util.Set;

@Repository
public interface AudioLikeRepository extends JpaRepository<AudioLike, AudioLikeIdentity> {

    boolean existsByLikeIdentity(AudioLikeIdentity likeIdentity);

    @Query("SELECT al.likeIdentity.audioId FROM AudioLike al WHERE al.likeIdentity.userId = :userId AND al.likeIdentity.audioId IN :audios")
    Set<Long> likedBulk(Collection<Long> audios, Long userId);

    @Query("SELECT al FROM AudioLike al WHERE al.likeIdentity.audioId = :audioId ORDER BY al.createdAt DESC")
    Page<AudioLike> findAudioLikesByAudioId(Long audioId, Pageable pageable);
}
