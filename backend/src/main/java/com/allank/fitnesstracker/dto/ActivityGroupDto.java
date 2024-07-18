package com.allank.fitnesstracker.dto;

import java.util.Map;
import java.util.Set;

public record ActivityGroupDto(
        Long id,
        String name,
        String description,
        Set<Long> activityTypes
) {
}
