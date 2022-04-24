package ua.tunepoint.audio.data.entity.playlist.converter;

import ua.tunepoint.audio.data.entity.playlist.ManagerType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ManagerTypeConverter implements AttributeConverter<ManagerType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ManagerType attribute) {
        return attribute.id();
    }

    @Override
    public ManagerType convertToEntityAttribute(Integer dbData) {
        return ManagerType.byId(dbData);
    }
}
