package com.allank.fitnesstracker.mapper;

import com.allank.fitnesstracker.dto.ActivityTypeDto;
import com.allank.fitnesstracker.dto.MetricDto;
import com.allank.fitnesstracker.dto.UnitDto;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.models.Metric;
import com.allank.fitnesstracker.models.Unit;
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
