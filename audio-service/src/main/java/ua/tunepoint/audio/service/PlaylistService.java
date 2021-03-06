package ua.tunepoint.audio.service;

import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.entity.IdEntity;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.Tag;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudio;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudioIdentity;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;
import ua.tunepoint.audio.data.mapper.PlaylistMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.GenreRepository;
import ua.tunepoint.audio.data.repository.PlaylistAudioRepository;
import ua.tunepoint.audio.data.repository.PlaylistLikeRepository;
import ua.tunepoint.audio.data.repository.PlaylistRepository;
import ua.tunepoint.audio.data.repository.TagRepository;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;
import ua.tunepoint.audio.model.request.PlaylistUpdateRequest;
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

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import static ua.tunepoint.audio.utils.UserUtils.extractId;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaylistService {

    private final AudioRepository audioRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistLikeRepository playlistLikeRepository;
    private final PlaylistAudioRepository playlistAudioRepository;
    private final TagRepository tagRepository;

    private final ResourceService resourceService;
    private final UserService userService;
    private final PlaylistLikeService playlistLikeService;

    private final RequestMapper requestMapper;
    private final PlaylistMapper playlistMapper;
    private final PlaylistSmartMapper playlistSmartMapper;

    private final EventPublisher eventPublisher;

    private final PlaylistInteractionAccessManager playlistInteractionAccessManager;
    private final PlaylistUpdateAccessManager playlistUpdateAccessManager;
    private final CommonVisibilityAccessManager commonVisibilityAccessManager;
    private final GenreRepository genreRepository;

    @Transactional
    public Long create(PlaylistPostRequest request, ManagerType manager, Long clientId) {

        Set<Long> requestAudioIds = request.getAudioIds();

        Set<AccessibleEntity> authorizedAudioSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(requestAudioIds)) {
            authorizedAudioSet = audioRepository.findAllByIdIn(requestAudioIds, AccessibleEntity.class);

            if (authorizedAudioSet.size() != requestAudioIds.size()) {
                throw new BadRequestException();
            }

            authorizedAudioSet.forEach((audio) -> commonVisibilityAccessManager.authorize(clientId, audio));
        }

        if (request.getCoverId() != null) {
            resourceService.getImage(request.getCoverId())
                    .orElseThrow(() -> new BadRequestException("Image with id " + request.getCoverId() + " doesn't exist"));
        }

        var playlist = requestMapper.toEntity(request, manager, clientId);
        var savedPlaylist = playlistRepository.save(playlist);

        Set<PlaylistAudio> playlistAudioSet = authorizedAudioSet.stream()
                .map(it -> playlistMapper.toPlaylistAudio(savedPlaylist.getId(), it.getId()))
                .collect(Collectors.toSet());

        playlistAudioRepository.saveAll(playlistAudioSet);

        eventPublisher.publish(PLAYLIST.getName(),
                singletonList(EventUtils.toCreatedEvent(savedPlaylist))
        );

        return savedPlaylist.getId();
    }

    @Transactional
    public void update(Long playlistId, PlaylistUpdateRequest request, Long clientId) {
        Playlist playlist = findPlaylistRequired(playlistId);

        playlistUpdateAccessManager.authorize(clientId, playlist);

        if (request.getCoverId() != null) {
            resourceService.getImage(request.getCoverId())
                    .orElseThrow(NotFoundException::new);
        }

        playlist = playlistMapper.merge(playlist, request);

        Playlist savedPlaylist = playlistRepository.save(playlist);

        eventPublisher.publish(
                PLAYLIST.getName(),
                singletonList(toUpdatedEvent(savedPlaylist))
        );
    }

    public PlaylistPayload findById(Long playlistId, Long clientId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(NotFoundException::new);

        commonVisibilityAccessManager.authorize(clientId, playlist);

        return playlistSmartMapper.toPayload(playlist, playlistLikeService.isLiked(playlistId, clientId));
    }

    public Page<PlaylistPayload> findByOwner(Long ownerId, Pageable pageable, UserPrincipal user) {
        var owner = userService.findUser(ownerId)
                .orElseThrow(() -> new NotFoundException("user with id " + ownerId + " was not found"));
        var page = playlistRepository.findByOwnerIdWithProtected(ownerId, extractId(user), pageable);

        var liked = user == null ? new HashSet<Long>() :
                playlistLikeService.likedFromBulk(page.stream().map(Playlist::getId)
                        .collect(Collectors.toSet()), extractId(user));

        log.info("Fetched liked playlists: {}", liked);

        return page.map(it -> playlistSmartMapper.toPayload(it, owner, liked.contains(it.getId())));
    }

    public Page<PlaylistPayload> findByUserLiked(Long userId, Pageable pageable, Long clientId) {
        var page = playlistRepository.findPlaylistLikedByUserProtected(userId, clientId, pageable);
        return mapToPayload(page, clientId);
    }

    public Page<PlaylistPayload> findByContainingAudio(Long audioId, @Nullable Long clientId, Pageable pageable) {
        var page = playlistRepository.findPlaylistsContainingAudioProtected(audioId, clientId, pageable);
        return mapToPayload(page, clientId);
    }

    private Page<PlaylistPayload> mapToPayload(Page<Playlist> page, Long clientId) {
        var liked = clientId == null ? new HashSet<Long>() :
                playlistLikeService.likedFromBulk(page.stream().map(Playlist::getId)
                        .collect(Collectors.toSet()), clientId);

        return page.map(it -> playlistSmartMapper.toPayload(it, liked.contains(it.getId())));
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

    @Transactional
    public void addTag(Long playlistId, Long tagId, Long clientId) {
        var context = playlistTagContext(playlistId, tagId);
        playlistUpdateAccessManager.authorize(clientId, context.playlist);

        var added = context.playlist.getTags().add(context.tag);
        if (!added) {
            throw new BadRequestException("tag was already added");
        }
    }

    @Transactional
    public void removeTag(Long playlistId, Long tagId, Long clientId) {
        var context = playlistTagContext(playlistId, tagId);
        playlistUpdateAccessManager.authorize(clientId, context.playlist);

        var removed = context.playlist.getTags().remove(context.tag);
        if (!removed) {
            throw new BadRequestException("tag was already removed");
        }
    }

    @Transactional
    public void addGenre(Long playlistId, Long genreId, Long clientId) {
        var context = playlistGenreContext(playlistId, genreId);
        playlistUpdateAccessManager.authorize(clientId, context.playlist);

        var added = context.playlist.getGenres().add(context.genre);
        if (!added) {
            throw new BadRequestException("tag was already added");
        }
    }

    @Transactional
    public void removeGenre(Long playlistId, Long genreId, Long clientId) {
        var context = playlistGenreContext(playlistId, genreId);
        playlistUpdateAccessManager.authorize(clientId, context.playlist);

        var removed = context.playlist.getGenres().remove(context.genre);
        if (!removed) {
            throw new BadRequestException("tag was already removed");
        }
    }

    public void authorizeAccess(Long playlistId, @Nullable Long userId) {
        var playlist = playlistRepository.findById(playlistId, AccessibleEntity.class)
                .orElseThrow(() -> new NotFoundException("playlist with id " + playlistId + " was not found"));
        commonVisibilityAccessManager.authorize(userId, playlist);
    }

    private PlaylistTagContext playlistTagContext(Long playlistId, Long tagId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(
                        () -> new NotFoundException("playlist with id " + playlistId + " was not found")
                );

        var tag = tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new NotFoundException("tag with id " + tagId + " was not found")
                );

        return new PlaylistTagContext(playlist, tag);
    }

    private PlaylistGenreContext playlistGenreContext(Long playlistId, Long genreId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(
                        () -> new NotFoundException("playlist with id " + playlistId + " was not found")
                );

        var genre = genreRepository.findById(genreId)
                .orElseThrow(
                        () -> new NotFoundException("tag with id " + genreId + " was not found")
                );

        return new PlaylistGenreContext(playlist, genre);
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

    public List<PlaylistPayload> searchBulk(List<Long> ids, @Nullable Long clientId) {
        var bulk = playlistRepository.findBulk(ids);

        var liked = clientId == null ? new HashSet<Long>() :
                playlistLikeService.likedFromBulk(bulk.stream().map(Playlist::getId).collect(Collectors.toSet()), clientId);

        return bulk
                .stream().map(it -> playlistSmartMapper.toPayload(it, liked.contains(it.getId())))
                .sorted(Comparator.comparing(it -> ids.indexOf(it.getId())))
                .collect(Collectors.toList());
    }

    private static record PlaylistUpdateContext(PlaylistAccessibleEntity playlist, AccessibleEntity audio) {
    }

    private static record PlaylistTagContext(Playlist playlist, Tag tag) {
    }

    private record PlaylistGenreContext(Playlist playlist, Genre genre) {
    }
}
