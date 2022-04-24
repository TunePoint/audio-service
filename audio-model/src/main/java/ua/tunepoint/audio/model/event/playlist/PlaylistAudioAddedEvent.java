package ua.tunepoint.audio.model.event.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlaylistAudioAddedEvent extends BasePlaylistEvent {

    private Long audioId;
    private Long audioOwnerId;
    private Long userId;
}
