package ua.tunepoint.audio.data.entity.playlist.converter;

import ua.tunepoint.audio.data.entity.playlist.PlaylistType;

import javax.persistence.AttributeConverter;

public class PlaylistTypeConverter implements AttributeConverter<PlaylistType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PlaylistType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public PlaylistType convertToEntityAttribute(Integer id) {
        return id == null ? null : PlaylistType.withId(id);
    }
}
