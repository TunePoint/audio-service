package ua.tunepoint.audio.security.comment;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class CommentDeleteAccessManager implements AccessManager<Long, Comment> {

    @Override
    public void authorize(Long user, Comment comment) {
        if (!comment.getUserId().equals(user)) {
            throw new ForbiddenException();
        }

        if (comment.getIsDeleted() != null && comment.getIsDeleted()) {
            throw new ForbiddenException("Comment is already deleted");
        }
    }
}
