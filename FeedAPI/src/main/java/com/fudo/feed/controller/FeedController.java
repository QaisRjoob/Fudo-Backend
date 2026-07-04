package com.fudo.feed.controller;

import com.fudo.feed.application.dto.FeedPost;
import com.fudo.feed.application.dto.query.GetExploreFeedQuery;
import com.fudo.feed.application.dto.query.GetFeedQuery;
import com.fudo.shared.cqrs.Mediator;
import com.fudo.shared.pagination.CursorPage;
import com.fudo.shared.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final Mediator mediator;

    public FeedController(Mediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping
    public ResponseEntity<CursorPage<FeedPost>> getFeed(@RequestParam(defaultValue = "20") int limit,
                                                          @RequestParam(required = false) String cursor) {
        UUID userId = CurrentUser.require().userId();
        CursorPage<FeedPost> result = mediator.send(new GetFeedQuery(userId, limit, cursor));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/explore")
    public ResponseEntity<CursorPage<FeedPost>> getExploreFeed(@RequestParam(defaultValue = "20") int limit,
                                                                 @RequestParam(required = false) String cursor) {
        UUID userId = CurrentUser.require().userId();
        CursorPage<FeedPost> result = mediator.send(new GetExploreFeedQuery(userId, limit, cursor));
        return ResponseEntity.ok(result);
    }
}
