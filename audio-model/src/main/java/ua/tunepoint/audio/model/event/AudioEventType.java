package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import ua.tunepoint.audio.model.event.audio.AudioCreatedEvent;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioListenEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.event.model.DomainEventType;

@AllArgsConstructor
public enum AudioEventType implements DomainEventType {

    AUDIO_CREATE("create", AudioCreatedEvent.class),
    AUDIO_LIKE("like", AudioLikeEvent.class),
    AUDIO_UNLIKE("unlike", AudioUnlikeEvent.class),
    AUDIO_LISTENING("listening", AudioListenEvent.class);

    private final String name;
    private final Class<?> type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
