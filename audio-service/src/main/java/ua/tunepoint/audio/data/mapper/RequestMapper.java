package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.model.request.AudioCommentPostRequest;
import ua.tunepoint.audio.model.request.AudioPostRequest;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mappings({
            @Mapping(target = "authorName", source = "request.authorName"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "isPrivate", source = "request.isPrivate"),
            @Mapping(target = "contentId", source = "request.contentId"),
            @Mapping(target = "coverId", source = "request.coverId"),
            @Mapping(target = "authorId", source = "userId")
    })
    Audio toEntity(AudioPostRequest request, Long userId);

    void mergeEntity(@MappingTarget Audio entity, AudioPostRequest request);

    @Mappings({
            @Mapping(target = "content", source = "request.content"),
            @Mapping(target = "audioTimestamp", source = "request.audioTimestamp"),
            @Mapping(target = "userId", source = "userId")
    })
    Comment toEntity(AudioCommentPostRequest request, Long userId);

    @Mappings({
            @Mapping(target = "authorId", source = "userId"),
            @Mapping(target = "title", source = "request.title"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "isPrivate", source = "request.isPrivate"),
            @Mapping(target = "coverId", source = "request.coverId")
    })
    Playlist toEntity(PlaylistPostRequest request, Long userId);
}
