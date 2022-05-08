package ua.tunepoint.audio.data.entity.playlist;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;

public enum PlaylistType {

    PLAYLIST(1, "playlist"),
    ALBUM(2, "album"),
    EP(3, "ep"),
    SINGLE(4, "single"),
    COMPILATION(5, "compilation");

    private final int id;
    private final String name;

    PlaylistType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static PlaylistType withId(Integer id) {
        if (id == null) {
            return null;
        }

        for (var type: PlaylistType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    @Null
    public static PlaylistType withName(String name) {
        if (name == null) {
            return null;
        }

        for (var type: PlaylistType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
