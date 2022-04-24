package ua.tunepoint.audio.security.audio;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class AudioVisibilityAccessManager implements AccessManager<Long, Audio> {

    @Override
    public void authorize(Long userIdentity, Audio objectIdentity) {
        if (objectIdentity.getIsPrivate() && !Objects.equals(objectIdentity.getOwnerId(), userIdentity))  {
            throw new ForbiddenException();
        }
    }
}
