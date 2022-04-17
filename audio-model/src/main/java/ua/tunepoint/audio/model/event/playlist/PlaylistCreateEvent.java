package ua.tunepoint.audio.model.event.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PlaylistCreateEvent implements DomainEvent {

    private Long playlistId;
    private Long playlistOwnerId;

    private LocalDateTime time;
}
