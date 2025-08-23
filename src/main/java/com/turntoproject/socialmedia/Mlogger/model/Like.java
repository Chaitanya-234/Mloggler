package com.turntoproject.socialmedia.Mlogger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Composite unique constraint to prevent duplicate likes
    @Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
    })
    public static class LikeId {
        // This is just for the unique constraint annotation
    }
}
