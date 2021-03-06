package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistPostRequest {

    @NotNull
    @Size(max = 128)
    private String title;

    @Size(max = 256)
    private String description;

    @NotNull
    private RequestPlaylistType type;

    private Boolean isPrivate = false;
    private Set<Long> audioIds;
    private Set<Long> genreIds;
    private String coverId;
}
