package ua.tunepoint.audio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tunepoint.audio.model.event.AudioCommentEventType;
import ua.tunepoint.audio.model.event.AudioEventType;
import ua.tunepoint.audio.model.event.PlaylistEventType;
import ua.tunepoint.event.starter.DomainRelation;
import ua.tunepoint.event.starter.registry.DomainRegistry;
import ua.tunepoint.event.starter.registry.builder.DomainRegistryBuilder;

import java.util.Set;

import static ua.tunepoint.audio.model.event.Domain.AUDIO;
import static ua.tunepoint.audio.model.event.Domain.AUDIO_COMMENT;
import static ua.tunepoint.audio.model.event.Domain.PLAYLIST;

@Configuration
public class EventConfiguration {

    @Bean
    public DomainRegistry domainRegistry() {
        return new DomainRegistryBuilder()
                .register(AUDIO.getName(), AudioEventType.values(), Set.of(DomainRelation.PRODUCER, DomainRelation.CONSUMER))
                .register(AUDIO_COMMENT.getName(), AudioCommentEventType.values(), Set.of(DomainRelation.PRODUCER))
                .register(PLAYLIST.getName(), PlaylistEventType.values(), Set.of(DomainRelation.PRODUCER))
                .build();
    }
}
