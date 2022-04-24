package ua.tunepoint.audio.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "playlists")
@PropertySource(value = "classpath:templates.properties")
public class ServicePlaylistProperties {

    private PlaylistProperties favourite;

    @Data
    @NoArgsConstructor
    public static class PlaylistProperties {

        private String title;
        private String description;
    }
}
