package ua.tunepoint.audio.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat
public enum RequestPlaylistType {

    PLAYLIST("playlist"),
    ALBUM("album"),
    EP("ep"),
    SINGLE("single"),
    COMPILATION("compilation");

    private final String name;

    RequestPlaylistType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static RequestPlaylistType withName(String name) {
        if (name == null) {
            return null;
        }
        for (var type: RequestPlaylistType.values()) {
            if (name.equals(type.toString())) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unable to convert '" + name + "'");
    }

    @Override
    @JsonValue
    public String toString() {
        return name;
    }
}
