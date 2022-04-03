package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.web.model.CommonResponse;

@SuperBuilder
@NoArgsConstructor
public class AudioListGetResponse extends CommonResponse<Page<AudioPayload>> {
}
