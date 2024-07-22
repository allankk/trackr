package com.allank.fitnesstracker.dto.request.session;

public record SessionMetricRequestDto(
        Long id,
        Long unitId,
        Double value
) {
}
