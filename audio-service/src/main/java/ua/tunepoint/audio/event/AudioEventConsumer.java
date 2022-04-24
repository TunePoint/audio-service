package ua.tunepoint.audio.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.config.properties.ServicePlaylistProperties;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;
import ua.tunepoint.audio.service.PlaylistService;
import ua.tunepoint.auth.model.event.user.UserCreatedEvent;
import ua.tunepoint.event.starter.handler.DomainEventHandlers;
import ua.tunepoint.event.starter.handler.DomainEventHandlersBuilder;
import ua.tunepoint.event.starter.registry.DomainRegistry;

import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.auth.model.event.AuthDomain.USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AudioEventConsumer {

    private final DomainRegistry domainRegistry;
    private final PlaylistService playlistService;
    private final ServicePlaylistProperties playlistProperties;

    public DomainEventHandlers eventHandlers() {
        return DomainEventHandlersBuilder.withRegistry(domainRegistry)
                .forDomain(AUDIO.getName())
                .onEvent(AudioLikeEvent.class, this::handleAudioLike)
                .onEvent(AudioUnlikeEvent.class, this::handleAudioUnlike)
                .forDomain(USER.getName())
                .onEvent(UserCreatedEvent.class, this::handleUserCreated)
                .build();
    }

    private void handleAudioLike(AudioLikeEvent event) {
        log.info("Handling audio.like event: " + event);

        playlistService.addAudio(
                ManagerType.SERVICE_LIKES,
                event.getAudioId(),
                event.getUserId()
        );
    }

    private void handleAudioUnlike(AudioUnlikeEvent event) {
        log.info("Handling audio.unlike event: " + event);

        playlistService.removeAudio(
                ManagerType.SERVICE_LIKES,
                event.getAudioId(),
                event.getUserId()
        );
    }

    private void handleUserCreated(UserCreatedEvent event) {
        log.info("Handling user.create event: " + event);

        playlistService.create(
                PlaylistPostRequest.builder()
                        .title(playlistProperties.getFavourite().getTitle())
                        .description(playlistProperties.getFavourite().getDescription())
                        .isPrivate(true)
                        .build(),
                ManagerType.SERVICE_LIKES,
                event.getUserId()
        );
    }
}
