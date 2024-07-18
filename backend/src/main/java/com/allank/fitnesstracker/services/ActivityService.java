package com.allank.fitnesstracker.services;

import com.allank.fitnesstracker.models.*;
import com.allank.fitnesstracker.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.allank.fitnesstracker.models.Erole.ROLE_USER;

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
    private ActivityMetricRepository activityMetricRepository;

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private ActivitySessionRepository activitySessionRepository;

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

        Set<Metric> metrics = new HashSet<>(metricRepository.findAllById(metricIds));
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
        Set<Metric> metrics = new HashSet<>(metricRepository.findAllById(metricIds));
        activityType.setMetrics(metrics);

        activityTypeRepository.save(activityType);
    }

    public void deleteUserActivityType(Long userId, Long activityTypeId) {
        ActivityType activityType = activityTypeRepository.findById(activityTypeId).orElseThrow(() -> new IllegalArgumentException("activity type not found"));

        if (activityType.getUser().getId().equals(userId)) {
            System.out.println("deleting activitytype");
            activityTypeRepository.delete(activityType);
        } else {
            throw new RuntimeException("Activity Type does not belong do the given user.");
        }
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

    @Autowired
    PasswordEncoder encoder;

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

        if (userRepository.count() == 0) {
            User user = new User("test@test.com",  encoder.encode("password"));

            if (roleRepository.count() == 0) {
                Role role = new Role(ROLE_USER);
                roleRepository.save(role);
            }

            Role role = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new EntityNotFoundException("role not found"));
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setRoles(roles);

            userRepository.save(user);
        }

        User user = userRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("user not found"));

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
            running.setUser(user);
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
            cycling.setUser(user);
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
            yoga.setUser(user);
            activityTypeRepository.save(yoga);
        }
    }
}
