package com.allank.fitnesstracker.dto;

import java.util.Set;

public record UserActivityTypeDto(
        String name,
        String description,
        Set<Long> metrics
) {
}
