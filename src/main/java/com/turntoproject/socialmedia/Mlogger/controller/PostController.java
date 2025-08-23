package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.dto.CreatePostRequest;
import com.turntoproject.socialmedia.Mlogger.dto.PostDto;
import com.turntoproject.socialmedia.Mlogger.service.PostService;
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
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Posts", description = "Post management APIs")
public class PostController {
    
    private final PostService postService;
    
    @PostMapping
    @Operation(summary = "Create a new post", description = "Creates a new post for the authenticated user")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequest request) {
        Long currentUserId = getCurrentUserId();
        log.info("Creating new post for user: {}", currentUserId);
        PostDto post = postService.createPost(request, currentUserId);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieves a post by its ID")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Long currentUserId = getCurrentUserId();
        log.info("Getting post by ID: {}", id);
        PostDto post = postService.getPostById(id, currentUserId);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user posts", description = "Retrieves all posts for a specific user")
    public ResponseEntity<Page<PostDto>> getUserPosts(@PathVariable Long userId, Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        log.info("Getting posts for user: {}", userId);
        Page<PostDto> posts = postService.getUserPosts(userId, pageable, currentUserId);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/feed")
    @Operation(summary = "Get user feed", description = "Retrieves the personalized feed for the authenticated user")
    public ResponseEntity<Page<PostDto>> getFeedPosts(Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        log.info("Getting feed posts for user: {}", currentUserId);
        Page<PostDto> posts = postService.getFeedPosts(currentUserId, pageable);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/trending")
    @Operation(summary = "Get trending posts", description = "Retrieves trending posts")
    public ResponseEntity<Page<PostDto>> getTrendingPosts(Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        log.info("Getting trending posts");
        Page<PostDto> posts = postService.getTrendingPosts(pageable, currentUserId);
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Searches posts by title, description, tags, or hashtags")
    public ResponseEntity<Page<PostDto>> searchPosts(@RequestParam String query, Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        log.info("Searching posts with query: {}", query);
        Page<PostDto> posts = postService.searchPosts(query, pageable, currentUserId);
        return ResponseEntity.ok(posts);
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // This is a simplified approach - in a real application, you'd want to store user ID in the JWT
        return Long.parseLong(authentication.getName());
    }
}
