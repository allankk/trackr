package com.allank.trackr.mapper;

import com.allank.trackr.dto.UnitDto;
import com.allank.trackr.dto.response.session.GroupedSessionResponseDto;
import com.allank.trackr.dto.response.session.SessionActivityResponseDto;
import com.allank.trackr.dto.response.session.SessionMetricResponseDto;
import com.allank.trackr.dto.response.session.SessionResponseDto;
import com.allank.trackr.models.ActivitySession;
import com.allank.trackr.models.ActivitySessionMetric;
import com.allank.trackr.models.ActivityType;
import com.allank.trackr.models.Unit;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActivitySessionResponseMapper {

    public List<GroupedSessionResponseDto> toGroupedDtos(List<ActivitySession> sessions) {
        Map<Instant, List<SessionResponseDto>> groupedByDate = sessions.stream()
                .map(this::toDto)
                .collect(Collectors.groupingBy(SessionResponseDto::date));

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
