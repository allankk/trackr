package com.allank.fitnesstracker.dto.request.session;

import java.util.List;

public record SessionActivityRequestDto(
        Long id,
        List<SessionMetricRequestDto> metrics
) {
}
