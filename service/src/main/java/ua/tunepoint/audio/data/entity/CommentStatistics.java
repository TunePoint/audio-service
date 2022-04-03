package ua.tunepoint.audio.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "comments_stats")
public class CommentStatistics {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "reply_count")
    private Long replyCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Comment comment;
}
