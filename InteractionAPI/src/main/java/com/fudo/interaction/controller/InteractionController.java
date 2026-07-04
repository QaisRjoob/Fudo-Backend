package com.fudo.interaction.controller;

import com.fudo.interaction.application.dto.command.AddCommentCommand;
import com.fudo.interaction.application.dto.command.DeleteCommentCommand;
import com.fudo.interaction.application.dto.command.LikePostCommand;
import com.fudo.interaction.application.dto.command.SavePostCommand;
import com.fudo.interaction.application.dto.command.UnlikePostCommand;
import com.fudo.interaction.application.dto.command.UnsavePostCommand;
import com.fudo.interaction.application.dto.query.CommentDto;
import com.fudo.interaction.application.dto.query.GetLikeStatusQuery;
import com.fudo.interaction.application.dto.query.GetPostCommentsQuery;
import com.fudo.interaction.application.dto.query.GetPostLikersQuery;
import com.fudo.interaction.application.dto.query.GetSavedPostsQuery;
import com.fudo.interaction.application.dto.query.LikeStatusDto;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.shared.security.CurrentUser;
import com.fudo.shared.security.FudoUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    private final Mediator mediator;

    public InteractionController(Mediator mediator) {
        this.mediator = mediator;
    }

    private UUID currentUserId() {
        return CurrentUser.require().userId();
    }

    private boolean isAdmin() {
        FudoUserPrincipal principal = CurrentUser.require();
        return principal.admin();
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> like(@PathVariable UUID postId) {
        LikePostCommand cmd = new LikePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unlike(@PathVariable UUID postId) {
        UnlikePostCommand cmd = new UnlikePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<List<UUID>> getLikers(@PathVariable UUID postId,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int pageSize) {
        GetPostLikersQuery query = new GetPostLikersQuery();
        query.setPostId(postId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @GetMapping("/posts/{postId}/status")
    public ResponseEntity<LikeStatusDto> getStatus(@PathVariable UUID postId) {
        GetLikeStatusQuery query = new GetLikeStatusQuery(postId, currentUserId());
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<AddCommentCommand.AddCommentResult> addComment(@PathVariable UUID postId,
                                                                          @Valid @RequestBody AddCommentCommand cmd) {
        cmd.setPostId(postId);
        cmd.setAuthorId(currentUserId());
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId) {
        DeleteCommentCommand cmd = new DeleteCommentCommand();
        cmd.setCommentId(commentId);
        cmd.setRequestingUserId(currentUserId());
        cmd.setAdmin(isAdmin());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<PagedResult<CommentDto>> getComments(@PathVariable UUID postId,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "20") int pageSize) {
        GetPostCommentsQuery query = new GetPostCommentsQuery();
        query.setPostId(postId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/posts/{postId}/save")
    public ResponseEntity<Void> save(@PathVariable UUID postId) {
        SavePostCommand cmd = new SavePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{postId}/save")
    public ResponseEntity<Void> unsave(@PathVariable UUID postId) {
        UnsavePostCommand cmd = new UnsavePostCommand();
        cmd.setPostId(postId);
        cmd.setUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<PagedResult<UUID>> getSaved(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "20") int pageSize) {
        GetSavedPostsQuery query = new GetSavedPostsQuery();
        query.setUserId(currentUserId());
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }
}
