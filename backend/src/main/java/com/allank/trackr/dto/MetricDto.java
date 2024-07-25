package com.allank.trackr.dto;

import java.util.List;

public record MetricDto(
        Long id,
        String name,
        String standardUnit,
        List<UnitDto> units
) {
}
