package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.Audio;
import ua.tunepoint.audio.data.entity.Comment;
import ua.tunepoint.audio.model.request.AudioCommentRequest;
import ua.tunepoint.audio.model.request.AudioPostRequest;

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
    Comment toEntity(AudioCommentRequest request, Long userId);
}
