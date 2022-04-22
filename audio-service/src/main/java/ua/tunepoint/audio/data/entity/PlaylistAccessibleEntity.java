package ua.tunepoint.audio.data.entity;

import ua.tunepoint.audio.data.entity.playlist.ManagerType;

public interface PlaylistAccessibleEntity extends AccessibleEntity {

    ManagerType getManagerType();
}
