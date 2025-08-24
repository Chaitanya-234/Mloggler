package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Likes", description = "Like management APIs")
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping("/{postId}/like")
    @Operation(summary = "Like a post", description = "Likes a specific post")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        Long currentUserId = getCurrentUserId();
        log.info("User {} liking post: {}", currentUserId, postId);
        likeService.likePost(postId, currentUserId);
        return ResponseEntity.ok("Post liked successfully");
    }
    
    @DeleteMapping("/{postId}/like")
    @Operation(summary = "Unlike a post", description = "Removes like from a specific post")
    public ResponseEntity<String> unlikePost(@PathVariable Long postId) {
        Long currentUserId = getCurrentUserId();
        log.info("User {} unliking post: {}", currentUserId, postId);
        likeService.unlikePost(postId, currentUserId);
        return ResponseEntity.ok("Post unliked successfully");
    }
    
    @GetMapping("/{postId}/likes/count")
    @Operation(summary = "Get post likes count", description = "Retrieves the total number of likes for a post")
    public ResponseEntity<Long> getPostLikesCount(@PathVariable Long postId) {
        log.info("Getting likes count for post: {}", postId);
        long count = likeService.getPostLikesCount(postId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{postId}/liked")
    @Operation(summary = "Check if post is liked", description = "Checks if the current user has liked a specific post")
    public ResponseEntity<Boolean> isPostLikedByUser(@PathVariable Long postId) {
        Long currentUserId = getCurrentUserId();
        log.info("Checking if user {} liked post: {}", currentUserId, postId);
        boolean isLiked = likeService.isPostLikedByUser(postId, currentUserId);
        return ResponseEntity.ok(isLiked);
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // This is a simplified approach - in a real application, you'd want to store user ID in the JWT
        return Long.parseLong(authentication.getName());
    }
}
