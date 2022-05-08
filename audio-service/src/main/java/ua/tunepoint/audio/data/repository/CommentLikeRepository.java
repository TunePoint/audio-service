package ua.tunepoint.audio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.comment.CommentLike;
import ua.tunepoint.audio.data.entity.comment.CommentLikeIdentity;

import java.util.Collection;
import java.util.Set;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeIdentity> {

    boolean existsByIdentity(CommentLikeIdentity identity);

    @Query("SELECT c.identity.commentId FROM CommentLike c WHERE c.identity.userId = :clientId AND c.identity.commentId IN :comments")
    Set<Long> likedBulk(Collection<Long> comments, Long clientId);
}
