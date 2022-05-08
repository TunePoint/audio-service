package ua.tunepoint.audio.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.entity.Tag;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.mapper.AudioMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioLikeRepository;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.GenreRepository;
import ua.tunepoint.audio.data.repository.PlaylistRepository;
import ua.tunepoint.audio.data.repository.TagRepository;
import ua.tunepoint.audio.model.request.AudioPostRequest;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.audio.security.CommonVisibilityAccessManager;
import ua.tunepoint.audio.security.audio.AudioUpdateAccessManager;
import ua.tunepoint.audio.security.audio.AudioVisibilityAccessManager;
import ua.tunepoint.audio.service.support.AudioSmartMapper;
import ua.tunepoint.audio.utils.EventUtils;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.audio.utils.EventUtils.toLikeEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUnlikeEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUpdatedEvent;

@Service
@RequiredArgsConstructor
public class AudioService {

    private final AudioRepository audioRepository;
    private final AudioLikeRepository audioLikeRepository;
    private final PlaylistRepository playlistRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;

    private final ResourceService resourceService;
    private final UserService userService;
    private final AudioLikeService audioLikeService;

    private final RequestMapper requestMapper;
    private final AudioMapper audioMapper;
    private final AudioSmartMapper audioSmartMapper;

    private final AudioVisibilityAccessManager visibilityAccessManager;
    private final AudioUpdateAccessManager updateAccessManager;
    private final CommonVisibilityAccessManager commonVisibilityAccessManager;

    private final EventPublisher publisher;

    @Transactional
    public Long save(AudioPostRequest request, @NotNull Long user) {
        getAudioRequired(request.getContentId());
        if (request.getCoverId() != null) {
            getImageRequired(request.getCoverId());
        }

        var genres = request.getGenreIds() == null ? null :
                request.getGenreIds().stream()
                        .map(it -> genreRepository.findById(it)
                                .orElseThrow(
                                        () -> new NotFoundException("genre with id " + it + " was not found")
                                )
                        ).collect(Collectors.toSet());

        var audio = requestMapper.toEntity(request, genres, user);
        var savedAudio = audioRepository.save(audio);

        publisher.publish(AUDIO.getName(),
                singletonList(EventUtils.toCreatedEvent(audio, user))
        );
        return savedAudio.getId();
    }

    public List<AudioPayload> searchBulk(List<Long> ids, @Nullable Long clientId) {
        final var bulk = audioRepository.findBulk(ids);

        final var liked = clientId == null ? new HashSet<Long>() :
            audioLikeService.likedFromBulk(bulk.stream().map(Audio::getId).collect(Collectors.toSet()), clientId);

        return bulk.stream().map(it -> audioSmartMapper.toPayload(it, liked.contains(it.getId())))
                .sorted(Comparator.comparing(it -> ids.indexOf(it.getId())))
                .collect(Collectors.toList());
    }

    public Page<AudioPayload> findByOwner(@NotNull Long ownerId, @Nullable Long currentUser, Pageable pageable) {
        var owner = userService.findUser(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " was not found"));

        Page<Audio> page = ownerId.equals(currentUser) ? audioRepository.findAudioByOwnerId(ownerId, pageable)
                : audioRepository.findAudioByOwnerIdAndIsPrivateFalse(ownerId, pageable);

        var liked = currentUser == null ? new HashSet<Long>() :
                audioLikeService.likedFromBulk(page.stream().map(Audio::getId).collect(Collectors.toSet()), currentUser);

        return page.map(it -> audioSmartMapper.toPayload(it, owner, liked.contains(it.getId())));
    }

    public AudioPayload find(Long audioId, Long user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        return audioSmartMapper.toPayload(audio, audioLikeService.isLiked(audioId, user));
    }

    @Transactional
    public void update(Long id, AudioPostRequest request, Long user) {
        var audio = audioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio with id " + id + " was not found"));

        updateAccessManager.authorize(user, audio);

        var content = getAudioRequired(request.getContentId());
        var cover = getImageRequired(request.getCoverId());

        requestMapper.mergeEntity(audio, request);

        var savedAudio = audioRepository.save(audio);

        publisher.publish(
                AUDIO.getName(),
                singletonList(toUpdatedEvent(savedAudio))
        );
    }

    public void like(Long audioId, @NotNull Long user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        var likeIdentity = audioMapper.toLikeIdentity(audioId, user);
        if (audioLikeRepository.existsByLikeIdentity(likeIdentity)) {
            throw new BadRequestException("Like is already set");
        }

        var like = audioMapper.toAudioLike(likeIdentity);
        audioLikeRepository.save(like);

        publisher.publish(
                AUDIO.getName(),
                singletonList(toLikeEvent(audio, user))
        );
    }

    public void unlike(Long audioId, @NotNull Long user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        var likeIdentity = audioMapper.toLikeIdentity(audioId, user);
        if (!audioLikeRepository.existsByLikeIdentity(likeIdentity)) {
            throw new BadRequestException("Like is not set");
        }

        var like = audioMapper.toAudioLike(likeIdentity);
        audioLikeRepository.delete(like);

        publisher.publish(AUDIO.getName(),
                singletonList(toUnlikeEvent(audio, user))
        );
    }

    public Page<AudioPayload> findByPlaylist(Long playlistId, Long user, Pageable pageable) {

        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(NotFoundException::new);

        commonVisibilityAccessManager.authorize(user, playlist);

        Page<Audio> audioPage = audioRepository.findAudioFromPlaylistProtected(playlistId, user, pageable);

        var liked = user == null ? new HashSet<Long>() :
            audioLikeService.likedFromBulk(audioPage.stream().map(Audio::getId).collect(Collectors.toSet()), user);

        return audioPage.map(it -> audioSmartMapper.toPayload(it, liked.contains(it.getId())));
    }

    @Transactional
    public void addGenre(Long audioId, Long genreId, Long clientId) {
        var context = audioGenreContext(audioId, genreId);
        updateAccessManager.authorize(clientId, context.audio);

        boolean added = context.audio.getGenres().add(context.genre);
        if (!added) {
            throw new BadRequestException("genre was not added");
        }
    }

    @Transactional
    public void removeGenre(Long audioId, Long genreId, Long clientId) {
        var context = audioGenreContext(audioId, genreId);
        updateAccessManager.authorize(clientId, context.audio);

        boolean removed = context.audio.getGenres().remove(context.genre);

        if (!removed) {
            throw new BadRequestException("genre was not deleted");
        }
    }

    @Transactional
    public void addTag(Long audioId, Long tagId, Long clientId) {
        var context = audioTagContext(audioId, tagId);
        updateAccessManager.authorize(clientId, context.audio);

        boolean added = context.audio.getTags().add(context.tag);
        if (!added) {
            throw new BadRequestException("tag already added");
        }
    }

    @Transactional
    public void removeTag(Long audioId, Long tagId, Long clientId) {
        var context = audioTagContext(audioId, tagId);
        updateAccessManager.authorize(clientId, context.audio);

        boolean removed = context.audio.getTags().remove(context.tag);
        if (!removed) {
            throw new BadRequestException("tag was not present");
        }
    }

    private AudioGenreContext audioGenreContext(Long audioId, Long genreId) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("audio with id " + audioId + " was not found"));

        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("genre with id " + genreId + " was not found"));

        return new AudioGenreContext(audio, genre);
    }

    private AudioTagContext audioTagContext(Long audioId, Long tagId) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("audio with id " + audioId + " was not found"));

        var tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("tag with id " + tagId + " was not found"));

        return new AudioTagContext(audio, tag);
    }

    private Resource getImageRequired(String id) {
        return resourceService.getImage(id)
                .orElseThrow(() -> new BadRequestException("Image with id '" + id + "' was not found"));
    }

    private Resource getAudioRequired(String id) {
        return resourceService.getAudio(id)
                .orElseThrow(() -> new BadRequestException("Audio with id '" + id + "' was not found"));
    }

    private record AudioGenreContext(Audio audio, Genre genre) {
    }

    private record AudioTagContext(Audio audio, Tag tag) {
    }
}
