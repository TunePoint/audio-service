package ua.tunepoint.audio.security.playlist;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class PlaylistUpdateAccessManager implements AccessManager<Long, PlaylistAccessibleEntity> {

    @Override
    public void authorize(Long user, PlaylistAccessibleEntity playlist) {
        if (playlist.getManagerType() != ManagerType.USER) {
            throw new ForbiddenException("You can't update service playlist");
        }

        if (!Objects.equals(user, playlist.getOwnerId())) {
            throw new ForbiddenException("You can't update playlist you don't own");
        }
    }
}
