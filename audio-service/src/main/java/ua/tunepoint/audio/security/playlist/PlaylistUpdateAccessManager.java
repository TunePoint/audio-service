package ua.tunepoint.audio.security.playlist;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class PlaylistUpdateAccessManager implements AccessManager<UserPrincipal, PlaylistAccessibleEntity> {

    @Override
    public void authorize(UserPrincipal user, PlaylistAccessibleEntity playlist) {
        if (playlist.getManagerType() != ManagerType.USER) {
            throw new ForbiddenException("You can't update service playlist");
        }

        if (!playlist.getOwnerId().equals(extractUserId(user))) {
            throw new ForbiddenException("You can't update playlist you don't own");
        }
    }

    private Long extractUserId(UserPrincipal user) {
        return user == null ? null : user.getId();
    }
}
