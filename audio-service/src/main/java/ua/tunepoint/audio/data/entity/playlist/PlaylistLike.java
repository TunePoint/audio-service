package ua.tunepoint.audio.data.entity.playlist;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "playlists_likes")
public class PlaylistLike {

    @EmbeddedId
    private PlaylistLikeIdentity identity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
