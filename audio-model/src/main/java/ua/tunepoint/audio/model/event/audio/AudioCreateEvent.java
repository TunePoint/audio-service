package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AudioCreateEvent implements DomainEvent {

    private Long userId;
    private Long audioId;
    private LocalDateTime time;
}
