package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioPostRequest {

    @NotNull
    @NotBlank
    private String authorName;

    @NotNull
    @Size(min = 3, max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    private String contentId;

    @NotNull
    private String coverId;

    private Boolean isPrivate = false;
}