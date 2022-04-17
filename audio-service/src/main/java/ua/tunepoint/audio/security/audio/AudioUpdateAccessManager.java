package ua.tunepoint.audio.security.audio;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class AudioUpdateAccessManager implements AccessManager<UserPrincipal, Audio> {

    @Override
    public void authorize(UserPrincipal userIdentity, Audio objectIdentity) {
        if (!Objects.equals(objectIdentity.getOwnerId(), extractId(userIdentity))) {
            throw new ForbiddenException();
        }
    }

    private Long extractId(UserPrincipal user) {
        return user == null ? null : user.getId();
    }
}
