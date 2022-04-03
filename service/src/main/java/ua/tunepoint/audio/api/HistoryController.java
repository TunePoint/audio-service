package ua.tunepoint.audio.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/history")
public class HistoryController {

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> getHistory() {
        return null;
    }
}
