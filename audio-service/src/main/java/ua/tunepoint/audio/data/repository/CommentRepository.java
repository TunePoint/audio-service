package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.comment.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    Page<Comment> findCommentsByAudioAndReplyToIsNull(Audio audio, Pageable pageable);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.audio WHERE c.id = :id")
    Optional<Comment> findByIdWithAudio(Long id);
}
