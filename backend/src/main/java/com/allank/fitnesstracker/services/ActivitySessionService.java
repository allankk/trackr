package com.allank.fitnesstracker.services;

import com.allank.fitnesstracker.dto.request.session.SessionActivityRequestDto;
import com.allank.fitnesstracker.dto.request.session.SessionRequestDto;
import com.allank.fitnesstracker.dto.request.session.SessionMetricRequestDto;
import com.allank.fitnesstracker.dto.response.session.SessionResponseDto;
import com.allank.fitnesstracker.mapper.ActivitySessionResponseMapper;
import com.allank.fitnesstracker.models.*;
import com.allank.fitnesstracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<SessionResponseDto> getAllSessions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<ActivitySession> activitySessions = activitySessionRepository.findByUser(user);

        return activitySessions.stream()
                .map(activitySessionResponseMapper::toDto)
                .toList();
    }
}
