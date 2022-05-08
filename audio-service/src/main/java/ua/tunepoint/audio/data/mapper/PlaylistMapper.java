package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudio;
import ua.tunepoint.audio.data.entity.playlist.PlaylistAudioIdentity;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLike;
import ua.tunepoint.audio.data.entity.playlist.PlaylistLikeIdentity;
import ua.tunepoint.audio.data.entity.playlist.PlaylistType;
import ua.tunepoint.audio.model.request.PlaylistUpdateRequest;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.model.response.payload.PlaylistPayload;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mappings({
            @Mapping(target = "id", source = "playlist.id"),
            @Mapping(target = "title", source = "playlist.title"),
            @Mapping(target = "description", source = "playlist.description"),
            @Mapping(target = "isPrivate", source = "playlist.isPrivate"),
            @Mapping(target = "cover", source = "cover"),
            @Mapping(target = "owner", source = "owner"),
            @Mapping(target = "likeCount", source = "playlist.statistics.likeCount"),
            @Mapping(target = "audioCount", source = "playlist.statistics.audioCount"),
            @Mapping(target = "genres", source = "playlist.genres"),
            @Mapping(target = "tags", source = "playlist.tags"),
            @Mapping(target = "type", source = "playlist.type"),
            @Mapping(target = "createdAt", source = "playlist.createdAt"),
            @Mapping(target = "isLiked", source = "isLiked")
    })
    PlaylistPayload toPayload(Playlist playlist, Resource cover, User owner, Boolean isLiked);

    Playlist merge(@MappingTarget Playlist playlist, PlaylistUpdateRequest request);

    @Mappings({
            @Mapping(target = "id", source = "id")
    })
    PlaylistAudio toPlaylistAudio(PlaylistAudioIdentity id);

    @Mappings({
            @Mapping(target = "id.playlistId", source = "playlistId"),
            @Mapping(target = "id.audioId", source = "audioId"),
    })
    PlaylistAudio toPlaylistAudio(Long playlistId, Long audioId);

    @Mappings({
            @Mapping(target = "identity", source = "id")
    })
    PlaylistLike toLike(PlaylistLikeIdentity id);

    default String mapPlaylistType(PlaylistType type) {
        return type == null ? null : type.getName();
    }
}
