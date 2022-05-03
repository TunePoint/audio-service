package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ua.tunepoint.audio.model.response.payload.PlaylistPayload;
import ua.tunepoint.web.model.CommonResponse;

import java.util.List;

@NoArgsConstructor
@SuperBuilder
public class PlaylistBulkResponse extends CommonResponse<List<PlaylistPayload>> {
}
