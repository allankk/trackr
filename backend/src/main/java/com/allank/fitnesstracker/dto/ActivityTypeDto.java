package com.allank.fitnesstracker.dto;

import com.allank.fitnesstracker.models.Metric;

import java.util.Set;

public record ActivityTypeDto(
        Long id,
        String name,
        String description,
        Set<Metric> metrics,
        boolean isDefault
) {
}
