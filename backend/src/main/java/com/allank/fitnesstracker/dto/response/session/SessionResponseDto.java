package com.allank.fitnesstracker.dto.response.session;

import java.time.Instant;
import java.util.List;

public record SessionResponseDto(
        Long id,
        Instant date,
        String notes,
        List<SessionActivityResponseDto> activityTypes
) {
}
