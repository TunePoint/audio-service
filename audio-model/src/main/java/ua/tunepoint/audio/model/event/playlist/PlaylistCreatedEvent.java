package ua.tunepoint.audio.model.event.playlist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.event.model.DomainEvent;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlaylistCreatedEvent extends BasePlaylistEvent {

    private String title;
    private String description;
    private String managerType;
    private Boolean isPrivate;
    private String type;
}
