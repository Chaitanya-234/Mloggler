package com.turntoproject.socialmedia.Mlogger.controller;

import com.turntoproject.socialmedia.Mlogger.dto.AuthResponse;
import com.turntoproject.socialmedia.Mlogger.service.OidcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/oidc")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OpenID Connect", description = "OIDC authentication APIs")
public class OidcController {
    
    private final OidcService oidcService;
    
    @GetMapping("/success")
    @Operation(summary = "OIDC authentication success", description = "Handles successful OIDC authentication")
    public ResponseEntity<AuthResponse> oidcSuccess(@AuthenticationPrincipal OAuth2User principal,
                                                   OAuth2AuthenticationToken authentication) {
        log.info("OIDC authentication successful for user: {}", principal.getName());
        
        try {
            AuthResponse response = oidcService.processOidcAuthentication(authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing OIDC authentication: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user")
    @Operation(summary = "Get OIDC user info", description = "Retrieves current OIDC user information")
    public ResponseEntity<OAuth2User> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            return ResponseEntity.ok(principal);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/providers")
    @Operation(summary = "Get available OIDC providers", description = "Lists available OIDC authentication providers")
    public ResponseEntity<String[]> getAvailableProviders() {
        String[] providers = {"google", "github"}; // Add more as configured
        return ResponseEntity.ok(providers);
    }
}
