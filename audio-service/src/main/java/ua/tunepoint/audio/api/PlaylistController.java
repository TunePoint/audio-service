package ua.tunepoint.audio.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import ua.tunepoint.audio.model.response.PlaylistPostResponse;
import ua.tunepoint.audio.model.response.PlaylistUpdateResponse;
import ua.tunepoint.audio.model.response.payload.PlaylistPayload;
import ua.tunepoint.audio.service.PlaylistService;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.model.StatusResponse;

import java.util.List;

import static ua.tunepoint.audio.utils.UserUtils.extractId;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/playlists")
public class PlaylistController {

    private final PlaylistService service;

    @GetMapping("/_bulk")
    public ResponseEntity<PlaylistBulkResponse> searchBulk(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(
                PlaylistBulkResponse.builder()
                        .payload(service.searchBulk(ids))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistPostResponse> postPlaylist(@RequestBody PlaylistPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = service.create(request, ManagerType.USER, extractId(user));
        var response = PlaylistPostResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{playlistId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> deletePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        service.delete(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PutMapping("/{playlistId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlaylistUpdateResponse> updatePlaylist(@PathVariable Long playlistId, @RequestBody PlaylistUpdateRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = service.update(playlistId, request, extractId(user));
        var response = PlaylistUpdateResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistGetResponse> getPlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        var payload = service.findById(playlistId, extractId(user));
        var response = PlaylistGetResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{playlistId}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> likePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        service.like(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> unlikePlaylist(@PathVariable Long playlistId, @AuthenticationPrincipal UserPrincipal user) {
        service.unlike(playlistId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/audio")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addAudioToPlaylist(@PathVariable Long playlistId, @RequestParam(name = "audioId") Long audioId, @AuthenticationPrincipal UserPrincipal user) {
        service.addAudio(playlistId, audioId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/audio")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeAudioFromPlaylist(@PathVariable Long playlistId, @RequestParam(name = "audioId") Long audioId, @AuthenticationPrincipal UserPrincipal user) {
        service.removeAudio(playlistId, audioId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/tags")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addTag(
            @PathVariable Long playlistId,
            @RequestParam("tagId") Long tagId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        service.addTag(playlistId, tagId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/tags")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeTag(
            @PathVariable Long playlistId,
            @RequestParam("tagId") Long tagId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        service.removeTag(playlistId, tagId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{playlistId}/genres")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> addGenre(
            @PathVariable Long playlistId,
            @RequestParam("genreId") Long genreId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        service.addGenre(playlistId, genreId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{playlistId}/genres")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> removeGenre(
            @PathVariable Long playlistId,
            @RequestParam("genreId") Long genreId,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        service.removeGenre(playlistId, genreId, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }
}
