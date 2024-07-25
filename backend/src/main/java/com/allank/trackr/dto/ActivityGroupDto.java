package com.allank.trackr.dto;

import java.util.Set;

public record ActivityGroupDto(
        Long id,
        String name,
        String description,
        Set<Long> activityTypes
) {
}
