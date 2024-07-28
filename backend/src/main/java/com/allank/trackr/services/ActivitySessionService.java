package com.allank.trackr.services;

import com.allank.trackr.dto.request.session.SessionActivityRequestDto;
import com.allank.trackr.dto.request.session.SessionRequestDto;
import com.allank.trackr.dto.request.session.SessionMetricRequestDto;
import com.allank.trackr.dto.response.session.GroupedSessionResponseDto;
import com.allank.trackr.dto.response.session.SessionResponseDto;
import com.allank.trackr.mapper.ActivitySessionResponseMapper;
import com.allank.trackr.models.*;
import com.allank.trackr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivitySessionService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivitySessionRepository activitySessionRepository;

    @Autowired
    ActivitySessionResponseMapper activitySessionResponseMapper;

    @Autowired
    ActivityTypeRepository activityTypeRepository;

    @Autowired
    MetricRepository metricRepository;

    @Autowired
    UnitRepository unitRepository;

    public void addSession(Long userId, SessionRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        ActivitySession activitySession = new ActivitySession();
        activitySession.setUser(user);
        activitySession.setNotes(dto.notes());
        activitySession.setDate(dto.date());

        for (SessionActivityRequestDto activityDto : dto.activities()) {
            ActivityType activityType = activityTypeRepository.findById(activityDto.id()).orElseThrow(() -> new RuntimeException("ActivityType not found"));
            activitySession.getActivityTypes().add(activityType);

            for (SessionMetricRequestDto metricDto : activityDto.metrics()) {
                if (metricDto.value() == null) {
                    continue;
                }

                Metric metric = metricRepository.findById(metricDto.id()).orElseThrow(() -> new RuntimeException("Metric not found"));
                Unit selectedUnit = unitRepository.findById(metricDto.unitId()).orElseThrow(() -> new RuntimeException("Metric not found"));

                double standardizedValue = metricDto.value() * selectedUnit.getConversionFactor();

                ActivitySessionMetric activitySessionMetric = new ActivitySessionMetric();
                activitySessionMetric.setActivitySession(activitySession);
                activitySessionMetric.setActivityType(activityType);
                activitySessionMetric.setMetric(metric);
                activitySessionMetric.setUnit(selectedUnit);
                activitySessionMetric.setValue(standardizedValue);

                activitySession.getActivitySessionMetrics().add(activitySessionMetric);
            }
        }

        activitySessionRepository.save(activitySession);
    }

    public List<GroupedSessionResponseDto> getAllSessions(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<ActivitySession> activitySessions = activitySessionRepository.findByUser(user);

        return activitySessionResponseMapper.toGroupedDtos(activitySessions);
    }

    public List<GroupedSessionResponseDto> getFilteredSessions(Long userId, Instant startDate, Instant endDate, List<Long> activityTypes) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<ActivitySession> activitySessions = new ArrayList<>(activitySessionRepository.findByUser(user));

        // Filter by date range
        if (startDate != null && endDate != null) {
            activitySessions = activitySessions.stream()
                    .filter(session -> !session.getDate().isBefore(startDate) && !session.getDate().isAfter(endDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        // Filter by activity types
        if (activityTypes != null && !activityTypes.isEmpty()) {
            activitySessions = activitySessions.stream()
                    .filter(session -> session.getActivityTypes().stream().anyMatch(type -> activityTypes.contains(type.getId())))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return activitySessionResponseMapper.toGroupedDtos(activitySessions);
    }

    public SessionResponseDto getSession(Long userId, Long activitySessionId) {
        ActivitySession activitySession = activitySessionRepository.findById(activitySessionId).orElseThrow(() -> new RuntimeException("Activity Session not found"));

        if (!activitySession.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to view this session");
        }

        return activitySessionResponseMapper.toDto(activitySession);
    }

    public List<GroupedSessionResponseDto> getSessionByDate(Long userId, Instant sessionDate) {
        Instant startOfDay = sessionDate.truncatedTo(ChronoUnit.DAYS);
        Instant endOfDay = startOfDay.plus(Duration.ofDays(1));
        List<ActivitySession> activitySessions = activitySessionRepository.findByDateAfterAndDateBefore(startOfDay, endOfDay);

        List<ActivitySession> userSessions = activitySessions.stream()
                .filter(session -> session.getUser().getId().equals(userId))
                .toList();

        return userSessions.isEmpty() ? Collections.emptyList() : activitySessionResponseMapper.toGroupedDtos(userSessions);
    }

    public void updateSession(Long userId, Long activitySessionId, SessionRequestDto sessionRequestDto) {
        ActivitySession activitySession = activitySessionRepository.findById(activitySessionId).orElseThrow(() -> new RuntimeException("Activity Session not found"));

        if (!activitySession.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to update this session");
        }

        activitySession.setDate(sessionRequestDto.date());
        activitySession.setNotes(sessionRequestDto.notes());

        activitySession.getActivityTypes().clear();
        activitySession.getActivitySessionMetrics().clear();

        for (SessionActivityRequestDto activityDto : sessionRequestDto.activities()) {
            ActivityType activityType = activityTypeRepository.findById(activityDto.id()).orElseThrow(() -> new RuntimeException("ActivityType not found"));
            activitySession.getActivityTypes().add(activityType);

            for (SessionMetricRequestDto metricDto : activityDto.metrics()) {
                if (metricDto.value() == null) {
                    continue;
                }

                Metric metric = metricRepository.findById(metricDto.id())
                        .orElseThrow(() -> new RuntimeException("Metric not found"));
                Unit selectedUnit = unitRepository.findById(metricDto.unitId())
                        .orElseThrow(() -> new RuntimeException("Unit not found"));

                double standardizedValue = metricDto.value() * selectedUnit.getConversionFactor();

                ActivitySessionMetric activitySessionMetric = new ActivitySessionMetric();
                activitySessionMetric.setActivitySession(activitySession);
                activitySessionMetric.setActivityType(activityType);
                activitySessionMetric.setMetric(metric);
                activitySessionMetric.setUnit(selectedUnit);
                activitySessionMetric.setValue(standardizedValue);

                activitySession.getActivitySessionMetrics().add(activitySessionMetric);
            }
        }

        activitySessionRepository.save(activitySession);
    }

    public void deleteSession(Long userId, Long activitySessionId) {
        ActivitySession activitySession = activitySessionRepository.findById(activitySessionId).orElseThrow(() -> new RuntimeException("Activity Session not found"));

        if (!activitySession.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to update this session");
        }

        activitySessionRepository.delete(activitySession);
    }
}
