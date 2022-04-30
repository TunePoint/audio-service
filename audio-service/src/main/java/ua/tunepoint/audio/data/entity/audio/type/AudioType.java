package ua.tunepoint.audio.data.entity.audio.type;

public enum AudioType {

    MUSIC("music", 1),
    PODCAST("podcast", 2),
    AUDIOBOOK("audiobook", 3);

    private final String name;
    private final int id;

    AudioType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static AudioType withId(int id) {
        for (var type: AudioType.values()) {
            if (id == type.getId()) {
                return type;
            }
        }
        return null;
    }

    public static AudioType withName(String name) {
        if (name == null) {
            return null;
        }

        for (var type: AudioType.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
