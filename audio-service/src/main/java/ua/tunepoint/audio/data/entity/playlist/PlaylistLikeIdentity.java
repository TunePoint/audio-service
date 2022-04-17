package ua.tunepoint.audio.data.entity.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistLikeIdentity implements Serializable {

    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "user_id")
    private Long userId;
}
