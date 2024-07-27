package com.allank.trackr.services;

import com.allank.trackr.dto.response.dashboard.ResultsDto;
import com.allank.trackr.models.ActivitySession;
import com.allank.trackr.models.ActivitySessionMetric;
import com.allank.trackr.models.User;
import com.allank.trackr.models.Unit;
import com.allank.trackr.repository.ActivitySessionMetricRepository;
import com.allank.trackr.repository.ActivitySessionRepository;
import com.allank.trackr.repository.UnitRepository;
import com.allank.trackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DashboardService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivitySessionRepository activitySessionRepository;

    @Autowired
    ActivitySessionMetricRepository activitySessionMetricRepository;

    @Autowired
    UnitRepository unitRepository;

    public List<Instant> getAllSessionDates(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ActivitySession> activitySessions = activitySessionRepository.findByUser(user);

        return activitySessions.stream().map(ActivitySession::getDate).toList();
    }

    public ResultsDto getResults(Long userId, Long activityTypeId, Long metricId, Long unitId) {

        List<ActivitySessionMetric> activitySessionMetrics = activitySessionMetricRepository.findByActivitySessionUserIdAndActivityTypeIdAndMetricId(userId, activityTypeId, metricId);

        if (activitySessionMetrics.isEmpty()) {
            return new ResultsDto(List.of(), List.of());
        }

        double conversionFactor = unitRepository.findById(unitId)
                .map(Unit::getConversionFactor)
                .orElseThrow(() -> new NoSuchElementException("Unit not found with id: " + unitId));

        List<ActivitySessionMetric> sortedMetrics = activitySessionMetrics.stream()
                .sorted(Comparator.comparing(metric -> metric.getActivitySession().getDate()))
                .toList();

        List<Instant> dates = sortedMetrics.stream()
                        .map(metric -> metric.getActivitySession().getDate())
                        .toList();

        List<Double> values = sortedMetrics.stream()
                .map(metric -> metric.getValue() / conversionFactor)
                .toList();

        return new ResultsDto(dates, values);
    }
}
