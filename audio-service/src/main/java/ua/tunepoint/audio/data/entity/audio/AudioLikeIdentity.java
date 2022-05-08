package ua.tunepoint.audio.data.entity.audio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class AudioLikeIdentity implements Serializable {

    @Column(name = "audio_id")
    private Long audioId;

    @Column(name = "user_id")
    private Long userId;
}
