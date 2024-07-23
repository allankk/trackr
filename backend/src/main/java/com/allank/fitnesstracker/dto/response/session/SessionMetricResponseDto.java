package com.allank.fitnesstracker.dto.response.session;

import com.allank.fitnesstracker.dto.UnitDto;

import java.util.List;

public record SessionMetricResponseDto(
        Long id,
        String metricName,
        String selectedUnitName,
        Long selectedUnitId,
        List<UnitDto> units,
        Double value
) {
}
