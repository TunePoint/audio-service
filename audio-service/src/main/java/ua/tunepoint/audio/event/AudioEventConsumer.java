package ua.tunepoint.audio.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.audio.service.PlaylistService;
import ua.tunepoint.event.starter.handler.DomainEventHandlers;
import ua.tunepoint.event.starter.handler.DomainEventHandlersBuilder;
import ua.tunepoint.event.starter.registry.DomainRegistry;
import ua.tunepoint.model.event.user.UserCreatedEvent;

import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.model.event.AccountDomain.USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AudioEventConsumer {

    private final DomainRegistry domainRegistry;
    private final PlaylistService playlistService;

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
        // playlistService.findByManagerType(ManagerType.SERVICE_LIKES, event.getUserId())
        //                      .forEach(it -> playlistService.addToPlaylist(it, event.getAudioId()));
    }

    private void handleAudioUnlike(AudioUnlikeEvent event) {
        log.info("Handling audio.unlike event: " + event);
        // playlistService.findByManagerType(ManagerType.SERVICE_LIKES, event.getUserId())
        //                      .forEach(it -> playlistService.removeFromPlaylist(it, event.getAudioId()));
    }

    private void handleUserCreated(UserCreatedEvent event) {
        log.info("Handling user.create event: " + event);
        // playlistService.createPlaylist(name, ManagerType.SERVICE_LIKES, event.getUserId())
    }
}
