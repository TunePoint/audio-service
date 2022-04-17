package ua.tunepoint.audio.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PlaylistPostRequest {

    private String title;
    private String description;
    private Boolean isPrivate = false;
    private Set<Long> audioIds;
    private String coverId;
}
