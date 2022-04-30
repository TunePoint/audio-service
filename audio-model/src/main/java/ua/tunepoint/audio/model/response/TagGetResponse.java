package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.audio.model.response.payload.TagPayload;
import ua.tunepoint.web.model.CommonResponse;

@SuperBuilder
@NoArgsConstructor
public class TagGetResponse extends CommonResponse<TagPayload> {
}
