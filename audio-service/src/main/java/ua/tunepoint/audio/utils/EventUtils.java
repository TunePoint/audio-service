package ua.tunepoint.audio.utils;

import lombok.experimental.UtilityClass;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.model.event.audio.AudioCreateEvent;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentCreateEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentDeleteEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentLikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentReplyEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentUnlikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentUpdateEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistCreateEvent;

import java.time.LocalDateTime;

@UtilityClass
public class EventUtils {

    public static AudioCreateEvent toCreateEvent(Audio audio, Long userId) {
        return AudioCreateEvent.builder()
                .audioId(audio.getId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioLikeEvent toLikeEvent(Audio audio, Long userId) {
        return AudioLikeEvent.builder()
                .ownerId(audio.getOwnerId())
                .audioId(audio.getId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioUnlikeEvent toUnlikeEvent(Audio audio, Long userId) {
        return AudioUnlikeEvent.builder()
                .ownerId(audio.getOwnerId())
                .audioId(audio.getId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentCreateEvent toCreateEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentCreateEvent.builder()
                .commentId(comment.getId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentLikeEvent toLikeEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentLikeEvent.builder()
                .commentId(comment.getId())
                .commentAuthorId(comment.getUserId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentUnlikeEvent toUnlikeEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentUnlikeEvent.builder()
                .commentId(comment.getId())
                .commentAuthorId(comment.getUserId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentReplyEvent toReplyEvent(Comment comment, Comment reply, Audio audio, Long userId) {
        return AudioCommentReplyEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .commentId(comment.getId())
                .commentAuthorId(comment.getUserId())
                .replyId(reply.getId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentDeleteEvent toDeleteEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentDeleteEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .commentId(comment.getId())
                .commentAuthorId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentUpdateEvent toUpdateEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentUpdateEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .commentId(comment.getId())
                .commentAuthorId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static PlaylistCreateEvent toCreateEvent(Playlist playlist) {
        return PlaylistCreateEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .time(LocalDateTime.now())
                .build();
    }
}
