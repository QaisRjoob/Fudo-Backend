package com.fudo.post.controller;

import com.fudo.post.application.dto.command.CreatePostCommand;
import com.fudo.post.application.dto.command.CreatePostCommand.CreatePostResult;
import com.fudo.post.application.dto.command.DeletePostCommand;
import com.fudo.post.application.dto.command.UpdatePostCommand;
import com.fudo.post.application.dto.query.GetFeedPostsQuery;
import com.fudo.post.application.dto.query.GetPostQuery;
import com.fudo.post.application.dto.query.GetPostQuery.PostDetailDto;
import com.fudo.post.application.dto.query.GetUserPostsQuery;
import com.fudo.post.application.dto.query.GetUserPostsQuery.PostSummaryDto;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.pagination.CursorPage;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final Mediator mediator;

    public PostController(Mediator mediator) {
        this.mediator = mediator;
    }

    private UUID currentUserId() {
        return CurrentUser.require().userId();
    }

    private boolean isAdmin() {
        return CurrentUser.require().admin();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable UUID postId) {
        PostDetailDto result = mediator.send(new GetPostQuery(postId));
        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResult<PostSummaryDto>> getUserPosts(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        GetUserPostsQuery query = new GetUserPostsQuery();
        query.setAuthorId(userId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/feed")
    public ResponseEntity<CursorPage<PostSummaryDto>> getFeedPosts(@RequestBody GetFeedPostsQuery query) {
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping
    public ResponseEntity<CreatePostResult> createPost(@Valid @RequestBody CreatePostCommand cmd) {
        cmd.setAuthorId(currentUserId());
        CreatePostResult result = mediator.send(cmd);
        return ResponseEntity.created(URI.create("/api/posts/" + result.getPostId())).body(result);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable UUID postId, @Valid @RequestBody UpdatePostCommand cmd) {
        cmd.setPostId(postId);
        cmd.setRequestingUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID postId) {
        mediator.send(new DeletePostCommand(postId, currentUserId(), isAdmin()));
        return ResponseEntity.noContent().build();
    }
}
