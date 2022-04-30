package ua.tunepoint.audio.data.entity.playlist;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.tunepoint.audio.data.entity.Genre;
import ua.tunepoint.audio.data.entity.PlaylistAccessibleEntity;
import ua.tunepoint.audio.data.entity.Tag;
import ua.tunepoint.audio.data.entity.playlist.converter.ManagerTypeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Data
@Entity
@Table(name = "playlists")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Playlist implements PlaylistAccessibleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "is_private")
    private Boolean isPrivate = false;

    @Column(name = "manager_type")
    @Convert(converter = ManagerTypeConverter.class)
    private ManagerType managerType;

    @Column(name = "cover_id")
    private String coverId;

    @OneToMany
    @JoinColumn(name = "playlist_id")
    private Set<PlaylistAudio> audioSet;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlists_genres",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlists_tags",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "playlist")
    private PlaylistStatistics statistics;
}
