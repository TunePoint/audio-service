package ua.tunepoint.audio.utils;

import lombok.experimental.UtilityClass;
import ua.tunepoint.security.UserPrincipal;

import java.util.Optional;

@UtilityClass
public class UserUtils {

    public static Long extractId(UserPrincipal user) {
        return Optional.ofNullable(user).map(UserPrincipal::getId).orElse(null);
    }
}
