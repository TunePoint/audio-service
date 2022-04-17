package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.audio.AudioLike;
import ua.tunepoint.audio.data.entity.audio.AudioLikeIdentity;

@Repository
public interface AudioLikeRepository extends JpaRepository<AudioLike, AudioLikeIdentity> {

    boolean existsByLikeIdentity(AudioLikeIdentity likeIdentity);
}
