package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.web.model.CommonResponse;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
public class AudioBulkResponse extends CommonResponse<List<AudioPayload>> {
}
