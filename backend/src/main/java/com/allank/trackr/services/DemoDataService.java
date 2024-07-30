package com.allank.trackr.services;

import com.allank.trackr.models.*;
import com.allank.trackr.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.allank.trackr.models.Erole.ROLE_DEMO;

@Service
public class DemoDataService {

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private ActivitySessionRepository activitySessionRepository;

    @Autowired
    private ActivitySessionMetricRepository activitySessionMetricRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            initDemoUser();
        }
    }

    private void initDemoUser() {
        String hashedPassword = passwordEncoder.encode("password");
        User demoUser = new User();
        demoUser.setEmail("demo@demo.com");
        demoUser.setPassword(hashedPassword);

        Role demoRole = roleRepository.findByName(ROLE_DEMO).orElseThrow(() -> new RuntimeException("ROLE_DEMO not found"));
        Set<Role> demoRoles = new HashSet<>();
        demoRoles.add(demoRole);
        demoUser.setRoles(demoRoles);
        userRepository.save(demoUser);

        seedDemoUserData(demoUser);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void refreshDemoData() {
        User demoUser = userRepository.findByEmail("demo@demo.com").orElseThrow(() -> new RuntimeException("Demo user not found"));

        activitySessionMetricRepository.deleteByActivitySessionUser(demoUser);
        activitySessionRepository.deleteByUser(demoUser);
        activityGroupRepository.deleteByUser(demoUser);
        activityTypeRepository.deleteByUser(demoUser);

        seedDemoUserData(demoUser);
    }

    private void seedDemoUserData(User demoUser) {
        Metric distMetric = metricRepository.findByName("Distance");
        Metric timeMetric = metricRepository.findByName("Time");

        Unit kilometer = unitRepository.findByUnit("km");
        Unit minute = unitRepository.findByUnit("min");

        ActivityType running = new ActivityType();
        running.setName("Running");
        running.setDescription("Running");
        running.setDefault(true);
        running.setUser(demoUser);
        running.setMetrics(Arrays.asList(distMetric, timeMetric));
        activityTypeRepository.save(running);

        ActivityType cycling = new ActivityType();
        cycling.setName("Cycling");
        cycling.setDescription("Cycling");
        cycling.setDefault(true);
        cycling.setUser(demoUser);
        cycling.setMetrics(Arrays.asList(distMetric, timeMetric));
        activityTypeRepository.save(cycling);

        ActivityType yoga = new ActivityType();
        yoga.setName("Yoga");
        yoga.setDescription("Yoga");
        yoga.setDefault(true);
        yoga.setUser(demoUser);
        yoga.setMetrics(Collections.singletonList(timeMetric));
        activityTypeRepository.save(yoga);

        ActivityGroup activityGroup = new ActivityGroup();
        activityGroup.setName("Demo Group");
        activityGroup.setDescription("Group for demo user");
        activityGroup.setUser(demoUser);
        activityGroup.setActivityTypes(new HashSet<>(Arrays.asList(running, cycling, yoga)));
        activityGroupRepository.save(activityGroup);

        // Create and save sessions
        // Session 1
        ActivitySession session1 = new ActivitySession();
        session1.setUser(demoUser);
        session1.setNotes("Morning run");
        session1.setDate(Instant.now().minus(10, ChronoUnit.DAYS));
        session1.setActivityTypes(List.of(running));
        ActivitySession savedSession1 = activitySessionRepository.save(session1);

        ActivitySessionMetric session1Metric1 = createActivitySessionMetric(savedSession1, running, distMetric, kilometer, 5000);
        activitySessionMetricRepository.save(session1Metric1);
        ActivitySessionMetric session1Metric2 = createActivitySessionMetric(savedSession1, running, timeMetric, minute, 2400);
        activitySessionMetricRepository.save(session1Metric2);

        // Session 2
        ActivitySession session2 = new ActivitySession();
        session2.setUser(demoUser);
        session2.setNotes("Morning run");
        session2.setDate(Instant.now().minus(7, ChronoUnit.DAYS));
        session2.setActivityTypes(List.of(running));
        ActivitySession savedSession2 = activitySessionRepository.save(session2);

        ActivitySessionMetric session2Metric1 = createActivitySessionMetric(savedSession2, running, distMetric, kilometer, 5500);
        activitySessionMetricRepository.save(session2Metric1);
        ActivitySessionMetric session2Metric2 = createActivitySessionMetric(savedSession2, running, timeMetric, minute, 2160);
        activitySessionMetricRepository.save(session2Metric2);

        // Session 3
        ActivitySession session3 = new ActivitySession();
        session3.setUser(demoUser);
        session3.setNotes("Lunch run");
        session3.setDate(Instant.now().minus(5, ChronoUnit.DAYS));
        session3.setActivityTypes(List.of(running));
        ActivitySession savedSession3 = activitySessionRepository.save(session3);

        ActivitySessionMetric session3Metric1 = createActivitySessionMetric(savedSession3, running, distMetric, kilometer, 5800);
        activitySessionMetricRepository.save(session3Metric1);
        ActivitySessionMetric session3Metric2 = createActivitySessionMetric(savedSession3, running, timeMetric, minute, 2100);
        activitySessionMetricRepository.save(session3Metric2);

        // Session 4
        ActivitySession session4 = new ActivitySession();
        session4.setUser(demoUser);
        session4.setNotes("Morning run and yoga");
        session4.setDate(Instant.now().minus(3, ChronoUnit.DAYS));
        session4.setActivityTypes(List.of(running, yoga));
        ActivitySession savedSession4 = activitySessionRepository.save(session4);

        ActivitySessionMetric session4Metric1 = createActivitySessionMetric(savedSession4, running, distMetric, kilometer, 6000);
        activitySessionMetricRepository.save(session4Metric1);
        ActivitySessionMetric session4Metric2 = createActivitySessionMetric(savedSession4, running, timeMetric, minute, 1800);
        activitySessionMetricRepository.save(session4Metric2);
        ActivitySessionMetric session4Metric3 = createActivitySessionMetric(savedSession4, yoga, timeMetric, minute, 1800);
        activitySessionMetricRepository.save(session4Metric3);

        // Session 5
        ActivitySession session5 = new ActivitySession();
        session5.setUser(demoUser);
        session5.setNotes("Morning yoga");
        session5.setDate(Instant.now().minus(2, ChronoUnit.DAYS));
        session5.setActivityTypes(List.of(yoga));
        ActivitySession savedSession5 = activitySessionRepository.save(session5);

        ActivitySessionMetric session5Metric1 = createActivitySessionMetric(savedSession5, yoga, timeMetric, minute, 2100);
        activitySessionMetricRepository.save(session5Metric1);

        // Session 6
        ActivitySession session6 = new ActivitySession();
        session6.setUser(demoUser);
        session6.setNotes("Yoga practice");
        session6.setDate(Instant.now().minus(1, ChronoUnit.DAYS));
        session6.setActivityTypes(List.of(yoga));
        ActivitySession savedSession6 = activitySessionRepository.save(session6);

        ActivitySessionMetric session6Metric1 = createActivitySessionMetric(savedSession6, yoga, timeMetric, minute, 2100);
        activitySessionMetricRepository.save(session6Metric1);
    }

    @Transactional
    public void createUserWithDefaultActivityTypes(User user) {
        userRepository.save(user);

        Metric distMetric = metricRepository.findByName("Distance");
        Metric timeMetric = metricRepository.findByName("Time");

        ActivityType running = new ActivityType();
        running.setName("Running");
        running.setDescription("Running");
        running.setDefault(true);
        running.setUser(user);
        running.setMetrics(Arrays.asList(distMetric, timeMetric));
        activityTypeRepository.save(running);

        ActivityType cycling = new ActivityType();
        cycling.setName("Cycling");
        cycling.setDescription("Cycling");
        cycling.setDefault(true);
        cycling.setUser(user);
        cycling.setMetrics(Arrays.asList(distMetric, timeMetric));
        activityTypeRepository.save(cycling);

        ActivityType yoga = new ActivityType();
        yoga.setName("Yoga");
        yoga.setDescription("Yoga");
        yoga.setDefault(true);
        yoga.setUser(user);
        yoga.setMetrics(Collections.singletonList(timeMetric));
        activityTypeRepository.save(yoga);
    }

    private ActivitySessionMetric createActivitySessionMetric(ActivitySession session, ActivityType activityType, Metric metric, Unit unit, double value) {
        ActivitySessionMetric sessionMetric = new ActivitySessionMetric();
        sessionMetric.setActivitySession(session);
        sessionMetric.setActivityType(activityType);
        sessionMetric.setMetric(metric);
        sessionMetric.setUnit(unit);
        sessionMetric.setValue(value);
        return sessionMetric;
    }
}
