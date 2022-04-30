package ua.tunepoint.audio.model.response.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenrePayload {

    private Long id;
    private Long parentId;
    private String name;
}
