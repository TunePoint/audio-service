package ua.tunepoint.audio.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCreateRequest {

    @Size(min = 3, max = 64)
    private String name;
}
