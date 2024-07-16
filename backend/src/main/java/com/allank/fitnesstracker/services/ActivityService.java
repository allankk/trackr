package com.allank.fitnesstracker.services;

import com.allank.fitnesstracker.models.*;
import com.allank.fitnesstracker.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
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

    public List<ActivityType> getAllDefaultActivityTypes() {
        return activityTypeRepository.findByIsDefault(true);
    }

    public List<ActivityType> getUserActivityTypes(Long userId) {
        List<UserActivityType> userActivityTypes =  userActivityTypeRepository.findByUserId(userId);

        return userActivityTypes.stream().map(UserActivityType::getActivityType).toList();
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

    public List<Metric> getAllMetrics() {
        return metricRepository.findAll();
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

    @PostConstruct
    public void initDefaultActivityTypes() {
        if (metricRepository.count() == 0) {
            Metric distKm = new Metric();
            distKm.setName("Distance");
            distKm.setUnit("km");
            metricRepository.save(distKm);

            Metric distM = new Metric();
            distM.setName("Distance");
            distM.setUnit("m");
            metricRepository.save(distM);

            Metric timeH = new Metric();
            timeH.setName("Time");
            timeH.setUnit("h");
            metricRepository.save(timeH);

            Metric timeM = new Metric();
            timeM.setName("Time");
            timeM.setUnit("m");
            metricRepository.save(timeM);

            Metric timeS = new Metric();
            timeS.setName("Time");
            timeS.setUnit("s");
            metricRepository.save(timeS);

            Metric sets = new Metric();
            sets.setName("Sets");
            sets.setUnit("Sets");
            metricRepository.save(sets);

            Metric reps = new Metric();
            reps.setName("Reps");
            reps.setUnit("Reps");
            metricRepository.save(reps);
        }

        Metric distKm = metricRepository.findByNameAndUnit("Distance", "km");
        Metric distM = metricRepository.findByNameAndUnit("Distance", "m");
        Metric timeH = metricRepository.findByNameAndUnit("Time", "H");
        Metric timeM = metricRepository.findByNameAndUnit("Time", "m");

        if (activityTypeRepository.count() == 0) {
            ActivityType running = new ActivityType();
            running.setName("Running");
            running.setDescription("Running");
            Set<Metric> runningMetrics = new HashSet<>();
            runningMetrics.add(distKm);
            runningMetrics.add(distM);
            runningMetrics.add(timeH);
            runningMetrics.add(timeM);
            running.setMetrics(runningMetrics);
            running.setDefault(true);
            activityTypeRepository.save(running);

            ActivityType cycling = new ActivityType();
            cycling.setName("Cycling");
            cycling.setDescription("Cycling");
            Set<Metric> cyclingMetrics = new HashSet<>();
            cyclingMetrics.add(distKm);
            cyclingMetrics.add(distM);
            cyclingMetrics.add(timeH);
            cyclingMetrics.add(timeM);
            cycling.setMetrics(cyclingMetrics);
            cycling.setDefault(true);
            activityTypeRepository.save(cycling);

            ActivityType yoga = new ActivityType();
            yoga.setName("Yoga");
            yoga.setDescription("Yoga");
            Set<Metric> yogaMetrics = new HashSet<>();
            yogaMetrics.add(distKm);
            yogaMetrics.add(distM);
            yogaMetrics.add(timeH);
            yogaMetrics.add(timeM);
            yoga.setMetrics(yogaMetrics);
            yoga.setDefault(true);
            activityTypeRepository.save(yoga);
        }
    }
}
