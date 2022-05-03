package ua.tunepoint.audio.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.tunepoint.audio.model.response.PlaylistBulkResponse;
import ua.tunepoint.audio.model.response.PlaylistGetResponse;

import java.util.List;

public interface PlaylistEndpoint {

    @GetMapping("/playlists/_bulk")
    ResponseEntity<PlaylistBulkResponse> searchBulk(@RequestParam("ids") List<Long> ids);

    @GetMapping("/playlists/{id}")
    ResponseEntity<PlaylistGetResponse> searchPlaylist(@PathVariable("id") Long id);
}
