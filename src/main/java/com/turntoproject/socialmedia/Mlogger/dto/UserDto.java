package com.turntoproject.socialmedia.Mlogger.dto;

import com.turntoproject.socialmedia.Mlogger.model.Role;
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
public class UserDto {
    
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profilePicture;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int followersCount;
    private int followingCount;
    private int postsCount;
}
