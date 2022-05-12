package ua.tunepoint.audio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.tunepoint.account.model.response.payload.UserPublicPayload;
import ua.tunepoint.audio.service.client.UserClient;
import ua.tunepoint.web.exception.ServerException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DomainUserService {

    private final UserClient userClient;
    private final AudioLikeService audioLikeService;
    private final PlaylistLikeService playlistLikeService;

    public Page<UserPublicPayload> findUsersLikedAudio(Long audioId, Pageable pageable) {
        var likes = audioLikeService.getRecentLikes(audioId, pageable);
        if (likes.isEmpty()) {
            return Page.empty(pageable);
        }

        return fetchUserPage(likes.stream().map(it -> it.getLikeIdentity().getUserId()).collect(Collectors.toList()), pageable,
                likes.getTotalElements());
    }

    public Page<UserPublicPayload> findUsersLikedPlaylist(Long playlistId, Pageable pageable) {
        var likes = playlistLikeService.getRecentLikes(playlistId, pageable);
        if (likes.isEmpty()) {
            return Page.empty(pageable);
        }
        return fetchUserPage(likes.stream().map(it -> it.getIdentity().getUserId()).collect(Collectors.toList()), pageable,
                likes.getTotalElements());
    }

    private Page<UserPublicPayload> fetchUserPage(List<Long> ids, Pageable pageable, Long total) {
        var userSearchResponse = userClient.searchBulk(ids);
        if (userSearchResponse.getStatusCode().value() != HttpStatus.OK.value() || userSearchResponse.getBody() == null) {
            throw new ServerException();
        }
        var userBulk = userSearchResponse.getBody().getPayload();
        return new PageImpl<>(userBulk, pageable, total);
    }
}
