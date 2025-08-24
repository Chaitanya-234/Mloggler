package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.dto.UserDto;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }
    
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return convertToDto(user);
    }
    
    public Page<UserDto> searchUsers(String query, Pageable pageable) {
        return userRepository.searchUsers(query, pageable)
                .map(this::convertToDto);
    }
    
    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("Follower not found"));
        
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UsernameNotFoundException("User to follow not found"));
        
        if (followerId.equals(followingId)) {
            throw new RuntimeException("Cannot follow yourself");
        }
        
        follower.getFollowing().add(following);
        following.getFollowers().add(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
    }
    
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UsernameNotFoundException("Follower not found"));
        
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UsernameNotFoundException("User to unfollow not found"));
        
        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
    }
    
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profilePicture(user.getProfilePicture())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .followersCount((int) userRepository.countFollowers(user.getId()))
                .followingCount((int) userRepository.countFollowing(user.getId()))
                .postsCount((int) userRepository.countPosts(user.getId()))
                .build();
    }
}
