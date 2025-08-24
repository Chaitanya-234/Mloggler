package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.dto.AuthResponse;
import com.turntoproject.socialmedia.Mlogger.dto.UserDto;
import com.turntoproject.socialmedia.Mlogger.model.Role;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import com.turntoproject.socialmedia.Mlogger.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OidcService {
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    public AuthResponse processOidcAuthentication(OAuth2AuthenticationToken authentication) {
        OAuth2User oauth2User = authentication.getPrincipal();
        String provider = authentication.getAuthorizedClientRegistrationId();
        
        log.info("Processing OIDC authentication for provider: {}", provider);
        
        // Extract user information from OAuth2User
        String email = extractEmail(oauth2User, provider);
        String name = extractName(oauth2User, provider);
        String picture = extractPicture(oauth2User, provider);
        
        // Find or create user
        User user = findOrCreateUser(email, name, picture, provider);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        
        // Convert to DTO
        UserDto userDto = userService.convertToDto(user);
        
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 hours
                .user(userDto)
                .build();
    }
    
    private String extractEmail(OAuth2User oauth2User, String provider) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        switch (provider.toLowerCase()) {
            case "google":
                return (String) attributes.get("email");
            case "github":
                return (String) attributes.get("email");
            case "azure":
                return (String) attributes.get("email");
            default:
                return (String) attributes.get("email");
        }
    }
    
    private String extractName(OAuth2User oauth2User, String provider) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        switch (provider.toLowerCase()) {
            case "google":
                return (String) attributes.get("name");
            case "github":
                return (String) attributes.get("login");
            case "azure":
                return (String) attributes.get("name");
            default:
                return (String) attributes.get("name");
        }
    }
    
    private String extractPicture(OAuth2User oauth2User, String provider) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        
        switch (provider.toLowerCase()) {
            case "google":
                return (String) attributes.get("picture");
            case "github":
                return (String) attributes.get("avatar_url");
            case "azure":
                return null; // Azure doesn't provide profile picture by default
            default:
                return null;
        }
    }
    
    private User findOrCreateUser(String email, String name, String picture, String provider) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update profile picture if available and different
            if (picture != null && !picture.equals(user.getProfilePicture())) {
                user.setProfilePicture(picture);
                userRepository.save(user);
            }
            return user;
        }
        
        // Create new user
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        
        User newUser = User.builder()
                .username(generateUniqueUsername(name))
                .email(email)
                .bio("Signed up via " + provider)
                .profilePicture(picture)
                .roles(roles)
                .password("") // OIDC users don't have passwords
                .build();
        
        return userRepository.save(newUser);
    }
    
    private String generateUniqueUsername(String baseName) {
        String username = baseName.toLowerCase().replaceAll("[^a-z0-9]", "");
        String finalUsername = username;
        int counter = 1;
        
        while (userRepository.existsByUsername(finalUsername)) {
            finalUsername = username + counter;
            counter++;
        }
        
        return finalUsername;
    }
}
