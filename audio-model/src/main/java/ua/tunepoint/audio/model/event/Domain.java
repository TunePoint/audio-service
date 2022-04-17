package ua.tunepoint.audio.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Domain {

    AUDIO("audio"),
    AUDIO_COMMENT("audio_comment"),
    PLAYLIST("playlist");

    @Getter
    private String name;
}