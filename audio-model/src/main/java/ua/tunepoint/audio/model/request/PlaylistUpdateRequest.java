package ua.tunepoint.audio.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaylistUpdateRequest {

    private String title;
    private String description;
    private Boolean isPrivate;
    private String coverId;
}
