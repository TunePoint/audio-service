package ua.tunepoint.audio.security.playlist;

import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

public class PlaylistUpdateAccessManager implements AccessManager<UserPrincipal, Playlist> {

    @Override
    public void authorize(UserPrincipal user, Playlist playlist) {
        if (!playlist.getAuthorId().equals(extractUserId(user))) {
            throw new ForbiddenException();
        }
    }

    private Long extractUserId(UserPrincipal user) {
        return user == null ? null : user.getId();
    }
}
