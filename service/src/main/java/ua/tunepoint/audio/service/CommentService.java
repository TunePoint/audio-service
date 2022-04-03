package ua.tunepoint.audio.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.Audio;
import ua.tunepoint.audio.data.entity.Comment;
import ua.tunepoint.audio.data.mapper.CommentMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.CommentLikeRepository;
import ua.tunepoint.audio.data.repository.CommentRepository;
import ua.tunepoint.audio.model.request.AudioCommentRequest;
import ua.tunepoint.audio.model.request.CommentUpdateRequest;
import ua.tunepoint.audio.model.response.payload.CommentPayload;
import ua.tunepoint.audio.security.audio.AudioVisibilityAccessManager;
import ua.tunepoint.audio.security.comment.CommentDeleteAccessManager;
import ua.tunepoint.audio.security.comment.CommentUpdateAccessManager;
import ua.tunepoint.audio.service.support.CommentSmartMapper;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import javax.annotation.Nullable;
import javax.transaction.Transactional;

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

    @Transactional
    public CommentPayload save(Long audioId, AudioCommentRequest request, UserPrincipal user) {

        var audio = findAudioElseThrow(audioId);

        audioVisibilityAccessManager.authorize(user, audio);

        var comment = requestMapper.toEntity(request, user.getId());

        comment.setAudio(audio);

        var savedComment = commentRepository.save(comment);

        return commentSmartMapper.toPayload(savedComment);
    }

    @Transactional
    public Page<CommentPayload> find(Long audioId, Pageable pageable, @Nullable UserPrincipal user) {

        var audio = findAudioElseThrow(audioId);

        audioVisibilityAccessManager.authorize(user, audio);

        var comments = commentRepository.findCommentsByAudioAndReplyToIsNull(audio, pageable);
        return comments.map(commentSmartMapper::toPayload);
    }

    public CommentPayload find(Long id, @Nullable UserPrincipal user) {
        var comment = findCommentElseThrow(id);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        return commentSmartMapper.toPayload(comment);
    }

    @Transactional
    public CommentPayload update(Long commentId, CommentUpdateRequest request, UserPrincipal user) {

        var comment = findCommentElseThrow(commentId);

        commentUpdateAccessManager.authorize(user, comment);

        comment = commentMapper.update(comment, request);
        var updatedComment = commentRepository.save(comment);

        return commentSmartMapper.toPayload(updatedComment);
    }

    @Transactional
    public CommentPayload delete(Long commentId, UserPrincipal user) {
        var comment = findCommentElseThrow(commentId);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());
        commentDeleteAccessManager.authorize(user, comment);

        comment.setIsDeleted(true);
        comment.setContent(null);

        var deletedComment = commentRepository.save(comment);

        return commentSmartMapper.toPayload(deletedComment);
    }

    @Transactional
    public void like(Long commentId, @NotNull UserPrincipal user) {

        var comment = findCommentElseThrow(commentId);
        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var like = commentMapper.toLike(commentId, user.getId());
        if (likeRepository.existsByLikeIdentity(like.getLikeIdentity())) {
            throw new BadRequestException("You already liked this comment");
        }
        likeRepository.save(like);
    }

    @Transactional
    public void unlike(Long commentId, @NotNull UserPrincipal user) {

        var comment = findCommentElseThrow(commentId);
        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var like = commentMapper.toLike(commentId, user.getId());
        if (!likeRepository.existsByLikeIdentity(like.getLikeIdentity())) {
            throw new BadRequestException("Like is not set");
        }
        likeRepository.delete(like);
    }

    @Transactional
    public CommentPayload reply(Long commentId, AudioCommentRequest request, UserPrincipal user) {
        var comment = findCommentElseThrow(commentId);

        audioVisibilityAccessManager.authorize(user, comment.getAudio());

        var reply = requestMapper.toEntity(request, user.getId());

        reply.setAudio(comment.getAudio());
        reply.setReplyTo(comment);

        commentRepository.save(reply);

        return commentSmartMapper.toPayload(reply);
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
