package ua.tunepoint.audio.security.playlist;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class PlaylistInteractionAccessManager implements AccessManager<UserPrincipal, PlaylistAccessibleEntity> {

    @Override
    public void authorize(UserPrincipal user, PlaylistAccessibleEntity playlist) {
        if (playlist.isService()) {
            throw new ForbiddenException("You can't interact like that with service playlist");
        }
    }
}
