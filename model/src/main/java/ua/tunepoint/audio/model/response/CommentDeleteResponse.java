package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.audio.model.response.payload.CommentPayload;
import ua.tunepoint.web.model.CommonResponse;

@SuperBuilder
@NoArgsConstructor
public class CommentDeleteResponse extends CommonResponse<CommentPayload> {
}
