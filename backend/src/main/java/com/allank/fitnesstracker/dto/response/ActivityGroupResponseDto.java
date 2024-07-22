package com.allank.fitnesstracker.dto.response;

import com.allank.fitnesstracker.dto.ActivityTypeDto;

import java.util.List;

public record ActivityGroupResponseDto(
        Long id,
        String name,
        String description,
        List<ActivityTypeDto> activityTypes
) {
}