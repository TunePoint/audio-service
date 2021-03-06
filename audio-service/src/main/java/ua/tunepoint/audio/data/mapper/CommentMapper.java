package ua.tunepoint.audio.data.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.entity.comment.CommentLike;
import ua.tunepoint.audio.data.entity.comment.CommentLikeIdentity;
import ua.tunepoint.audio.model.request.AudioCommentUpdateRequest;
import ua.tunepoint.audio.model.response.payload.CommentPayload;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mappings({
            @Mapping(target = "likeCount", source = "comment.statistics.likeCount"),
            @Mapping(target = "replyCount", source = "comment.statistics.replyCount"),
            @Mapping(target = "id", source = "comment.id"),
            @Mapping(target = "content", source = "comment.content"),
            @Mapping(target = "createdAt", source = "comment.createdAt"),
            @Mapping(target = "replies", ignore = true),
            @Mapping(target = "isLiked", source = "isLiked")
    })
    CommentPayload toPayload(Comment comment, Boolean isLiked);

    @Mappings({
            @Mapping(target = "isEdited", constant = "true")
    })
    Comment update(@MappingTarget Comment comment, AudioCommentUpdateRequest request);

    @Mappings({
            @Mapping(target = "identity.userId", source = "userId"),
            @Mapping(target = "identity.commentId", source = "commentId")
    })
    CommentLike toLike(Long commentId, Long userId);

    @Mappings({
            @Mapping(target = "commentId", source = "commentId"),
            @Mapping(target = "userId", source = "userId")
    })
    CommentLikeIdentity toLikeIdentity(Long commentId, Long userId);
}
