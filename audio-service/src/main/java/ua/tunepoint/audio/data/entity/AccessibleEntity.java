package ua.tunepoint.audio.data.entity;

public interface AccessibleEntity extends IdEntity{

    Long getOwnerId();
    Boolean getIsPrivate();

    default boolean isPrivate() {
        return getIsPrivate() != null && getIsPrivate();
    }
}
