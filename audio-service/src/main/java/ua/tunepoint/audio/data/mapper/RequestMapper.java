package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.audio.type.AudioType;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.data.entity.playlist.PlaylistType;
import ua.tunepoint.audio.model.request.AudioCommentPostRequest;
import ua.tunepoint.audio.model.request.AudioPostRequest;
import ua.tunepoint.audio.model.request.AudioUpdateRequest;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;
import ua.tunepoint.audio.model.request.RequestAudioType;
import ua.tunepoint.audio.model.request.RequestPlaylistType;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mappings({
            @Mapping(target = "authorName", source = "request.authorPseudonym"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "isPrivate", source = "request.isPrivate"),
            @Mapping(target = "contentId", source = "request.contentId"),
            @Mapping(target = "coverId", source = "request.coverId"),
            @Mapping(target = "ownerId", source = "userId"),
            @Mapping(target = "genres", source = "genres"),
            @Mapping(target = "type", source = "request.type")
    })
    Audio toEntity(AudioPostRequest request, Set<Genre> genres, Long userId);

    default AudioType toAudioType(RequestAudioType requestType) {
        if (requestType == null) {
            return null;
        }
        return AudioType.withName(requestType.toString());
    }

    @Mappings({
            @Mapping(target = "authorName", source = "authorPseudonym")
    })
    void mergeEntity(@MappingTarget Audio entity, AudioUpdateRequest request);

    @Mappings({
            @Mapping(target = "content", source = "request.content"),
            @Mapping(target = "audioTimestamp", source = "request.audioTimestamp"),
            @Mapping(target = "userId", source = "userId")
    })
    Comment toEntity(AudioCommentPostRequest request, Long userId);

    @Mappings({
            @Mapping(target = "ownerId", source = "userId"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "isPrivate", source = "request.isPrivate"),
            @Mapping(target = "coverId", source = "request.coverId"),
            @Mapping(target = "type", source = "request.type"),
            @Mapping(target = "managerType", source = "manager")
    })
    Playlist toEntity(PlaylistPostRequest request, ManagerType manager, Long userId);

    default PlaylistType mapRequestType(RequestPlaylistType type) {
        return PlaylistType.withName(type.toString());
    }
}
