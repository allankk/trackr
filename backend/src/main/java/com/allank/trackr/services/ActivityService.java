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

            // Remove the activity type from all associated activity sessions
            List<ActivitySession> sessions = activitySessionRepository.findAllByActivityTypesContains(activityType);
            for (ActivitySession session : sessions) {
                session.getActivityTypes().remove(activityType);
            }
            activitySessionRepository.saveAll(sessions);

            // Remove the activity type from all associated activity session metrics
            List<ActivitySessionMetric> metrics = activitySessionMetricRepository.findAllByActivityType(activityType);
            activitySessionMetricRepository.deleteAll(metrics);

            // Remove the activity type from all associated activity groups
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

    @Autowired
    PasswordEncoder encoder;

    @PostConstruct
    public void initDefaultActivityTypes()
    {
        if (metricRepository.count() == 0) {
            Metric distMetric = new Metric();
            distMetric.setName("Distance");
            distMetric.setStandardUnit("m");

            Unit kilometer = new Unit();
            kilometer.setUnit("km");
            kilometer.setName("kilometer");
            kilometer.setConversionFactor(1000);
            distMetric.addUnit(kilometer);

            Unit meter = new Unit();
            meter.setUnit("m");
            meter.setName("meter");
            meter.setName("meter");
            meter.setConversionFactor(1);
            distMetric.addUnit(meter);

            metricRepository.save(distMetric);

            Metric timeMetric = new Metric();
            timeMetric.setName("Time");
            timeMetric.setStandardUnit("s");

            Unit seconds = new Unit();
            seconds.setUnit("sec");
            seconds.setName("second");
            seconds.setConversionFactor(1);
            timeMetric.addUnit(seconds);

            Unit minutes = new Unit();
            minutes.setUnit("min");
            minutes.setName("minute");
            minutes.setConversionFactor(60);
            timeMetric.addUnit(minutes);

            Unit hours = new Unit();
            hours.setUnit("h");
            hours.setName("hour");
            hours.setConversionFactor(3600);
            timeMetric.addUnit(hours);

            metricRepository.save(timeMetric);

            Metric setMetric = new Metric();
            setMetric.setName("Sets");
            setMetric.setStandardUnit("sets");

            Unit sets = new Unit();
            sets.setUnit("set");
            sets.setName("set");
            sets.setConversionFactor(1);
            setMetric.addUnit(sets);

            metricRepository.save(setMetric);

            Metric repMetric = new Metric();
            repMetric.setName("Reps");
            repMetric.setStandardUnit("reps");
            Unit reps = new Unit();
            reps.setUnit("rep");
            reps.setName("rep");
            reps.setConversionFactor(1);
            repMetric.addUnit(reps);

            metricRepository.save(repMetric);
        }

        Metric distMetric = metricRepository.findByName("Distance");
        Metric timeMetric = metricRepository.findByName("Time");

        if (userRepository.count() == 0) {
            User user = new User("test@test.com",  encoder.encode("password"));

            if (roleRepository.count() == 0) {
                Role role = new Role(ROLE_USER);
                Role demoRole = new Role(ROLE_DEMO);
                roleRepository.save(role);
                roleRepository.save(demoRole);
            }

            Role role = roleRepository.findByName(ROLE_USER).orElseThrow(() -> new EntityNotFoundException("role not found"));
            Set<Role> roles = new HashSet<>();
            roles.add(role);

            user.setRoles(roles);

            userRepository.save(user);

            User demoUser = new User("demo@demo.com",  encoder.encode("password"));

            Role demoRole = roleRepository.findByName(ROLE_DEMO).orElseThrow(() -> new EntityNotFoundException("role not found"));
            Set<Role> demoRoles = new HashSet<>();
            demoRoles.add(demoRole);
            demoUser.setRoles(demoRoles);
            userRepository.save(demoUser);
        }

        User user = userRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("user not found"));

        if (activityTypeRepository.count() == 0) {
            ActivityType running = new ActivityType();
            running.setName("Running");
            running.setDescription("Running");
            List<Metric> runningMetrics = new ArrayList<>();
            runningMetrics.add(distMetric);
            runningMetrics.add(timeMetric);
            running.setMetrics(runningMetrics);
            running.setDefault(true);
            running.setUser(user);
            activityTypeRepository.save(running);

            ActivityType cycling = new ActivityType();
            cycling.setName("Cycling");
            cycling.setDescription("Cycling");
            List<Metric> cyclingMetrics = new ArrayList<>();
            cyclingMetrics.add(distMetric);
            cyclingMetrics.add(timeMetric);
            cycling.setMetrics(cyclingMetrics);
            cycling.setDefault(true);
            cycling.setUser(user);
            activityTypeRepository.save(cycling);

            ActivityType yoga = new ActivityType();
            yoga.setName("Yoga");
            yoga.setDescription("Yoga");
            List<Metric> yogaMetrics = new ArrayList<>();
            yogaMetrics.add(timeMetric);
            yoga.setMetrics(yogaMetrics);
            yoga.setDefault(true);
            yoga.setUser(user);
            activityTypeRepository.save(yoga);
        }
    }
}
