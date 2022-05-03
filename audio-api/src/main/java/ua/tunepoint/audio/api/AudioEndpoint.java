package ua.tunepoint.audio.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.tunepoint.audio.model.response.AudioBulkResponse;
import ua.tunepoint.audio.model.response.AudioGetResponse;

import java.util.List;

public interface AudioEndpoint {

    @GetMapping("/audio/_bulk")
    ResponseEntity<AudioBulkResponse> searchBulk(@RequestParam("ids") List<Long> ids);

    @GetMapping("/audio/{id}")
    ResponseEntity<AudioGetResponse> searchAudio(@PathVariable("id") Long id);
}
