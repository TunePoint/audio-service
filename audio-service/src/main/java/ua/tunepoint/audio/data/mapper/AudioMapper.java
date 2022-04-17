package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.audio.AudioLike;
import ua.tunepoint.audio.data.entity.audio.AudioLikeIdentity;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;
import ua.tunepoint.audio.model.response.payload.AudioPayload;

@Mapper(componentModel = "spring")
public interface AudioMapper {

    @Mappings({
            @Mapping(target = "id", source = "audio.id"),
            @Mapping(target = "authorName", source = "audio.authorName"),
            @Mapping(target = "title", source = "audio.title"),
            @Mapping(target = "description", source = "audio.description"),
            @Mapping(target = "durationSec", source = "audio.durationSec"),
            @Mapping(target = "isPrivate", source = "audio.isPrivate"),
            @Mapping(target = "content", source = "content"),
            @Mapping(target = "likeCount", source = "audio.statistics.likeCount"),
            @Mapping(target = "listeningCount", source = "audio.statistics.listeningCount"),
            @Mapping(target = "uploadedTime", source = "audio.uploadedTime"),
            @Mapping(target = "cover", source = "cover"),
            @Mapping(target = "author", source = "author")
    })
    AudioPayload toPayload(Audio audio, Resource content, Resource cover, User author);

    @Mappings({
            @Mapping(target = "audioId", source = "audioId"),
            @Mapping(target = "userId", source = "userId"),
    })
    AudioLikeIdentity toLikeIdentity(Long audioId, Long userId);

    @Mappings({
            @Mapping(target = "likeIdentity", source = "likeIdentity")
    })
    AudioLike toAudioLike(AudioLikeIdentity likeIdentity);
}
