package ua.tunepoint.audio.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.mapper.AudioMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioLikeRepository;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.GenreRepository;
import ua.tunepoint.audio.data.repository.PlaylistRepository;
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

import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.audio.utils.EventUtils.toCreatedEvent;
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

    private final ResourceService resourceService;
    private final UserService userService;

    private final RequestMapper requestMapper;
    private final AudioMapper audioMapper;
    private final AudioSmartMapper audioSmartMapper;

    private final AudioVisibilityAccessManager visibilityAccessManager;
    private final AudioUpdateAccessManager updateAccessManager;
    private final CommonVisibilityAccessManager commonVisibilityAccessManager;

    private final EventPublisher publisher;

    // 1. resource-service call for audio
    // 2. resource-service call for image
    // 3. request-model mapping
    // 4. save to db
    // 5. account-service call
    // 6. model-payload mapping
    // 7. publishing audio.created event
    // the method does a lot of work. should think about how it can be split
    @Transactional
    public AudioPayload save(AudioPostRequest request, @NotNull Long user) {

        var content = getAudioRequired(request.getContentId());
        var cover = getImageRequired(request.getCoverId());

        var genres = request.getGenreIds() == null ? null :
                request.getGenreIds().stream()
                        .map(it -> genreRepository.findById(it)
                                .orElseThrow(
                                        () -> new NotFoundException("genre with id " + it + " was not found")
                                )
                        ).collect(Collectors.toSet());

        var audio = requestMapper.toEntity(request, genres, user);
        var savedAudio = audioRepository.save(audio);

        var payload = audioSmartMapper.toPayload(savedAudio, content, cover);

        publisher.publish(AUDIO.getName(),
                singletonList(EventUtils.toCreatedEvent(audio, user))
        );
        return payload;
    }

    public Page<AudioPayload> findByOwner(@NotNull Long ownerId, @Nullable Long currentUser, Pageable pageable) {
        var owner = userService.findUser(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " was not found"));

        Page<Audio> page = ownerId.equals(currentUser) ? audioRepository.findAudioByOwnerId(ownerId, pageable)
                : audioRepository.findAudioByOwnerIdAndIsPrivateFalse(ownerId, pageable);

        return page.map(it -> audioSmartMapper.toPayload(it, owner));
    }

    public AudioPayload find(Long audioId, Long user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        return audioSmartMapper.toPayload(audio);
    }

    @Transactional
    public AudioPayload update(Long id, AudioPostRequest request, Long user) {
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

        return audioSmartMapper.toPayload(savedAudio, content, cover);
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

        return audioPage.map(audioSmartMapper::toPayload);
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

    private AudioGenreContext audioGenreContext(Long audioId, Long genreId) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("audio with id " + audioId + " was not found"));

        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("genre with id " + genreId + " was not found"));

        return new AudioGenreContext(audio, genre);
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
}
