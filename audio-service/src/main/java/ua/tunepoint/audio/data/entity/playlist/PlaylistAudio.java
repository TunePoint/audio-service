package ua.tunepoint.audio.data.entity.playlist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.tunepoint.audio.data.entity.audio.Audio;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "playlists_audio")
public class PlaylistAudio {

    @EmbeddedId
    private PlaylistAudioIdentity id;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
