package ua.tunepoint.audio.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.mapper.CommentMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.CommentLikeRepository;
import ua.tunepoint.audio.data.repository.CommentRepository;
import ua.tunepoint.audio.model.request.AudioCommentPostRequest;
import ua.tunepoint.audio.model.request.AudioCommentUpdateRequest;
import ua.tunepoint.audio.model.response.payload.CommentPayload;
import ua.tunepoint.audio.security.audio.AudioVisibilityAccessManager;
import ua.tunepoint.audio.security.comment.CommentDeleteAccessManager;
import ua.tunepoint.audio.security.comment.CommentUpdateAccessManager;
import ua.tunepoint.audio.service.support.CommentSmartMapper;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import javax.annotation.Nullable;
import java.util.Collections;

import static ua.tunepoint.audio.model.event.Domain.AUDIO_COMMENT;
import static ua.tunepoint.audio.utils.EventUtils.toCreatedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toDeleteEvent;
import static ua.tunepoint.audio.utils.EventUtils.toLikeEvent;
import static ua.tunepoint.audio.utils.EventUtils.toReplyEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUnlikeEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUpdateEvent;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository likeRepository;
    private final AudioRepository audioRepository;

    private final RequestMapper requestMapper;
    private final CommentMapper commentMapper;
    private final CommentSmartMapper commentSmartMapper;

    private final AudioVisibilityAccessManager audioVisibilityAccessManager;
    private final CommentDeleteAccessManager commentDeleteAccessManager;
    private final CommentUpdateAccessManager commentUpdateAccessManager;

    private final EventPublisher publisher;

    @Transactional
    public CommentPayload save(Long audioId, AudioCommentPostRequest request, Long user) {

        var audio = findAudioElseThrow(audioId);

        audioVisibilityAccessManager.authorize(user, audio);

        var comment = requestMapper.toEntity(request, user);
        comment.setAudio(audio);

        var savedComment = commentRepository.save(comment);
        var payload = commentSmartMapper.toPayload(savedComment);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toCreatedEvent(comment, audio, user))
        );

        return payload;
    }

    public Page<CommentPayload> find(Long audioId, @Nullable Long user, Pageable pageable) {

        var audio = findAudioElseThrow(audioId);

        audioVisibilityAccessManager.authorize(user, audio);

        var comments = commentRepository.findCommentsByAudioAndReplyToIsNull(audio, pageable);
        return comments.map(commentSmartMapper::toPayload);
    }

    public CommentPayload find(Long id, @Nullable Long user) {
        var comment = findCommentElseThrow(id);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        return commentSmartMapper.toPayload(comment);
    }

    @Transactional
    public CommentPayload update(Long commentId, AudioCommentUpdateRequest request, Long user) {

        var comment = findCommentElseThrow(commentId);

        commentUpdateAccessManager.authorize(user, comment);

        comment = commentMapper.update(comment, request);
        var updatedComment = commentRepository.save(comment);

        var payload = commentSmartMapper.toPayload(updatedComment);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toUpdateEvent(updatedComment, comment.getAudio(), user))
        );

        return payload;
    }

    @Transactional
    public CommentPayload delete(Long commentId, Long user) {
        var comment = findCommentElseThrow(commentId);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());
        commentDeleteAccessManager.authorize(user, comment);

        comment.setIsDeleted(true);
        comment.setContent(null);

        var deletedComment = commentRepository.save(comment);

        var payload = commentSmartMapper.toPayload(deletedComment);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toDeleteEvent(deletedComment, comment.getAudio(), user))
        );

        return payload;
    }

    @Transactional
    public void like(Long commentId, @NotNull Long user) {

        var comment = findCommentElseThrow(commentId);
        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var like = commentMapper.toLike(commentId, user);
        if (likeRepository.existsByIdentity(like.getIdentity())) {
            throw new BadRequestException("You already liked this comment");
        }
        likeRepository.save(like);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toLikeEvent(comment, comment.getAudio(), user))
        );
    }

    @Transactional
    public void unlike(Long commentId, @NotNull Long user) {

        var comment = findCommentElseThrow(commentId);
        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var like = commentMapper.toLike(commentId, user);
        if (!likeRepository.existsByIdentity(like.getIdentity())) {
            throw new BadRequestException("Like is not set");
        }
        likeRepository.delete(like);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toUnlikeEvent(comment, comment.getAudio(), user))
        );
    }

    @Transactional
    public CommentPayload reply(Long commentId, AudioCommentPostRequest request, Long user) {
        var comment = findCommentElseThrow(commentId);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var replyComment = requestMapper.toEntity(request, user);

        replyComment.reply(comment);

        commentRepository.save(replyComment);

        var payload = commentSmartMapper.toPayload(replyComment);

        publisher.publish(AUDIO_COMMENT.getName(),
                Collections.singletonList(toReplyEvent(comment, replyComment, comment.getAudio(), user))
        );

        return payload;
    }

    private Comment findCommentElseThrow(Long commentId) {
        return commentRepository.findByIdWithAudio(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " was not found"));
    }

    private Audio findAudioElseThrow(Long audioId) {
        return audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));
    }
}
