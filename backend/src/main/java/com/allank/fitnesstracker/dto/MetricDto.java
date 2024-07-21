package com.allank.fitnesstracker.dto;

import com.allank.fitnesstracker.models.Metric;

import java.util.List;
import java.util.Set;

public record MetricDto(
        Long id,
        String name,
        String standardUnit,
        List<UnitDto> units
) {
}
