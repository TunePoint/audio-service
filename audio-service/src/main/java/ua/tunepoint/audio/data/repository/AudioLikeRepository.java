package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tunepoint.audio.data.entity.AudioLike;
import ua.tunepoint.audio.data.entity.AudioLikeIdentity;

public interface AudioLikeRepository extends JpaRepository<AudioLike, AudioLikeIdentity> {

    boolean existsByLikeIdentity(AudioLikeIdentity likeIdentity);
}
