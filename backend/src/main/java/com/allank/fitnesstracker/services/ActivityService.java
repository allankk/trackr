package com.allank.fitnesstracker.services;

import com.allank.fitnesstracker.models.*;
import com.allank.fitnesstracker.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

public class ActivityService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private ActivityMetricRepository activityMetricRepository;

    @Autowired
    private UserActivityTypeRepository userActivityTypeRepository;

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private ActivitySessionRepository activitySessionRepository;

    @PostConstruct
    public void initDefaultActivityTypes() {
        if (activityTypeRepository.count() == 0) {
            ActivityType running = new ActivityType();
            running.setName("Running");
            running.setDescription("Running activity");
            running.setDefault(true);
            activityTypeRepository.save(running);

            ActivityType cycling = new ActivityType();
            cycling.setName("Cycling");
            cycling.setDescription("Cycling activity");
            cycling.setDefault(true);
            activityTypeRepository.save(cycling);

            ActivityType yoga = new ActivityType();
            yoga.setName("Yoga");
            yoga.setDescription("Yoga activity");
            yoga.setDefault(true);
            activityTypeRepository.save(yoga);
        }
    }

    public List<ActivityType> getAllDefaultActivityTypes() {
        return activityTypeRepository.findByIsDefault(true);
    }

    public List<UserActivityType> getUserActivityTypes(Long userId) {
        return userActivityTypeRepository.findByUserId(userId);
    }

    public void addCustomActivityTypeForUser(Long userId, String name, String description, Set<Long> metricIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ActivityType customActivityType = new ActivityType();
        customActivityType.setName(name);
        customActivityType.setDescription(description);
        customActivityType.setDefault(false);

        Set<Metric> metrics = new HashSet<>(metricRepository.findAllById(metricIds));
        customActivityType.setMetrics(metrics);

        activityTypeRepository.save(customActivityType);

        UserActivityType userActivityType = new UserActivityType();
        userActivityType.setUser(user);
        userActivityType.setActivityType(customActivityType);

        userActivityTypeRepository.save(userActivityType);
    }

    public void createActivityGroupForUser(Long userId, String groupName, Set<Long> activityTypeIds ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        ActivityGroup activityGroup = new ActivityGroup();
        activityGroup.setUser(user);
        activityGroup.setName(groupName);

        Set<ActivityType> activityTypes = new HashSet<>(activityTypeRepository.findAllById(activityTypeIds));
        activityGroup.setActivityTypes(activityTypes);
        activityGroupRepository.save(activityGroup);
    }

    public void saveActivitySession(Long userId, Long activityTypeId, LocalDate date, Map<Long, String> metricValues) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        ActivityType activityType = activityTypeRepository.findById(activityTypeId).orElseThrow(() -> new IllegalArgumentException("Activity type not found"));

        ActivitySession activitySession = new ActivitySession();
        activitySession.setUser(user);
        activitySession.setActivityType(activityType);
        activitySession.setDate(date);

        Map<Metric, String> metrics = new HashMap<>();
        for (Map.Entry<Long, String> entry : metricValues.entrySet()) {
            Metric metric = metricRepository.findById(entry.getKey()).orElseThrow(() -> new IllegalArgumentException("Metric not found"));
            metrics.put(metric, entry.getValue());
        }

        activitySession.setMetrics(metrics);
        activitySessionRepository.save(activitySession);
    }











}
