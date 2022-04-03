package ua.tunepoint.audio.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "audio_stats")
public class AudioStatistics {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "listening_count")
    private Long listeningCount = 0L;

    @Column(name = "comment_count")
    private Long commentCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Audio audio;
}
