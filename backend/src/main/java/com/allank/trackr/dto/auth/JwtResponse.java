package com.allank.trackr.dto.auth;

import java.util.List;

public record JwtResponse(
        String accessToken,
        String type,
        Long id,
        String email,
        List<String> roles
) {
    public JwtResponse(String accessToken, Long id, String email, List<String> roles) {
        this(accessToken, "Bearer", id, email, roles);
    }
}