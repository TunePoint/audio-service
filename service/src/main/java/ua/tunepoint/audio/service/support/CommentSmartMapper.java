package ua.tunepoint.audio.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.Comment;
import ua.tunepoint.audio.data.mapper.CommentMapper;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.model.response.payload.CommentPayload;
import ua.tunepoint.audio.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommentSmartMapper {

    private final CommentMapper commentMapper;
    private final UserService userService;

    public CommentPayload toPayload(Comment comment) {
        return toPayloadWithCache(comment, new HashMap<>());
    }

    private CommentPayload toPayloadWithCache(Comment comment, Map<Long, User> userCache) {

        var mappedComment = commentMapper.toPayload(comment);

        var userId = mappedComment.getUserId();
        if (userCache.containsKey(userId)) {
            mappedComment.setUser(userCache.get(userId));
        } else {
            var user = userService.getUser(mappedComment.getUserId()).orElse(null);
            userCache.put(userId, user);
            mappedComment.setUser(user);
        }

        var replies = new ArrayList<CommentPayload>();
        mappedComment.setReplies(replies);

        for (var reply: emptyWhenNull(comment.getReplies())) {
            replies.add(toPayloadWithCache(reply, userCache));
        }

        return mappedComment;
    }

    private <T> Set<T> emptyWhenNull(Set<T> list) {
        return list == null ? Collections.emptySet() : list;
    }
}
