package ua.tunepoint.audio.service.client.mapper;

import org.mapstruct.Mapper;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.resource.model.response.payload.AudioResourcePayload;
import ua.tunepoint.resource.model.response.payload.ImageResourcePayload;


@Mapper(componentModel = "spring")
public interface ResourceMapper {

    Resource toPayload(AudioResourcePayload audio);

    Resource toPayload(ImageResourcePayload image);

    Resource toLocalResource(ua.tunepoint.account.model.response.domain.Resource resource);
}
