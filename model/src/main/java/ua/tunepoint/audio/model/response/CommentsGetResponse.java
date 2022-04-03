package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import ua.tunepoint.audio.model.response.payload.CommentPayload;
import ua.tunepoint.web.model.CommonResponse;

@SuperBuilder
@NoArgsConstructor
public class CommentsGetResponse extends CommonResponse<Page<CommentPayload>> {
}
