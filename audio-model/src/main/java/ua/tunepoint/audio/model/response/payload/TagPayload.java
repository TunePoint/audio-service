package ua.tunepoint.audio.model.response.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagPayload {

    private Long id;
    private String name;
}
