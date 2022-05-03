package ua.tunepoint.audio.model.response.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.audio.model.response.domain.Resource;
import ua.tunepoint.audio.model.response.domain.User;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class AudioPayload {

    private Long id;
    private String authorPseudonym;
    private String title;
    private String description;
    private Integer durationSec;
    private Boolean isPrivate;
    private String type;
    private Long likeCount = 0L;
    private Long listeningCount = 0L;
    private Long commentCount = 0L;
    private LocalDateTime uploadedTime;
    private Resource cover;
    private Resource content;
    private User owner;

    private Set<GenrePayload> genres;
    private Set<TagPayload> tags;
}
