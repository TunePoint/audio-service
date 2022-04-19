package ua.tunepoint.audio.data.entity;

public interface PlaylistAccessibleEntity extends AccessibleEntity {

    Boolean getIsService();

    default boolean isService() {
        return getIsService() != null && getIsService();
    }
}
