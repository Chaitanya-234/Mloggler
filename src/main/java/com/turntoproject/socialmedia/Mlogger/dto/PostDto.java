package com.turntoproject.socialmedia.Mlogger.dto;

import com.turntoproject.socialmedia.Mlogger.model.ContentType;
import com.turntoproject.socialmedia.Mlogger.model.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    
    private Long id;
    private UserDto user;
    private ContentType contentType;
    private String title;
    private String description;
    private Set<String> tags;
    private Set<String> hashtags;
    private Visibility visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private int commentsCount;
    private boolean isLikedByCurrentUser;
}
