package com.allank.fitnesstracker.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record RegisterRequest(
        @NotBlank @Size(min = 5, max = 50) String email,
        @NotBlank @Size(min = 6, max = 50) String password,
        Set<String> role
) {}