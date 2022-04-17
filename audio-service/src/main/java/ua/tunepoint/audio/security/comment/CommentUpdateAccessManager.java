package ua.tunepoint.audio.security.comment;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class CommentUpdateAccessManager implements AccessManager<UserPrincipal, Comment> {

    @Override
    public void authorize(UserPrincipal user, Comment comment) {
        if (user == null || !comment.getUserId().equals(user.getId())) {
            throw new ForbiddenException();
        }

        if (comment.getIsDeleted()) {
            throw new ForbiddenException("The comment is deleted");
        }
    }
}
