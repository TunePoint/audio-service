package ua.tunepoint.audio.data.entity.playlist;

/**
 * Describe how (or by whom) playlist is managed.
 * !WARNING! field id SHOULD NOT be changed during development process
 */
public enum ManagerType {

    /**
     * Playlist is created and managed by user. So it can be updated or deleted by owner
     */
    USER(1),
    /**
     * Playlist is created and managed by service. User cannot delete or update such playlist manually
     * (only by performing certain actions, i.e. like an audio)
     */
    SERVICE_LIKES(2);

    private final int id;

    ManagerType(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public static ManagerType byId(int id) {
        for (var type: ManagerType.values()) {
            if (id == type.id()) {
                return type;
            }
        }
        return null;
    }
}
