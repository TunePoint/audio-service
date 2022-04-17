package ua.tunepoint.audio.data.entity;

public interface AccessibleEntity {

    Long getId();
    Long getAuthorId();
    Boolean getIsPrivate();

    default boolean isPrivate() {
        return getIsPrivate() != null && getIsPrivate();
    }
}
