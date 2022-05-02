package ua.tunepoint.audio.utils;

import lombok.experimental.UtilityClass;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.audio.Audio;
import ua.tunepoint.audio.data.entity.comment.Comment;
import ua.tunepoint.audio.data.entity.playlist.Playlist;
import ua.tunepoint.audio.model.event.audio.AudioCreatedEvent;
import ua.tunepoint.audio.model.event.audio.AudioDeletedEvent;
import ua.tunepoint.audio.model.event.audio.AudioLikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUnlikeEvent;
import ua.tunepoint.audio.model.event.audio.AudioUpdatedEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentCreateEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentDeleteEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentLikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentReplyEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentUnlikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentUpdateEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistAudioAddedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistAudioRemovedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistCreatedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistDeletedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistLikedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistUnlikedEvent;
import ua.tunepoint.audio.model.event.playlist.PlaylistUpdatedEvent;

import java.time.LocalDateTime;

@UtilityClass
public class EventUtils {

    public static AudioCreatedEvent toCreatedEvent(Audio audio, Long userId) {
        return AudioCreatedEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(userId)
                .type(audio.getType().toString())
                .description(audio.getDescription())
                .title(audio.getTitle())
                .authorPseudonym(audio.getAuthorName())
                .isPrivate(audio.isPrivate())
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioLikeEvent toLikeEvent(Audio audio, Long userId) {
        return AudioLikeEvent.builder()
                .audioOwnerId(audio.getOwnerId())
                .audioId(audio.getId())
                .userId(userId)
                .type(audio.getType().toString())
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioUnlikeEvent toUnlikeEvent(Audio audio, Long userId) {
        return AudioUnlikeEvent.builder()
                .audioOwnerId(audio.getOwnerId())
                .audioId(audio.getId())
                .userId(userId)
                .type(audio.getType().toString())
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioCommentCreateEvent toCreatedEvent(Comment comment, Audio audio, Long userId) {
        return AudioCommentCreateEvent.builder()
                .commentId(comment.getId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioDeletedEvent toDeletedEvent(Audio audio) {
        return AudioDeletedEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .time(LocalDateTime.now())
                .build();
    }

    public static AudioUpdatedEvent toUpdatedEvent(Audio audio) {
        return AudioUpdatedEvent.builder()
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .title(audio.getTitle())
                .description(audio.getDescription())
                .coverId(audio.getCoverId())
                .contentId(audio.getContentId())
                .authorPseudonym(audio.getAuthorName())
                .isPrivate(audio.isPrivate())
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

    public static PlaylistCreatedEvent toCreatedEvent(Playlist playlist) {
        return PlaylistCreatedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .time(LocalDateTime.now())
                .title(playlist.getTitle())
                .description(playlist.getDescription())
                .isPrivate(playlist.isPrivate())
                .build();
    }

    public static PlaylistUpdatedEvent toUpdatedEvent(Playlist playlist) {
        return PlaylistUpdatedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .title(playlist.getTitle())
                .description(playlist.getDescription())
                .isPrivate(playlist.isPrivate())
                .time(LocalDateTime.now())
                .build();
    }

    public static PlaylistDeletedEvent toDeletedEvent(PlaylistAccessibleEntity playlist) {
        return PlaylistDeletedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .time(LocalDateTime.now())
                .build();
    }

    public static PlaylistLikedEvent toLikedEvent(PlaylistAccessibleEntity playlist, Long userId) {
        return PlaylistLikedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .time(LocalDateTime.now())
                .userId(userId)
                .build();
    }

    public static PlaylistUnlikedEvent toUnlikedEvent(PlaylistAccessibleEntity playlist, Long userId) {
        return PlaylistUnlikedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .time(LocalDateTime.now())
                .userId(userId)
                .build();
    }

    public static PlaylistAudioAddedEvent toAddedEvent(PlaylistAccessibleEntity playlist, AccessibleEntity audio, Long userId) {
        return PlaylistAudioAddedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }

    public static PlaylistAudioRemovedEvent toRemovedEvent(PlaylistAccessibleEntity playlist, AccessibleEntity audio, Long userId) {
        return PlaylistAudioRemovedEvent.builder()
                .playlistId(playlist.getId())
                .playlistOwnerId(playlist.getOwnerId())
                .audioId(audio.getId())
                .audioOwnerId(audio.getOwnerId())
                .userId(userId)
                .time(LocalDateTime.now())
                .build();
    }
}
