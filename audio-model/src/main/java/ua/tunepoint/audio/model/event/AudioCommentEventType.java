package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import ua.tunepoint.audio.model.event.comment.AudioCommentCreateEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentLikeEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentReplyEvent;
import ua.tunepoint.audio.model.event.comment.AudioCommentUnlikeEvent;
import ua.tunepoint.event.model.DomainEventType;

@AllArgsConstructor
public enum AudioCommentEventType implements DomainEventType {

    AUDIO_COMMENT_LIKE("like", AudioCommentLikeEvent.class),
    AUDIO_COMMENT_UNLIKE("unlike", AudioCommentUnlikeEvent.class),
    AUDIO_COMMENT_REPLY("reply", AudioCommentReplyEvent.class),
    AUDIO_COMMENT_CREATE("create", AudioCommentCreateEvent.class);

    private final String name;
    private final Class<?> type;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
