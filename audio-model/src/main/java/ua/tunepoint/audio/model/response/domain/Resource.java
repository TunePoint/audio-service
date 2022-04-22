package ua.tunepoint.audio.model.response.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
public class Resource {

    private String id;
    private String url;
    private LocalDateTime created;
    private String originalName;
}
