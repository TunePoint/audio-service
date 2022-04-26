package ua.tunepoint.audio.data.entity.audio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "audio_listening")
public class AudioListening {

    @EmbeddedId
    private AudioListeningId listeningIdentity;

    @Column(name = "last_listening_time")
    private LocalDateTime lastListeningTime;

    @Column(name = "listening_count")
    private Integer listeningCount;

    public void recordListening(LocalDateTime when) {
        setListeningCount(this.listeningCount + 1);
        setLastListeningTime(when);
    }

    public static AudioListening create(AudioListeningId id, LocalDateTime when) {
        var listening = new AudioListening();
        listening.setListeningIdentity(id);
        listening.setListeningCount(1);
        listening.setLastListeningTime(when);

        return listening;
    }
}
