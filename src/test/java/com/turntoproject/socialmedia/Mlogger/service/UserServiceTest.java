package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.dto.UserDto;
import com.turntoproject.socialmedia.Mlogger.model.Role;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .bio("Test bio")
                .roles(roles)
                .build();
    }

    @Test
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.countFollowers(1L)).thenReturn(5L);
        when(userRepository.countFollowing(1L)).thenReturn(3L);
        when(userRepository.countPosts(1L)).thenReturn(10L);

        // When
        UserDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(5, result.getFollowersCount());
        assertEquals(3, result.getFollowingCount());
        assertEquals(10, result.getPostsCount());
        
        verify(userRepository).findById(1L);
        verify(userRepository).countFollowers(1L);
        verify(userRepository).countFollowing(1L);
        verify(userRepository).countPosts(1L);
    }

    @Test
    void getUserById_UserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        
        verify(userRepository).findById(999L);
    }

    @Test
    void getUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.countFollowers(1L)).thenReturn(5L);
        when(userRepository.countFollowing(1L)).thenReturn(3L);
        when(userRepository.countPosts(1L)).thenReturn(10L);

        // When
        UserDto result = userService.getUserByUsername("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void getUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserByUsername("nonexistent");
        });
        
        verify(userRepository).findByUsername("nonexistent");
    }
}
