package com.fudo.user.controller;

import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.pagination.PagedResult;
import com.fudo.shared.security.CurrentUser;
import com.fudo.user.application.dto.command.CreateProfileCommand;
import com.fudo.user.application.dto.command.FollowUserCommand;
import com.fudo.user.application.dto.command.UnfollowUserCommand;
import com.fudo.user.application.dto.command.UpdateProfileCommand;
import com.fudo.user.application.dto.query.GetFollowersQuery;
import com.fudo.user.application.dto.query.GetFollowingQuery;
import com.fudo.user.application.dto.query.GetSuggestedUsersQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery;
import com.fudo.user.application.dto.query.GetUserProfileQuery.UserProfileDto;
import com.fudo.user.application.dto.query.SearchUsersQuery;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Mediator mediator;

    public UserController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable UUID userId) {
        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUserId(userId);
        query.setRequestingUserId(CurrentUser.require().userId());
        UserProfileDto result = mediator.send(query);
        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserProfileDto> getProfileByUsername(@PathVariable String username) {
        GetUserProfileQuery query = new GetUserProfileQuery();
        query.setUsername(username);
        query.setRequestingUserId(CurrentUser.require().userId());
        UserProfileDto result = mediator.send(query);
        return result == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResult<UserProfileDto>> search(
            @RequestParam("q") String q,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        SearchUsersQuery query = new SearchUsersQuery();
        query.setQuery(q);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<UserProfileDto>> getSuggested(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        GetSuggestedUsersQuery query = new GetSuggestedUsersQuery();
        query.setUserId(CurrentUser.require().userId());
        query.setLimit(limit);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileCommand cmd) {
        cmd.setUserId(CurrentUser.require().userId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> follow(@PathVariable UUID userId) {
        mediator.send(new FollowUserCommand(CurrentUser.require().userId(), userId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable UUID userId) {
        mediator.send(new UnfollowUserCommand(CurrentUser.require().userId(), userId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<PagedResult<UserProfileDto>> getFollowers(
            @PathVariable UUID userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        GetFollowersQuery query = new GetFollowersQuery();
        query.setUserId(userId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<PagedResult<UserProfileDto>> getFollowing(
            @PathVariable UUID userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        GetFollowingQuery query = new GetFollowingQuery();
        query.setUserId(userId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }

    @PostMapping("/internal/create-profile")
    public ResponseEntity<Void> createProfile(@Valid @RequestBody CreateProfileCommand cmd) {
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }
}
