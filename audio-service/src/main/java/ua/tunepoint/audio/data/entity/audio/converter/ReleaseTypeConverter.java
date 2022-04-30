package ua.tunepoint.audio.data.entity.audio.converter;

import ua.tunepoint.audio.data.entity.audio.AudioReleaseType;

import javax.persistence.AttributeConverter;

public class ReleaseTypeConverter implements AttributeConverter<AudioReleaseType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AudioReleaseType attribute) {
        if (attribute == null) {
            return AudioReleaseType.SINGLE.id();
        }
        return attribute.id();
    }

    @Override
    public AudioReleaseType convertToEntityAttribute(Integer dbData) {
        return AudioReleaseType.byId(dbData);
    }
}
