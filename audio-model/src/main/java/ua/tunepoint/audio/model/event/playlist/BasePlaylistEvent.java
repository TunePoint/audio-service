package ua.tunepoint.audio.model.event.playlist;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class BasePlaylistEvent implements DomainEvent {

    private Long playlistId;
    private Long playlistOwnerId;
    private LocalDateTime time;
}
