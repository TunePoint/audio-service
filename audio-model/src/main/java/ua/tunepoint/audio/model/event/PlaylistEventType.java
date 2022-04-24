package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.tunepoint.audio.model.event.playlist.PlaylistCreatedEvent;
import ua.tunepoint.event.model.DomainEventType;

@Getter
@AllArgsConstructor
public enum PlaylistEventType implements DomainEventType {

    PLAYLIST_CREATE("create", PlaylistCreatedEvent.class);

    private String name;
    private Class<?> type;
}
