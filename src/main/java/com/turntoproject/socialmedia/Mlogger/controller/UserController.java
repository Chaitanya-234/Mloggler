package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.dto.UserDto;
import com.turntoproject.socialmedia.Mlogger.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management APIs")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves user information by user ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves user information by username")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        log.info("Getting user by username: {}", username);
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Searches users by username or bio")
    public ResponseEntity<Page<UserDto>> searchUsers(@RequestParam String query, Pageable pageable) {
        log.info("Searching users with query: {}", query);
        Page<UserDto> users = userService.searchUsers(query, pageable);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/follow/{id}")
    @Operation(summary = "Follow user", description = "Follows a user")
    public ResponseEntity<String> followUser(@PathVariable Long id) {
        Long currentUserId = getCurrentUserId();
        log.info("User {} following user {}", currentUserId, id);
        userService.followUser(currentUserId, id);
        return ResponseEntity.ok("User followed successfully");
    }
    
    @PostMapping("/unfollow/{id}")
    @Operation(summary = "Unfollow user", description = "Unfollows a user")
    public ResponseEntity<String> unfollowUser(@PathVariable Long id) {
        Long currentUserId = getCurrentUserId();
        log.info("User {} unfollowing user {}", currentUserId, id);
        userService.unfollowUser(currentUserId, id);
        return ResponseEntity.ok("User unfollowed successfully");
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // This is a simplified approach - in a real application, you'd want to store user ID in the JWT
        // For now, we'll assume the username is the user ID or implement a proper user context
        return Long.parseLong(authentication.getName());
    }
}
