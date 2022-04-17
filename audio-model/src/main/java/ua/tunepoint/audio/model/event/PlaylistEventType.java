package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.tunepoint.audio.model.event.playlist.PlaylistCreateEvent;
import ua.tunepoint.event.model.DomainEventType;

@Getter
@AllArgsConstructor
public enum PlaylistEventType implements DomainEventType {

    PLAYLIST_CREATE("create", PlaylistCreateEvent.class);

    private String name;
    private Class<?> type;
}
