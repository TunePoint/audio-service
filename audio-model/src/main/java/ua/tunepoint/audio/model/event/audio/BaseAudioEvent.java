package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseAudioEvent implements DomainEvent {

    private Long audioId;
    private Long audioOwnerId;
    private String type;
    private LocalDateTime time;
}
