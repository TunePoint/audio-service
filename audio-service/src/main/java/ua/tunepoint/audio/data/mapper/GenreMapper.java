package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.model.response.payload.GenrePayload;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    GenrePayload toPayload(Genre genre);
}
