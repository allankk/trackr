package com.allank.fitnesstracker.dto;

import java.util.List;
import java.util.Map;

public record ActivityGroupResponseDto(
        Long id,
        String name,
        String description,
        List<ActivityTypeDto> activityTypes
) {
}