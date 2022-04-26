package ua.tunepoint.audio.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import ua.tunepoint.audio.model.request.AudioPostRequest;
import ua.tunepoint.audio.model.response.AudioGetResponse;
import ua.tunepoint.audio.model.response.AudioListGetResponse;
import ua.tunepoint.audio.model.response.AudioPostResponse;
import ua.tunepoint.audio.model.response.AudioUpdateResponse;
import ua.tunepoint.audio.model.response.payload.AudioPayload;
import ua.tunepoint.audio.service.AudioService;
import ua.tunepoint.audio.service.ListeningService;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.exception.BadRequestException;
import ua.tunepoint.web.model.StatusResponse;

import static ua.tunepoint.audio.utils.UserUtils.extractId;

@RestController
@RequestMapping("/audio")
@RequiredArgsConstructor
public class AudioController {

    private final AudioService audioService;
    private final ListeningService listeningService;

    @GetMapping("/{id}")
    public ResponseEntity<AudioGetResponse> getAudio(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        var payload = audioService.find(id, extractId(user));
        var response = AudioGetResponse.builder().payload(payload).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/listenings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> recordListening(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        listeningService.recordListening(id, extractId(user));

        return ResponseEntity.ok(
                StatusResponse.builder().build()
        );
    }

    @GetMapping(params = {"searchBy", "id"})
    public ResponseEntity<AudioListGetResponse> getUserAudio(
            @RequestParam("searchBy") String searchBy,
            @RequestParam("id") Long id,
            @PageableDefault(sort = "uploadedTime", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal user) {

        Page<AudioPayload> payload = switch (searchBy) {
            case "user" -> audioService.findByOwner(id, extractId(user), pageable);
            case "playlist" -> audioService.findByPlaylist(id, extractId(user), pageable);
            default -> throw new BadRequestException("Unknown value of parameter 'searchBy'");
        };

        var response = AudioListGetResponse.builder().payload(payload).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AudioPostResponse> postAudio(@RequestBody AudioPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = audioService.save(request, extractId(user));
        var response = AudioPostResponse.builder().payload(payload).build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AudioUpdateResponse> updateAudio(@PathVariable Long id, @RequestBody AudioPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = audioService.update(id, request, extractId(user));
        var response = AudioUpdateResponse.builder().payload(payload).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> like(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        audioService.like(id, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> unlike(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        audioService.unlike(id, extractId(user));
        return ResponseEntity.ok(StatusResponse.builder().build());
    }
}
