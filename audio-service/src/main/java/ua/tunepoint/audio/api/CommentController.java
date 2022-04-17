package ua.tunepoint.audio.api;

import lombok.RequiredArgsConstructor;
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
import ua.tunepoint.audio.model.request.AudioCommentPostRequest;
import ua.tunepoint.audio.model.request.AudioCommentUpdateRequest;
import ua.tunepoint.audio.model.response.CommentDeleteResponse;
import ua.tunepoint.audio.model.response.CommentGetResponse;
import ua.tunepoint.audio.model.response.CommentsGetResponse;
import ua.tunepoint.audio.model.response.CommentPostResponse;
import ua.tunepoint.audio.model.response.CommentUpdateResponse;
import ua.tunepoint.audio.service.CommentService;
import ua.tunepoint.security.UserPrincipal;
import ua.tunepoint.web.model.StatusResponse;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(params = {"audioId"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentPostResponse> postComment(@RequestParam Long audioId, @RequestBody AudioCommentPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.save(audioId, request, user);
        var response = CommentPostResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"audioId"})
    public ResponseEntity<CommentsGetResponse> getComments(@RequestParam Long audioId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.find(audioId, pageable, user);
        var response = CommentsGetResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommentGetResponse> getComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.find(id, user);
        var response = CommentGetResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Long id, @RequestBody AudioCommentUpdateRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.update(id, request, user);
        var response = CommentUpdateResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDeleteResponse> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.delete(id, user);
        var response = CommentDeleteResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        commentService.like(id, user);
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @DeleteMapping(path = "/{id}/likes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> unlikeComment(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal user) {
        commentService.unlike(id, user);
        return ResponseEntity.ok(StatusResponse.builder().build());
    }

    @PostMapping("/{id}/replies")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentPostResponse> replyComment(@PathVariable Long id, @RequestBody AudioCommentPostRequest request, @AuthenticationPrincipal UserPrincipal user) {
        var payload = commentService.reply(id, request, user);
        var response = CommentPostResponse.builder().payload(payload).build();
        return ResponseEntity.ok(response);
    }
}
