package ua.tunepoint.audio.security;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class CommonVisibilityAccessManager implements AccessManager<Long, AccessibleEntity> {

    @Override
    public void authorize(Long user, AccessibleEntity entity) {
        if (entity.isPrivate() && !Objects.equals(user, entity.getOwnerId())) {
            throw new ForbiddenException();
        }
    }
}
