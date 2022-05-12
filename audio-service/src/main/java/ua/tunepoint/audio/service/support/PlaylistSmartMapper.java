package ua.tunepoint.audio.service.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.data.mapper.PlaylistMapper;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.model.response.payload.PlaylistPayload;
import ua.tunepoint.audio.service.ResourceService;
import ua.tunepoint.audio.service.UserService;
import ua.tunepoint.web.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class PlaylistSmartMapper {

    private final UserService userService;
    private final ResourceService resourceService;
    private final PlaylistMapper playlistMapper;

    public PlaylistPayload toPayload(Playlist playlist, Resource cover, Boolean isLiked) {

        User user = userService.findUser(playlist.getOwnerId())
                .orElseThrow(() -> new NotFoundException("User with id " + playlist.getOwnerId() + " was not found"));

        return playlistMapper.toPayload(playlist, cover, user, isLiked);
    }

    public PlaylistPayload toPayload(Playlist playlist, User user, Boolean isLiked) {

        Resource cover = null;
        if (playlist.getCoverId() != null) {
            cover = resourceService.getImage(playlist.getCoverId())
                    .orElseThrow(() -> new NotFoundException("Image with id " + playlist.getCoverId() + " was not found" ));
        }

        return playlistMapper.toPayload(playlist, cover, user, isLiked);
    }

    public PlaylistPayload toPayload(Playlist playlist, Boolean isLiked) {

        Resource cover = null;
        if (playlist.getCoverId() != null) {
            cover = resourceService.getImage(playlist.getCoverId())
                    .orElseThrow(() -> new NotFoundException("Image with id " + playlist.getCoverId() + " was not found" ));
        }

        User user = userService.findUser(playlist.getOwnerId())
                .orElseThrow(() -> new NotFoundException("User with id " + playlist.getOwnerId() + " was not found"));

        return playlistMapper.toPayload(playlist, cover, user, isLiked);
    }
}
