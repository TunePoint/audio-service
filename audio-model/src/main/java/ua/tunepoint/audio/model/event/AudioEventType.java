package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import ua.tunepoint.audio.model.event.audio.AudioCreatedEvent;
import ua.tunepoint.audio.model.event.audio.AudioDeletedEvent;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioListenEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUpdatedEvent;
import ua.tunepoint.event.model.DomainEventType;

@AllArgsConstructor
public enum AudioEventType implements DomainEventType {

    AUDIO_CREATE("created", AudioCreatedEvent.class),
    AUDIO_UPDATED("updated", AudioUpdatedEvent.class),
    AUDIO_DELETED("deleted", AudioDeletedEvent.class),
    AUDIO_LIKE("liked", AudioLikeEvent.class),
    AUDIO_UNLIKE("unliked", AudioUnlikeEvent.class),
    AUDIO_LISTENING("listened", AudioListenEvent.class);

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
