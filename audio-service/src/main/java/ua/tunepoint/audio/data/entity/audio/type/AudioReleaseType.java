package ua.tunepoint.audio.data.entity.audio.type;

public enum AudioReleaseType {

    SINGLE("single", 1), ALBUM("album", 2);

    private final String name;
    private final int id;

    AudioReleaseType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static AudioReleaseType withId(int id) {
        for (var type: AudioReleaseType.values())  {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
