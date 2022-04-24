package ua.tunepoint.audio.service;

import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.IdEntity;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudio;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudioIdentity;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;
import ua.tunepoint.audio.data.mapper.PlaylistMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.PlaylistAudioRepository;
import ua.tunepoint.audio.data.repository.PlaylistLikeRepository;
import ua.tunepoint.audio.data.repository.PlaylistRepository;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;
import ua.tunepoint.audio.model.request.PlaylistUpdateRequest;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.payload.PlaylistPayload;
import ua.tunepoint.audio.security.CommonVisibilityAccessManager;
import ua.tunepoint.audio.security.playlist.PlaylistInteractionAccessManager;
import ua.tunepoint.audio.security.playlist.PlaylistUpdateAccessManager;
import ua.tunepoint.audio.service.support.PlaylistSmartMapper;
import ua.tunepoint.audio.utils.EventUtils;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.PLAYLIST;
import static ua.tunepoint.audio.utils.EventUtils.toAddedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toDeletedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toLikedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toRemovedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUnlikedEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUpdatedEvent;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final AudioRepository audioRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistLikeRepository playlistLikeRepository;
    private final PlaylistAudioRepository playlistAudioRepository;

    private final ResourceService resourceService;

    private final RequestMapper requestMapper;
    private final PlaylistMapper playlistMapper;
    private final PlaylistSmartMapper playlistSmartMapper;

    private final EventPublisher eventPublisher;

    private final PlaylistInteractionAccessManager playlistInteractionAccessManager;
    private final PlaylistUpdateAccessManager playlistUpdateAccessManager;
    private final CommonVisibilityAccessManager commonVisibilityAccessManager;

    @Transactional
    public PlaylistPayload create(PlaylistPostRequest request, ManagerType manager, Long clientId) {

        Set<Long> requestAudioIds = request.getAudioIds();

        Set<AccessibleEntity> authorizedAudioSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(requestAudioIds)) {
            authorizedAudioSet = audioRepository.findAllByIdIn(requestAudioIds, AccessibleEntity.class);

            if (authorizedAudioSet.size() != requestAudioIds.size()) {
                throw new BadRequestException();
            }

            authorizedAudioSet.forEach((audio) -> commonVisibilityAccessManager.authorize(clientId, audio));
        }

        Resource cover = null;
        if (request.getCoverId() != null) {
            cover = resourceService.getImage(request.getCoverId())
                    .orElseThrow(() -> new BadRequestException("Image with id " + request.getCoverId() + " doesn't exist"));
        }

        var playlist = requestMapper.toEntity(request, manager, clientId);
        var savedPlaylist = playlistRepository.save(playlist);

        Set<PlaylistAudio> playlistAudioSet = authorizedAudioSet.stream()
                .map(it -> playlistMapper.toPlaylistAudio(savedPlaylist.getId(), it.getId()))
                .collect(Collectors.toSet());

        playlistAudioRepository.saveAll(playlistAudioSet);

        var payload = playlistSmartMapper.toPayload(savedPlaylist, cover);

        eventPublisher.publish(PLAYLIST.getName(),
                singletonList(EventUtils.toCreatedEvent(savedPlaylist))
        );

        return payload;
    }

    @Transactional
    public PlaylistPayload update(Long playlistId, PlaylistUpdateRequest request, Long clientId) {
        Playlist playlist = findPlaylistRequired(playlistId);

        playlistUpdateAccessManager.authorize(clientId, playlist);

        Resource cover = null;
        if (request.getCoverId() != null) {
            cover = resourceService.getImage(request.getCoverId())
                    .orElseThrow(NotFoundException::new);
        }

        playlist = playlistMapper.merge(playlist, request);

        Playlist savedPlaylist = playlistRepository.save(playlist); // TODO: publish event

        var payload =  playlistSmartMapper.toPayload(savedPlaylist, cover);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toUpdatedEvent(playlist))
        );

        return payload;
    }

    public PlaylistPayload findById(Long playlistId, Long clientId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(NotFoundException::new);

        commonVisibilityAccessManager.authorize(clientId, playlist);

        return playlistSmartMapper.toPayload(playlist);
    }

    public Page<PlaylistPayload> findByOwner(Long ownerId, Pageable pageable, UserPrincipal user) {
        return playlistRepository.findByOwnerIdWithAccessControl(ownerId, user == null ? null : user.getId(), pageable)
                .map(playlistSmartMapper::toPayload);
    }

    @Transactional
    public void like(Long playlistId, Long clientId) {
         var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                 .orElseThrow(NotFoundException::new);

         playlistInteractionAccessManager.authorize(clientId, playlistAccessible);
         commonVisibilityAccessManager.authorize(clientId, playlistAccessible);

         var likeIdentity = new PlaylistLikeIdentity(playlistId, clientId);
         if (playlistLikeRepository.existsById(likeIdentity)) {
             throw new BadRequestException("Like is already set");
         }

         var like = playlistMapper.toLike(likeIdentity);
         playlistLikeRepository.save(like);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toLikedEvent(playlistAccessible, clientId))
        );
    }

    @Transactional
    public void unlike(Long playlistId, Long clientId) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);

        playlistInteractionAccessManager.authorize(clientId, playlistAccessible);
        commonVisibilityAccessManager.authorize(clientId, playlistAccessible);

        var likeIdentity = new PlaylistLikeIdentity(playlistId, clientId);
        if (!playlistLikeRepository.existsById(likeIdentity)) {
            throw new BadRequestException("Like is not set");
        }

        playlistLikeRepository.deleteById(likeIdentity);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toUnlikedEvent(playlistAccessible, clientId))
        );
    }

    @Transactional
    public void delete(Long playlistId, Long clientId) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);

        playlistUpdateAccessManager.authorize(clientId, playlistAccessible);

        playlistRepository.deleteById(playlistId);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toDeletedEvent(playlistAccessible))
        );
    }

    @Transactional
    public void addAudio(Long playlistId, Long audioId, Long clientId) {
        var id = new PlaylistAudioIdentity(playlistId, audioId);
        if (playlistAudioRepository.existsById(id)) {
            throw new BadRequestException("Audio already in playlist");
        }

        var context = authorizePlaylistUpdate(playlistId, audioId, clientId);

        addAudio(playlistId, audioId);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toAddedEvent(context.playlist, context.audio, clientId))
        );
    }

    @Transactional
    public void addAudio(Long playlistId, Long audioId) {
        var entry = playlistMapper.toPlaylistAudio(playlistId, audioId);

        playlistAudioRepository.save(entry);
    }

    @Transactional
    public void removeAudio(Long playlistId, Long audioId, Long clientId) {
        var id = new PlaylistAudioIdentity(playlistId, audioId);
        if (!playlistAudioRepository.existsById(id)) {
            throw new BadRequestException("Audio is not in playlist");
        }

        var context = authorizePlaylistUpdate(playlistId, audioId, clientId);

        playlistAudioRepository.deleteById(id);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toRemovedEvent(context.playlist, context.audio, clientId))
        );
    }

    @Transactional
    public void removeAudio(Long playlistId, Long audioId) {
        var id = new PlaylistAudioIdentity(playlistId, audioId);

        playlistAudioRepository.deleteById(id);
    }

    @Transactional
    public void addAudio(ManagerType manager, Long audioId, Long clientId) {
        playlistRepository.findByManagerTypeAndOwnerId(
                manager, clientId, IdEntity.class
        ).forEach(
                it -> addAudio(it.getId(), audioId)
        );
    }

    @Transactional
    public void removeAudio(ManagerType manager, Long audioId, Long clientId) {
        playlistRepository.findByManagerTypeAndOwnerId(
                manager, clientId, IdEntity.class
        ).forEach(
                it -> removeAudio(it.getId(), audioId)
        );
    }

    private PlaylistUpdateContext authorizePlaylistUpdate(Long playlistId, Long audioId, Long clientId) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);
        playlistUpdateAccessManager.authorize(clientId, playlistAccessible);

        var audioAccessible = audioRepository.findById(audioId, AccessibleEntity.class)
                .orElseThrow(NotFoundException::new);
        commonVisibilityAccessManager.authorize(clientId, audioAccessible);

        return new PlaylistUpdateContext(playlistAccessible, audioAccessible);
    }

    private Playlist findPlaylistRequired(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + id + " was not found"));
    }

    private static record PlaylistUpdateContext(PlaylistAccessibleEntity playlist, AccessibleEntity audio) { }
}
