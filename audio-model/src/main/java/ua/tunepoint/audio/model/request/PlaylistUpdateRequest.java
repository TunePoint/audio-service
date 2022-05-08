package ua.tunepoint.audio.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PlaylistUpdateRequest {

    @NotNull
    private String title;
    private String description;
    private Boolean isPrivate;
    @NotNull
    private RequestPlaylistType type;
    private String coverId;
}
