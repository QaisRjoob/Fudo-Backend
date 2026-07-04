package com.fudo.story.controller;

import com.fudo.story.application.dto.command.CreateStoryCommand;
import com.fudo.story.application.dto.command.DeleteStoryCommand;
import com.fudo.story.application.dto.command.ReplyToStoryCommand;
import com.fudo.story.application.dto.command.ViewStoryCommand;
import com.fudo.story.application.dto.query.GetStoriesQuery;
import com.fudo.story.application.dto.query.GetStoryArchiveQuery;
import com.fudo.story.application.dto.query.StoryDto;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    private final Mediator mediator;

    public StoryController(Mediator mediator) {
        this.mediator = mediator;
    }

    private UUID currentUserId() {
        return CurrentUser.require().userId();
    }

    @PostMapping
    public ResponseEntity<CreateStoryCommand.CreateStoryResult> createStory(@Valid @RequestBody CreateStoryCommand cmd) {
        cmd.setAuthorId(currentUserId());
        return ResponseEntity.ok(mediator.send(cmd));
    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<Void> deleteStory(@PathVariable UUID storyId) {
        DeleteStoryCommand cmd = new DeleteStoryCommand();
        cmd.setStoryId(storyId);
        cmd.setRequestingUserId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{storyId}/view")
    public ResponseEntity<Void> viewStory(@PathVariable UUID storyId) {
        ViewStoryCommand cmd = new ViewStoryCommand();
        cmd.setStoryId(storyId);
        cmd.setViewerId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{storyId}/reply")
    public ResponseEntity<Void> replyToStory(@PathVariable UUID storyId, @Valid @RequestBody ReplyToStoryCommand cmd) {
        cmd.setStoryId(storyId);
        cmd.setSenderId(currentUserId());
        mediator.send(cmd);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/feed")
    public ResponseEntity<List<GetStoriesQuery.StoryGroupDto>> getFeedStories(@RequestBody GetStoriesQuery query) {
        query.setRequestingUserId(currentUserId());
        return ResponseEntity.ok(mediator.send(query));
    }

    @GetMapping("/{authorId}/archive")
    public ResponseEntity<List<StoryDto>> getArchive(@PathVariable UUID authorId,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "30") int pageSize) {
        GetStoryArchiveQuery query = new GetStoryArchiveQuery();
        query.setAuthorId(authorId);
        query.setPage(page);
        query.setPageSize(pageSize);
        return ResponseEntity.ok(mediator.send(query));
    }
}
