package ua.tunepoint.audio.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.tunepoint.audio.data.entity.Audio;
import ua.tunepoint.audio.data.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    Page<Comment> findCommentsByAudioAndReplyToIsNull(Audio audio, Pageable pageable);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.audio WHERE c.id = :id")
    Optional<Comment> findByIdWithAudio(Long id);
}
