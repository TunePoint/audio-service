package ua.tunepoint.audio.service.client.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.tunepoint.account.model.response.payload.UserPublicPayload;
import ua.tunepoint.audio.model.response.domain.User;

@Mapper(
        componentModel = "spring",
        uses = ResourceMapper.class
)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "profile.firstName"),
            @Mapping(target = "lastName", source = "profile.lastName"),
            @Mapping(target = "avatar", source = "profile.avatar")
    })
    User toUser(UserPublicPayload user);
}
