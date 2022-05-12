package ua.tunepoint.audio.api;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tunepoint.audio.data.entity.playlist.ManagerType;
import ua.tunepoint.audio.model.request.PlaylistPostRequest;
import ua.tunepoint.audio.model.request.PlaylistUpdateRequest;
import ua.tunepoint.audio.model.response.PlaylistBulkResponse;
import ua.tunepoint.audio.model.response.PlaylistGetResponse;
import ua.tunepoint.audio.model.response.PlaylistPageResponse;
import ua.tunepoint.audio.model.response.UserPageResponse;
import ua.tunepoint.audio.service.DomainUserService;
import ua.tunepoint.audio.service.PlaylistService;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.model.IdResponse;
import ua.tunepoint.web.model.StatusResponse;

import java.util.List;

import static ua.tunepoint.audio.utils.UserUtils.extractId;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final DomainUserService domainUserService;

    @GetMapping("/_bulk")
    public ResponseEntity<PlaylistBulkResponse> searchBulk(@RequestParam("ids") List<Long> ids, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                PlaylistBulkResponse.builder()
                        .payload(playlistService.searchBulk(ids, extractId(user)))
                        .build()
        );
    }

    @GetMapping("/_users")
    public ResponseEntity<PlaylistPageResponse> playlistsOfUser(@RequestParam("id") Long userId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                PlaylistPageResponse.builder()
                        .payload(
                                playlistService.findByOwner(userId, pageable, user)
                        )
                        .build()
        );
    }

    @GetMapping("/_audio")
    public ResponseEntity<PlaylistPageResponse> playlistsContainingAudio(@RequestParam("id") Long audioId, @PageableDefault Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                PlaylistPageResponse.builder()
                        .payload(playlistService.findByContainingAudio(audioId, extractId(user), pageable))
                        .build()
        );
    }

    @GetMapping("/_likes")
    public ResponseEntity<PlaylistPageResponse> playlistLikedByUser(@RequestParam("id") Long userId, @PageableDefault Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                PlaylistPageResponse.builder()
                        .payload(playlistService.findByUserLiked(userId, pageable, extractId(user)))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IdResponse> postPlaylist(@RequestBody @Validated PlaylistPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var id = playlistService.create(request, ManagerType.USER, extractId(user));
        return ResponseEntity.ok(IdResponse.withId(id));
    }

    @DeleteMapping("/{playlistId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> deletePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.delete(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PutMapping("/{playlistId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> updatePlaylist(@PathVariable Long playlistId, @RequestBody PlaylistUpdateRequest request, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.update(playlistId, request, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistGetResponse> getPlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        var payload = playlistService.findById(playlistId, extractId(user));
        var response = PlaylistGetResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{playlistId}/likes/users")
    public ResponseEntity<UserPageResponse> getUsersLikedPlaylist(@PathVariable Long playlistId, @PageableDefault Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.authorizeAccess(playlistId, extractId(user));
        return ResponseEntity.ok(
                UserPageResponse.builder().payload(domainUserService.findUsersLikedPlaylist(playlistId, pageable))
                        .build()
        );
    }

    @PostMapping("/{playlistId}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> likePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.like(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> unlikePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.unlike(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/audio")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addAudioToPlaylist(@PathVariable Long playlistId, @RequestParam(name = "audioId") Long audioId, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.addAudio(playlistId, audioId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/audio")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeAudioFromPlaylist(@PathVariable Long playlistId, @RequestParam(name = "audioId") Long audioId, @AuthenticationPrincipal UserPrincipal user) {
        playlistService.removeAudio(playlistId, audioId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/tags")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addTag(
            @PathVariable Long playlistId,
            @RequestParam("tagId") Long tagId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        playlistService.addTag(playlistId, tagId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/tags")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeTag(
            @PathVariable Long playlistId,
            @RequestParam("tagId") Long tagId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        playlistService.removeTag(playlistId, tagId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/genres")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addGenre(
            @PathVariable Long playlistId,
            @RequestParam("genreId") Long genreId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        playlistService.addGenre(playlistId, genreId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/genres")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeGenre(
            @PathVariable Long playlistId,
            @RequestParam("genreId") Long genreId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        playlistService.removeGenre(playlistId, genreId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }
}
