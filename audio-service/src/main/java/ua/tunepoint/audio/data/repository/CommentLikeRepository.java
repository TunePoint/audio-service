package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tunepoint.audio.data.entity.CommentLike;
import ua.tunepoint.audio.data.entity.CommentLikeIdentity;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeIdentity> {

    boolean existsByLikeIdentity(CommentLikeIdentity likeIdentity);
}
