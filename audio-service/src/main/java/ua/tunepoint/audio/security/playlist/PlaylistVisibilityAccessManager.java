package ua.tunepoint.audio.security.playlist;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class PlaylistVisibilityAccessManager implements AccessManager<Long, Playlist> {

    @Override
    public void authorize(Long user, Playlist playlist) {
        if (playlist.getIsPrivate() && Objects.equals(user, playlist.getOwnerId())) {
            throw new ForbiddenException();
        }
    }
}
