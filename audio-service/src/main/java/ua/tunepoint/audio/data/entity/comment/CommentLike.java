package ua.tunepoint.audio.data.entity.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments_likes")
public class CommentLike {

    @EmbeddedId
    private CommentLikeIdentity identity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
