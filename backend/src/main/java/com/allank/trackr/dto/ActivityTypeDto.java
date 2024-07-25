package com.allank.trackr.dto;

import java.util.List;

public record ActivityTypeDto(
        Long id,
        String name,
        String description,
        boolean isDefault,
        List<MetricDto> metrics
) {
}
