package ua.tunepoint.audio.service;

import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.audio.Audio;
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
import ua.tunepoint.audio.security.CommonUpdateAccessManager;
import ua.tunepoint.audio.security.CommonVisibilityAccessManager;
import ua.tunepoint.audio.security.playlist.PlaylistInteractionAccessManager;
import ua.tunepoint.audio.security.playlist.PlaylistUpdateAccessManager;
import ua.tunepoint.audio.service.support.PlaylistSmartMapper;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.PLAYLIST;
import static ua.tunepoint.audio.utils.EventUtils.toCreateEvent;

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
    public PlaylistPayload create(PlaylistPostRequest request, UserPrincipal user) {

        Set<Long> requestAudioIds = request.getAudioIds();

        Set<AccessibleEntity> authorizedAudioSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(requestAudioIds)) {
            authorizedAudioSet = audioRepository.findAllByIdIn(requestAudioIds, AccessibleEntity.class);

            if (authorizedAudioSet.size() != requestAudioIds.size()) {
                throw new BadRequestException();
            }

            authorizedAudioSet.forEach((audio) -> commonVisibilityAccessManager.authorize(user, audio));
        }

        Resource cover = null;
        if (request.getCoverId() != null) {
            cover = resourceService.getImage(request.getCoverId())
                    .orElseThrow(() -> new BadRequestException("Image with id " + request.getCoverId() + " doesn't exist"));
        }

        Playlist playlist = requestMapper.toEntity(request, user.getId());
        Playlist savedPlaylist = playlistRepository.save(playlist);

        Set<PlaylistAudio> playlistAudioSet = authorizedAudioSet.stream()
                .map(it -> playlistMapper.toPlaylistAudio(savedPlaylist.getId(), it.getId()))
                .collect(Collectors.toSet());
        playlistAudioRepository.saveAll(playlistAudioSet);

        var payload = playlistSmartMapper.toPayload(savedPlaylist, cover);

        eventPublisher.publish(PLAYLIST.getName(),
                singletonList(toCreateEvent(savedPlaylist))
        );

        return payload;
    }

    @Transactional
    public PlaylistPayload update(Long playlistId, PlaylistUpdateRequest request, UserPrincipal user) {
        Playlist playlist = findPlaylistRequired(playlistId);

        playlistUpdateAccessManager.authorize(user, playlist);

        Resource cover = null;
        if (request.getCoverId() != null) {
            cover = resourceService.getImage(request.getCoverId())
                    .orElseThrow(NotFoundException::new);
        }

        playlist = playlistMapper.merge(playlist, request);

        Playlist savedPlaylist = playlistRepository.save(playlist); // TODO: publish event

        return playlistSmartMapper.toPayload(savedPlaylist, cover);
    }

    public PlaylistPayload findById(Long playlistId, UserPrincipal user) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(NotFoundException::new);

        commonVisibilityAccessManager.authorize(user, playlist);

        return playlistSmartMapper.toPayload(playlist);
    }

    public Page<PlaylistPayload> findByOwner(Long ownerId, Pageable pageable, UserPrincipal user) {
        return playlistRepository.findByOwnerIdWithAccessControl(ownerId, user == null ? null : user.getId(), pageable)
                .map(playlistSmartMapper::toPayload);
    }

    @Transactional
    public void like(Long playlistId, UserPrincipal user) {
         var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                 .orElseThrow(NotFoundException::new);

         playlistInteractionAccessManager.authorize(user, playlistAccessible);
         commonVisibilityAccessManager.authorize(user, playlistAccessible);

         var likeIdentity = new PlaylistLikeIdentity(playlistId, user.getId());
         if (playlistLikeRepository.existsById(likeIdentity)) {
             throw new BadRequestException("Like is already set");
         }

         var like = playlistMapper.toLike(likeIdentity);
         playlistLikeRepository.save(like); // TODO: publish event
    }

    @Transactional
    public void unlike(Long playlistId, UserPrincipal user) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);

        playlistInteractionAccessManager.authorize(user, playlistAccessible);
        commonVisibilityAccessManager.authorize(user, playlistAccessible);

        var likeIdentity = new PlaylistLikeIdentity(playlistId, user.getId());
        if (!playlistLikeRepository.existsById(likeIdentity)) {
            throw new BadRequestException("Like is not set");
        }

        playlistLikeRepository.deleteById(likeIdentity); // TODO: publish event
    }

    @Transactional
    public void delete(Long playlistId, UserPrincipal user) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);

        playlistUpdateAccessManager.authorize(user, playlistAccessible);

        playlistRepository.deleteById(playlistId); // TODO: publish event
    }

    @Transactional
    public void addAudio(Long playlistId, Long audioId, UserPrincipal user) {
        var id = new PlaylistAudioIdentity(playlistId, audioId);
        if (playlistAudioRepository.existsById(id)) {
            throw new BadRequestException("Audio already in playlist");
        }

        authorizePlaylistUpdate(playlistId, audioId, user);

        var entry = playlistMapper.toPlaylistAudio(id);

        playlistAudioRepository.save(entry); //TODO: publish event
    }

    @Transactional
    public void removeAudio(Long playlistId, Long audioId, UserPrincipal user) {
        var id = new PlaylistAudioIdentity(playlistId, audioId);
        if (!playlistAudioRepository.existsById(id)) {
            throw new BadRequestException("Audio is not in playlist");
        }

        authorizePlaylistUpdate(playlistId, audioId, user);

        playlistAudioRepository.deleteById(id); // TODO: publish event
    }

    private void authorizePlaylistUpdate(Long playlistId, Long audioId, UserPrincipal user) {
        var playlistAccessible = playlistRepository.findById(playlistId, PlaylistAccessibleEntity.class)
                .orElseThrow(NotFoundException::new);
        playlistUpdateAccessManager.authorize(user, playlistAccessible);

        var audioAccessible = audioRepository.findById(audioId, AccessibleEntity.class)
                .orElseThrow(NotFoundException::new);
        commonVisibilityAccessManager.authorize(user, audioAccessible);
    }

    private Playlist findPlaylistRequired(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Playlist with id " + id + " was not found"));
    }

    private Audio findAudioRequired(Long id) {
        return audioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio with id " + id + " was not found"));
    }
}
