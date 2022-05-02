package ua.tunepoint.audio.model.event.audio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AudioListenEvent extends BaseAudioEvent {

    private Long userId;
    private Long playlistId;
}
