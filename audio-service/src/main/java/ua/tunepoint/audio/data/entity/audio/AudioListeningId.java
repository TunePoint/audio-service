package ua.tunepoint.audio.data.entity.audio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AudioListeningId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "audio_id")
    private Long audioId;
}
