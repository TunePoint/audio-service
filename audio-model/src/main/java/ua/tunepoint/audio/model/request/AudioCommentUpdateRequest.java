package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioCommentUpdateRequest {

    @NotNull
    private String content;
}
