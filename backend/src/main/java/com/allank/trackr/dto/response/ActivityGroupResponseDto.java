package com.allank.trackr.dto.response;

import com.allank.trackr.dto.ActivityTypeDto;

import java.util.List;

public record ActivityGroupResponseDto(
        Long id,
        String name,
        String description,
        List<ActivityTypeDto> activityTypes
) {
}