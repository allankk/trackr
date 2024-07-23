package com.allank.fitnesstracker.mapper;

import com.allank.fitnesstracker.dto.UnitDto;
import com.allank.fitnesstracker.dto.response.session.GroupedSessionResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionActivityResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionMetricResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionResponseDto;
import com.allank.fitnesstracker.models.ActivitySession;
import com.allank.fitnesstracker.models.ActivitySessionMetric;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.models.Unit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActivitySessionResponseMapper {

    public List<GroupedSessionResponseDto> toGroupedDtos(List<ActivitySession> sessions) {
        Map<LocalDate, List<SessionResponseDto>> groupedByDate = sessions.stream()
                .map(this::toDto)
                .collect(Collectors.groupingBy(session -> session.date().atZone(ZoneId.systemDefault()).toLocalDate()));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .map(entry -> new GroupedSessionResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    public SessionResponseDto toDto(ActivitySession session) {
        List<SessionActivityResponseDto> activityTypes = session.getActivityTypes().stream()
                .map(activityType -> toDto(activityType, session))
                .toList();

        return new SessionResponseDto(
                session.getId(),
                session.getDate(),
                session.getNotes(),
                activityTypes
        );
    }

    private SessionActivityResponseDto toDto(ActivityType activityType, ActivitySession session) {
        List<SessionMetricResponseDto> metrics = session.getActivitySessionMetrics().stream()
                .filter(amv -> amv.getActivityType().equals(activityType))
                .map(this::toDto)
                .toList();

        return new SessionActivityResponseDto(
                activityType.getId(),
                activityType.getName(),
                activityType.getDescription(),
                metrics
        );
    }

    private SessionMetricResponseDto toDto(ActivitySessionMetric activitySessionMetric) {
        List<UnitDto> units = activitySessionMetric.getMetric().getUnits().stream()
                .map(this::toDto)
                .toList();

        double valueInUserUnit = activitySessionMetric.getValue() / activitySessionMetric.getUnit().getConversionFactor();

        return new SessionMetricResponseDto(
                activitySessionMetric.getMetric().getId(),
                activitySessionMetric.getMetric().getName(),
                activitySessionMetric.getUnit().getName(),
                activitySessionMetric.getUnit().getId(),
                units,
                valueInUserUnit
        );
    }

    private UnitDto toDto(Unit unit) {
        return new UnitDto(
                unit.getId(),
                unit.getName(),
                unit.getUnit(),
                unit.getConversionFactor()
        );
    }
}
