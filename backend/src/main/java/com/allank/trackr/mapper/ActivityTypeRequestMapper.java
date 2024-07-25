package com.allank.trackr.mapper;

import com.allank.trackr.dto.ActivityTypeDto;
import com.allank.trackr.dto.MetricDto;
import com.allank.trackr.dto.UnitDto;
import com.allank.trackr.models.ActivityType;
import com.allank.trackr.models.Metric;
import com.allank.trackr.models.Unit;
import org.springframework.stereotype.Component;

@Component
public class ActivityTypeRequestMapper {

    public ActivityTypeDto toDto(ActivityType activityType) {
        return new ActivityTypeDto(
                activityType.getId(),
                activityType.getName(),
                activityType.getDescription(),
                activityType.isDefault(),
                activityType.getMetrics().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    public MetricDto toDto(Metric metric) {
        return new MetricDto(
                metric.getId(),
                metric.getName(),
                metric.getStandardUnit(),
                metric.getUnits().stream()
                        .map(this::toDto)
                        .toList()
        );
    }

    public UnitDto toDto(Unit unit) {
        return new UnitDto(
                unit.getId(),
                unit.getName(),
                unit.getUnit(),
                unit.getConversionFactor()
        );
    }
}
