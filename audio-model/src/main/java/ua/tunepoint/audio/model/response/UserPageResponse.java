package ua.tunepoint.audio.model.response;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;
import ua.tunepoint.account.model.response.payload.UserPublicPayload;
import ua.tunepoint.web.model.CommonResponse;

@SuperBuilder
@NoArgsConstructor
public class UserPageResponse extends CommonResponse<Page<UserPublicPayload>> {
}
