package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioCommentRequest {

    @NotNull
    @NotBlank
    private String content;

    private Integer audioTimestamp;
}
