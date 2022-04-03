package ua.tunepoint.audio.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class AudioListeningIdentity implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "audio_id")
    private Long audioId;
}
