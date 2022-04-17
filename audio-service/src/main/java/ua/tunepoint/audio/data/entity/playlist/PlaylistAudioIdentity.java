package ua.tunepoint.audio.data.entity.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PlaylistAudioIdentity implements Serializable {

    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "audio_id")
    private Long audioId;
}
