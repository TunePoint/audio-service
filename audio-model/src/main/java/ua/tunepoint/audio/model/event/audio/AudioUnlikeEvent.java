package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudioUnlikeEvent implements DomainEvent {

    private Long userId;
    private Long audioId;
    private Long authorId;
    private LocalDateTime time;
}
