package ua.tunepoint.audio.security.audio;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.security.AccessManager;
import ua.tunepoint.security.UserPrincipal;

@Component
public class AudioNoopAccessManager implements AccessManager<Long, Audio> {

    @Override
    public void authorize(Long userIdentity, Audio objectIdentity) {

    }
}
