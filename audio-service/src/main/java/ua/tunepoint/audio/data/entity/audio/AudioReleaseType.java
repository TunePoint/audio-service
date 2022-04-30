package ua.tunepoint.audio.data.entity.audio;

public enum AudioReleaseType {

    SINGLE(1), ALBUM(2);

    private final int id;

    AudioReleaseType(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }

    public static AudioReleaseType byId(int id) {
        for (var type: AudioReleaseType.values())  {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
