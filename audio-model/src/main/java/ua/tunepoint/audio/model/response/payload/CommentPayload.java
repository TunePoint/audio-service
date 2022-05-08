package ua.tunepoint.audio.model.response.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.tunepoint.audio.model.response.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPayload {

    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isLiked;

    private List<CommentPayload> replies;
    private Long likeCount;
    private Long replyCount;

    private User user;
}
