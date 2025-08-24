package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.model.Notification;
import com.turntoproject.socialmedia.Mlogger.service.NotificationService;
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
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    @Operation(summary = "Get user notifications", description = "Retrieves all notifications for the authenticated user")
    public ResponseEntity<Page<Notification>> getUserNotifications(Pageable pageable) {
        Long currentUserId = getCurrentUserId();
        log.info("Getting notifications for user: {}", currentUserId);
        Page<Notification> notifications = notificationService.getUserNotifications(currentUserId, pageable);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread/count")
    @Operation(summary = "Get unread notifications count", description = "Retrieves the count of unread notifications")
    public ResponseEntity<Long> getUnreadNotificationsCount() {
        Long currentUserId = getCurrentUserId();
        log.info("Getting unread notifications count for user: {}", currentUserId);
        long count = notificationService.getUnreadNotificationsCount(currentUserId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{notificationId}/read")
    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
    
    @PutMapping("/read-all")
    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications as read for the user")
    public ResponseEntity<String> markAllNotificationsAsRead() {
        Long currentUserId = getCurrentUserId();
        log.info("Marking all notifications as read for user: {}", currentUserId);
        notificationService.markAllNotificationsAsRead(currentUserId);
        return ResponseEntity.ok("All notifications marked as read");
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // This is a simplified approach - in a real application, you'd want to store user ID in the JWT
        return Long.parseLong(authentication.getName());
    }
}
