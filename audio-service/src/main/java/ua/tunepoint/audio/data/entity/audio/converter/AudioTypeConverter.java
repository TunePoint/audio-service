package ua.tunepoint.audio.data.entity.audio.converter;

import ua.tunepoint.audio.data.entity.audio.type.AudioType;

import javax.persistence.AttributeConverter;

public class AudioTypeConverter implements AttributeConverter<AudioType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AudioType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public AudioType convertToEntityAttribute(Integer id) {
        return id == null ? null : AudioType.withId(id);
    }
}
