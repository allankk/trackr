package com.allank.trackr.dto.response.session;

import java.util.List;

public record SessionActivityResponseDto(
        Long id,
        String name,
        String description,
        List<SessionMetricResponseDto> metrics
) {
}
