package ua.tunepoint.audio.security;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class CommonUpdateAccessManager implements AccessManager<Long, AccessibleEntity>{

    @Override
    public void authorize(Long user, AccessibleEntity objectIdentity) {
        if (!Objects.equals(user, objectIdentity.getOwnerId())) {
            throw new ForbiddenException();
        }
    }
}
