package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioUpdateRequest {

    @Size(min = 2, max = 128)
    private String authorPseudonym;

    @NotNull
    @Size(min = 2, max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    private String coverId;

    private Set<Long> genreIds;

    @NotNull
    private RequestAudioType type;

    private Boolean isPrivate = false;
}
