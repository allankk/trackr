package com.allank.trackr.dto.response.session;

import com.allank.trackr.dto.UnitDto;

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
