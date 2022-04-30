package ua.tunepoint.audio.data.entity.audio.converter;

import ua.tunepoint.audio.data.entity.audio.type.AudioReleaseType;

import javax.persistence.AttributeConverter;

public class ReleaseTypeConverter implements AttributeConverter<AudioReleaseType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AudioReleaseType attribute) {
        return attribute == null ? null : attribute.getId();
    }

    @Override
    public AudioReleaseType convertToEntityAttribute(Integer id) {
        return id == null ? null : AudioReleaseType.withId(id);
    }
}
