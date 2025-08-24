package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.dto.CommentDto;
import com.turntoproject.socialmedia.Mlogger.dto.CreateCommentRequest;
import com.turntoproject.socialmedia.Mlogger.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Comments", description = "Comment management APIs")
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping
    @Operation(summary = "Create a new comment", description = "Creates a new comment on a post")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentRequest request) {
        Long currentUserId = getCurrentUserId();
        log.info("Creating new comment for user: {} on post: {}", currentUserId, request.getPostId());
        CommentDto comment = commentService.createComment(request, currentUserId);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/post/{postId}")
    @Operation(summary = "Get post comments", description = "Retrieves all comments for a specific post")
    public ResponseEntity<Page<CommentDto>> getPostComments(@PathVariable Long postId, Pageable pageable) {
        log.info("Getting comments for post: {}", postId);
        Page<CommentDto> comments = commentService.getPostComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // This is a simplified approach - in a real application, you'd want to store user ID in the JWT
        return Long.parseLong(authentication.getName());
    }
}
