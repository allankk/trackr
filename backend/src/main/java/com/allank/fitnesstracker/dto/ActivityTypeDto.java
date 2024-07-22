package com.allank.fitnesstracker.dto;

import com.allank.fitnesstracker.models.Metric;

import java.util.List;

public record ActivityTypeDto(
        Long id,
        String name,
        String description,
        boolean isDefault,
        List<MetricDto> metrics
) {
}
