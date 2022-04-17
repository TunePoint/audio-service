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
public class AudioCommentUnlikeEvent implements DomainEvent {

    private Long audioId;
    private Long audioOwnerId;

    private Long commentId;
    private Long commentAuthorId;

    private Long userId;
    private LocalDateTime time;
}
