package com.allank.trackr.services;

import com.allank.trackr.models.*;
import com.allank.trackr.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.allank.trackr.models.Erole.ROLE_DEMO;
import static com.allank.trackr.models.Erole.ROLE_USER;

@Service
public class ActivityService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private ActivitySessionRepository activitySessionRepository;

    @Autowired
    private ActivitySessionMetricRepository activitySessionMetricRepository;

    public List<ActivityType> getUserActivityTypes(Long userId) {
        return activityTypeRepository.findByUserId(userId);
    }

    public void addCustomActivityTypeForUser(Long userId, String name, String description, Set<Long> metricIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ActivityType customActivityType = new ActivityType();
        customActivityType.setName(name);
        customActivityType.setDescription(description);
        customActivityType.setDefault(false);
        customActivityType.setUser(user);

        List<Metric> metrics = new ArrayList<>(metricRepository.findAllById(metricIds));
        customActivityType.setMetrics(metrics);

        activityTypeRepository.save(customActivityType);
    }

    public void updateActivityType(Long activityTypeId, Long userId, String name, String description, Set<Long> metricIds) {
        ActivityType activityType = activityTypeRepository.findById(activityTypeId).orElseThrow(() -> new IllegalArgumentException("Activity type not found"));

        if (!activityType.getUser().getId().equals(userId)) {
            throw new SecurityException("Unauthorized to update this activity type");
        }

        activityType.setName(name);
        activityType.setDescription(description);
        List<Metric> metrics = new ArrayList<>(metricRepository.findAllById(metricIds));
        activityType.setMetrics(metrics);

        activityTypeRepository.save(activityType);
    }

    @Transactional
    public void deleteUserActivityType(Long userId, Long activityTypeId) {
        ActivityType activityType = activityTypeRepository.findById(activityTypeId).orElseThrow(() -> new IllegalArgumentException("activity type not found"));

        if (activityType.getUser().getId().equals(userId)) {

            List<ActivitySession> sessions = activitySessionRepository.findAllByActivityTypesContains(activityType);
            for (ActivitySession session : sessions) {
                session.getActivityTypes().remove(activityType);
            }
            activitySessionRepository.saveAll(sessions);

            List<ActivitySessionMetric> metrics = activitySessionMetricRepository.findAllByActivityType(activityType);
            activitySessionMetricRepository.deleteAll(metrics);

            List<ActivityGroup> groups = activityGroupRepository.findAllByActivityTypesContains(activityType);
            for (ActivityGroup group : groups) {
                group.getActivityTypes().remove(activityType);
                activityGroupRepository.save(group);
            }

            activityTypeRepository.delete(activityType);
        } else {
            throw new RuntimeException("Activity Type does not belong do the given user.");
        }
    }

    public List<Metric> getAllMetrics() {
        return metricRepository.findAll();
    }
}
