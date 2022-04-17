package ua.tunepoint.audio.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.mapper.AudioMapper;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.audio.service.ResourceService;
import ua.tunepoint.audio.service.UserService;

@Component
@RequiredArgsConstructor
public class AudioSmartMapper {

    private final AudioMapper audioMapper;
    private final ResourceService resourceService;
    private final UserService userService;

    public AudioPayload toPayload(Audio audio) {

        var content = resourceService.getAudio(audio.getContentId())
                .orElse(null);

        var cover = resourceService.getImage(audio.getCoverId())
                .orElse(null);

        var user = userService.findUser(audio.getAuthorId())
                .orElse(null);

        return audioMapper.toPayload(audio, content, cover, user);
    }

    public AudioPayload toPayload(Audio audio, Resource content, Resource cover) {

        var user = userService.findUser(audio.getAuthorId())
                .orElse(null);

        return audioMapper.toPayload(audio, content, cover, user);
    }

    public AudioPayload toPayload(Audio audio, User user) {

        var content = resourceService.getAudio(audio.getContentId())
                .orElse(null);

        var cover = resourceService.getImage(audio.getCoverId())
                .orElse(null);

        return audioMapper.toPayload(audio, content, cover, user);
    }
}
