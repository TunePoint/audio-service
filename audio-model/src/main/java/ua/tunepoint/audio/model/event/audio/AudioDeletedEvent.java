package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.event.model.DomainEvent;

@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AudioDeletedEvent extends BaseAudioEvent {

}
