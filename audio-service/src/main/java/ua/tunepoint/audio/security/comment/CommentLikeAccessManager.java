package ua.tunepoint.audio.security.comment;

import org.aspectj.bridge.ICommand;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class CommentLikeAccessManager implements AccessManager<Long, Comment> {

    @Override
    public void authorize(Long userIdentity, Comment comment) {
        if (comment.getIsDeleted()) {
            throw new ForbiddenException("comment is already deleted!");
        }
    }
}
