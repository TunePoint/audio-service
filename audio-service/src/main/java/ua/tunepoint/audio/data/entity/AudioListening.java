package ua.tunepoint.audio.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private AudioListeningIdentity  listeningIdentity;

    private LocalDateTime listeningTime;
}
