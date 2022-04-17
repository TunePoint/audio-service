package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.comment.CommentLike;
import ua.tunepoint.audio.data.entity.comment.CommentLikeIdentity;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeIdentity> {

    boolean existsByIdentity(CommentLikeIdentity identity);
}
