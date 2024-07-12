package com.allank.fitnesstracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.allank.fitnesstracker.security.services.UserDetailsImpl;

import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess(@RequestHeader("Authorization") String token) {
        // Extract JWT token (remove the Bearer prefix if present)
        String jwtToken = token.startsWith("Bearer: ") ? token.substring(7) : token;

        // Get authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Or cast to your custom UserDetails for more info

        // Optionally, you can access the authorities/roles
        String roles = authentication.getAuthorities().toString();

        // Construct response with user details and token

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usernameContext = userDetails.getUsername();
        String email = userDetails.getEmail();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return String.format("user Content. Username: %s, Roles: %s, email: %s", usernameContext, email, authorities);

        //return String.format("User Content. Username: %s, Roles: %s, Token: %s", username, roles, jwtToken);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
