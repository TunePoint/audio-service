package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.audio.model.response.payload.TagPayload;
import ua.tunepoint.web.model.CommonResponse;

import java.util.Set;

@SuperBuilder
@NoArgsConstructor
public class TagSearchResponse extends CommonResponse<Set<TagPayload>> {
}
