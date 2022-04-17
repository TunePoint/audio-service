package ua.tunepoint.audio.security;

import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.ForbiddenException;

import java.util.Objects;

@Component
public class CommonUpdateAccessManager implements AccessManager<UserPrincipal, AccessibleEntity>{

    @Override
    public void authorize(UserPrincipal userIdentity, AccessibleEntity objectIdentity) {
        if (!Objects.equals(extractUserId(userIdentity), objectIdentity.getOwnerId())) {
            throw new ForbiddenException();
        }
    }

    private Long extractUserId(UserPrincipal user) {
        return user == null ? null : user.getId();
    }
}
