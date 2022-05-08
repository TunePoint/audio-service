package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.tunepoint.audio.data.entity.comment.CommentLikeIdentity;
import ua.tunepoint.audio.data.repository.CommentLikeRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;

    public Set<Long> likedFromBulk(Set<Long> comments, Long clientId) {
        return likeRepository.likedBulk(comments, clientId);
    }

    public boolean isLiked(Long commentId, Long clientId) {
        return likeRepository.existsByIdentity(new CommentLikeIdentity(commentId, clientId));
    }
}
