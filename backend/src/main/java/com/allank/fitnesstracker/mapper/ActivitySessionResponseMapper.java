package com.allank.fitnesstracker.mapper;

import com.allank.fitnesstracker.dto.response.session.SessionActivityResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionMetricResponseDto;
import com.allank.fitnesstracker.dto.response.session.SessionResponseDto;
import com.allank.fitnesstracker.models.ActivitySession;
import com.allank.fitnesstracker.models.ActivitySessionMetric;
import com.allank.fitnesstracker.models.ActivityType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivitySessionResponseMapper {

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
        return new SessionMetricResponseDto(
                activitySessionMetric.getId(),
                activitySessionMetric.getMetric().getName(),
                activitySessionMetric.getUnit().getName(),
                activitySessionMetric.getValue()
        );
    }
}
