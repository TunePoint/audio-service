package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.tunepoint.audio.model.event.playlist.PlaylistAudioAddedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistAudioRemovedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistCreatedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistDeletedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistLikedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistUnlikedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistUpdatedEvent;
import ua.tunepoint.event.model.DomainEventType;

@Getter
@AllArgsConstructor
public enum PlaylistEventType implements DomainEventType {

    PLAYLIST_CREATED("created", PlaylistCreatedEvent.class),
    PLAYLIST_AUDIO_ADDED("audio.added", PlaylistAudioAddedEvent.class),
    PLAYLIST_AUDIO_REMOVED("audio.removed", PlaylistAudioRemovedEvent.class),
    PLAYLIST_DELETED("deleted", PlaylistDeletedEvent.class),
    PLAYLIST_LIKED("liked", PlaylistLikedEvent.class),
    PLAYLIST_UNLIKED("unliked", PlaylistUnlikedEvent.class),
    PLAYLIST_UPDATED("updated", PlaylistUpdatedEvent.class);

    private String name;
    private Class<?> type;
}
