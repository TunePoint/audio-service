package ua.tunepoint.audio.service.client.mapper;

import org.mapstruct.Mapper;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.account.model.response.payload.ProfilePayload;

@Mapper(
        componentModel = "spring",
        uses = ResourceMapper.class
)
public interface UserMapper {

    User toUser(ProfilePayload profile);
}
