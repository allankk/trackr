package com.allank.trackr.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.allank.trackr.security.services.UserDetailsImpl;
import com.allank.trackr.dto.response.UserDto;

import java.util.stream.Collectors;
import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('DEMO') or hasRole('ADMIN')")
    public UserDto userAccess() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getEmail();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::mapRoleToString)
                .collect(Collectors.joining(", "));

        return new UserDto(email, roles);
    }

    private String mapRoleToString(String role) {
        return switch (role) {
            case "ROLE_USER" -> "user";
            case "ROLE_DEMO" -> "demo";
            case "ROLE_ADMIN" -> "admin";
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }
}
