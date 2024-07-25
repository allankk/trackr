package com.allank.trackr.dto.request.session;

public record SessionMetricRequestDto(
        Long id,
        Long unitId,
        Double value
) {
}
