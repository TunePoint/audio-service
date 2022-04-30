package ua.tunepoint.audio.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tunepoint.audio.model.response.GenreListResponse;
import ua.tunepoint.audio.model.response.GenreResponse;
import ua.tunepoint.audio.service.GenreService;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<GenreListResponse> fetchGenres(
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault Pageable pageable
    )  {
        var genres = (name == null) ? genreService.fetchAll(pageable) :
                genreService.fetchLike(name, pageable);

        return ResponseEntity.ok(
                GenreListResponse.builder()
                        .payload(
                                genres
                        )
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> fetchGenre(@PathVariable Long id) {
        return ResponseEntity.ok(
                GenreResponse.builder()
                        .payload(
                                genreService.fetchWithId(id)
                        )
                        .build()
        );
    }
}
