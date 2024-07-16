package com.allank.fitnesstracker.dto;

import com.allank.fitnesstracker.models.Metric;

import java.util.Set;

public record MetricDto(
        Long id,
        String name,
        String unit
) {
}
