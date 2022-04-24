package ua.tunepoint.audio.security.playlist;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

@Component
public class PlaylistInteractionAccessManager implements AccessManager<Long, PlaylistAccessibleEntity> {

    @Override
    public void authorize(Long user, PlaylistAccessibleEntity playlist) {
        if (playlist.getManagerType() != ManagerType.USER) {
            throw new ForbiddenException("You can't interact like that with service playlist");
        }
    }
}
