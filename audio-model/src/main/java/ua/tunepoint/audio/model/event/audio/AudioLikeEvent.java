package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AudioLikeEvent extends BaseAudioEvent {

    private Long userId;
}
