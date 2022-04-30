package ua.tunepoint.audio.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tunepoint.audio.model.request.TagCreateRequest;
import ua.tunepoint.audio.model.response.TagCreateResponse;
import ua.tunepoint.audio.model.response.TagGetResponse;
import ua.tunepoint.audio.model.response.TagSearchResponse;
import ua.tunepoint.audio.service.TagService;
import ua.tunepoint.web.exception.BadRequestException;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<TagGetResponse> findTag(@PathVariable Long id) {
        return ResponseEntity.ok(
                TagGetResponse.builder()
                        .payload(
                                tagService.findById(id)
                        )
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<TagSearchResponse> findTags(@RequestParam(name = "name") String name) {
        if (name.length() < 2) {
            throw new BadRequestException("Name pattern should be 2 symbols or more");
        }
        return ResponseEntity.ok(
                TagSearchResponse.builder()
                        .payload(tagService.findLike(name))
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TagCreateResponse> creatTag(@RequestBody @Validated TagCreateRequest request) {
        return ResponseEntity.ok(
            TagCreateResponse.builder()
                    .payload(
                            tagService.create(request)
                    )
                    .build()
        );
    }
}
