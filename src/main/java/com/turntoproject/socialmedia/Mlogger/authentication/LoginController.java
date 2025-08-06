package com.turntoproject.socialmedia.Mlogger.authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account/")
public class LoginController {

    @GetMapping("/sucess/")
    public String success(@AuthenticationPrincipal OAuth2User user) {
        return "Login successful! Welcome " + user.getAttribute("name");
    }

    @GetMapping("/secure")
    public String secure(@AuthenticationPrincipal OAuth2User user) {
        return "Hello, " + user.getAttribute("name");
    }
}
