package ua.tunepoint.audio.model.response.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;

@Data
@NoArgsConstructor
public class PlaylistPayload {

    private Long id;
    private String title;
    private String description;
    private Boolean isPrivate = false;
    private Long likeCount;
    private Long audioCount;

    private Resource cover;
    private User owner;
}
