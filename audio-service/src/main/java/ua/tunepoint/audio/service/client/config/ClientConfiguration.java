package ua.tunepoint.audio.service.client.config;

import org.springframework.context.annotation.Bean;
import ua.tunepoint.security.UserContextRequestInterceptor;

public class ClientConfiguration {

    @Bean
    public UserContextRequestInterceptor requestInterceptor() {
        return new UserContextRequestInterceptor();
    }
}
