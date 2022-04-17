package ua.tunepoint.audio.model.event.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioCommentReplyEvent implements DomainEvent {

    private Long audioId;
    private Long audioAuthorId;

    private Long commentId;
    private Long commentAuthorId;

    private Long replyId;

    private Long userId;
    private LocalDateTime time;
}
