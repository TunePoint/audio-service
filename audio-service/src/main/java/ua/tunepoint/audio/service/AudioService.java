package ua.tunepoint.audio.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.mapper.AudioMapper;
import ua.tunepoint.audio.data.mapper.RequestMapper;
import ua.tunepoint.audio.data.repository.AudioLikeRepository;
import ua.tunepoint.audio.data.repository.AudioRepository;
import ua.tunepoint.audio.data.repository.PlaylistRepository;
import ua.tunepoint.audio.model.request.AudioPostRequest;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.audio.security.CommonVisibilityAccessManager;
import ua.tunepoint.audio.security.audio.AudioUpdateAccessManager;
import ua.tunepoint.audio.security.audio.AudioVisibilityAccessManager;
import ua.tunepoint.audio.service.support.AudioSmartMapper;
import ua.tunepoint.event.starter.publisher.EventPublisher;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.exception.NotFoundException;

import javax.annotation.Nullable;

import static java.util.Collections.singletonList;
import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.audio.utils.EventUtils.toCreateEvent;
import static ua.tunepoint.audio.utils.EventUtils.toLikeEvent;
import static ua.tunepoint.audio.utils.EventUtils.toUnlikeEvent;

@Service
@RequiredArgsConstructor
public class AudioService {

    private final AudioRepository audioRepository;
    private final AudioLikeRepository audioLikeRepository;
    private final PlaylistRepository playlistRepository;
    private final ResourceService resourceService;
    private final UserService userService;

    private final RequestMapper requestMapper;
    private final AudioMapper audioMapper;
    private final AudioSmartMapper audioSmartMapper;

    private final AudioVisibilityAccessManager visibilityAccessManager;
    private final AudioUpdateAccessManager updateAccessManager;
    private final CommonVisibilityAccessManager commonVisibilityAccessManager;

    private final EventPublisher publisher;

    @Transactional
    public AudioPayload save(AudioPostRequest request, @NotNull UserPrincipal user) {

        var content = getAudioRequired(request.getContentId());
        var cover = getImageRequired(request.getCoverId());

        var audio = requestMapper.toEntity(request, user.getId());
        var savedAudio = audioRepository.save(audio);

        var payload = audioSmartMapper.toPayload(savedAudio, content, cover);

        publisher.publish(AUDIO.getName(),
                singletonList(toCreateEvent(audio, user.getId()))
        );
        return payload;
    }

    public Page<AudioPayload> findByUser(@NotNull Long userId, @Nullable UserPrincipal currentUser, Pageable pageable) {
        var owner = userService.findUser(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " was not found" ));

        Page<Audio> page = userId.equals(extractId(currentUser)) ? audioRepository.findAudioByOwnerId(userId, pageable)
                : audioRepository.findAudioByOwnerIdAndIsPrivateFalse(userId, pageable);

        return page.map(it -> audioSmartMapper.toPayload(it, owner));
    }

    public AudioPayload find(Long audioId, UserPrincipal user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        return audioSmartMapper.toPayload(audio);
    }

    public AudioPayload update(Long id, AudioPostRequest request, UserPrincipal user) {
        var audio = audioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audio with id " + id + " was not found"));

        updateAccessManager.authorize(user, audio);

        var content = getAudioRequired(request.getContentId());
        var cover = getImageRequired(request.getCoverId());

        requestMapper.mergeEntity(audio, request);

        var savedAudio = audioRepository.save(audio);

        return audioSmartMapper.toPayload(savedAudio, content, cover);
    }

    public void like(Long audioId, @NotNull UserPrincipal user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        var likeIdentity = audioMapper.toLikeIdentity(audioId, user.getId());
        if (audioLikeRepository.existsByLikeIdentity(likeIdentity)) {
            throw new BadRequestException("Like is already set");
        }

        var like = audioMapper.toAudioLike(likeIdentity);
        audioLikeRepository.save(like);

        publisher.publish(AUDIO.getName(),
                singletonList(toLikeEvent(audio, user.getId()))
        );
    }

    public void unlike(Long audioId, @NotNull UserPrincipal user) {
        var audio = audioRepository.findById(audioId)
                .orElseThrow(() -> new NotFoundException("Audio with id " + audioId + " was not found"));

        visibilityAccessManager.authorize(user, audio);

        var likeIdentity = audioMapper.toLikeIdentity(audioId, user.getId());
        if (!audioLikeRepository.existsByLikeIdentity(likeIdentity)) {
            throw new BadRequestException("Like is not set");
        }

        var like = audioMapper.toAudioLike(likeIdentity);
        audioLikeRepository.delete(like);

        publisher.publish(AUDIO.getName(),
                singletonList(toUnlikeEvent(audio, user.getId()))
        );
    }

    public Page<AudioPayload> findByPlaylist(Long playlistId, UserPrincipal user, Pageable pageable) {

        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(NotFoundException::new);

        commonVisibilityAccessManager.authorize(user, playlist);

        Page<Audio> audioPage = audioRepository.findAudioByPlaylistWithAccess(playlistId, extractId(user), pageable);

        return audioPage.map(audioSmartMapper::toPayload);
    }

    private Resource getImageRequired(String id) {
        return resourceService.getImage(id)
                .orElseThrow(() -> new BadRequestException("Image with id '" + id + "' was not found"));
    }

    private Resource getAudioRequired(String id) {
        return resourceService.getAudio(id)
                .orElseThrow(() -> new BadRequestException("Audio with id '" + id + "' was not found"));
    }

    private Long extractId(@Nullable UserPrincipal user) {
        return user == null ? null : user.getId();
    }
}
