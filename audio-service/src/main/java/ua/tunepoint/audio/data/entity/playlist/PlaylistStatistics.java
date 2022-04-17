package ua.tunepoint.audio.data.entity.playlist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "playlists_stats")
public class PlaylistStatistics {

    @Id
    private Long id;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "audio_count")
    private Long audioCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Playlist playlist;
}
