package ua.tunepoint.audio.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat
public enum RequestAudioType {

    MUSIC("music"), PODCAST("podcast"), AUDIOBOOK("audiobook");

    private final String name;

    RequestAudioType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static RequestAudioType withName(String name) {
        if (name == null) {
            return null;
        }
        for (var type: RequestAudioType.values()) {
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
