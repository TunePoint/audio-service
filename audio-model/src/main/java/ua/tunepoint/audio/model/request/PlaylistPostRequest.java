package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistPostRequest {

    @NotNull
    private String title;
    private String description;
    @NotNull
    private RequestPlaylistType type;
    private Boolean isPrivate = false;
    private Set<Long> audioIds;
    private Set<Long> genreIds;
    private String coverId;
}
