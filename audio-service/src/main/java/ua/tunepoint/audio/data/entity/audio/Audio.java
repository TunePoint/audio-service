package ua.tunepoint.audio.data.entity.audio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.tunepoint.audio.data.entity.AccessibleEntity;
import ua.tunepoint.audio.data.entity.Genre;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "audio")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Audio implements AccessibleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "content_id")
    private String contentId;

    @Column(name = "cover_id")
    private String coverId;

    @Column(name = "description")
    private String description;

    @Column(name = "uploaded_time")
    private LocalDateTime uploadedTime;

    @Column(name = "duration_sec")
    private Integer durationSec;

    @Column(name = "is_private")
    private Boolean isPrivate = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "audio_genres",
            joinColumns = @JoinColumn(name = "audio_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @OneToOne(mappedBy = "audio", fetch = FetchType.EAGER)
    private AudioStatistics statistics;

    @PrePersist
    protected void onCreate() {
        uploadedTime = LocalDateTime.now();
    }
}
