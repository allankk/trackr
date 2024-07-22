package com.allank.fitnesstracker.dto.response.session;

public record SessionMetricResponseDto(
        Long id,
        String metricName,
        String unitName,
        Double value
) {
}
