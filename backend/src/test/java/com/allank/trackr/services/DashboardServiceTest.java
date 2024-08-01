package com.allank.trackr.services;

import com.allank.trackr.dto.response.dashboard.ResultsDto;
import com.allank.trackr.models.ActivitySession;
import com.allank.trackr.models.ActivitySessionMetric;
import com.allank.trackr.repository.ActivitySessionMetricRepository;
import com.allank.trackr.repository.ActivitySessionRepository;
import com.allank.trackr.models.Unit;
import com.allank.trackr.repository.UnitRepository;
import com.allank.trackr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivitySessionRepository activitySessionRepository;

    @Mock
    private ActivitySessionMetricRepository activitySessionMetricRepository;

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSessionDates() {
    }

    @Test
    void getResults() {
        Long userId = 1L;
        Long activityTypeId = 1L;
        Long metricId = 1L;
        Long unitId = 1L;

        ActivitySession session1 = new ActivitySession();
        session1.setDate(Instant.parse("2023-01-01T00:00:00Z"));
        ActivitySession session2 = new ActivitySession();
        session2.setDate(Instant.parse("2023-02-01T00:00:00Z"));

        ActivitySessionMetric metric1 = new ActivitySessionMetric();
        metric1.setActivitySession(session1);
        metric1.setValue(100.0);

        ActivitySessionMetric metric2 = new ActivitySessionMetric();
        metric2.setActivitySession(session2);
        metric2.setValue(200.0);

        List<ActivitySessionMetric> activitySessionMetrics = List.of(metric1, metric2);

        Unit unit = new Unit();
        unit.setConversionFactor(10.0);

        when(activitySessionMetricRepository.findByActivitySessionUserIdAndActivityTypeIdAndMetricId(userId, activityTypeId, metricId)).thenReturn(activitySessionMetrics);
        when(unitRepository.findById(unitId)).thenReturn(Optional.of(unit));

        ResultsDto results = dashboardService.getResults(userId, activityTypeId, metricId, unitId);

        assertEquals(2, results.dates().size());
        assertEquals(Instant.parse("2023-01-01T00:00:00Z"), results.dates().get(0));
        assertEquals(Instant.parse("2023-02-01T00:00:00Z"), results.dates().get(1));

        assertEquals(2, results.results().size());
        assertEquals(10.0, results.results().get(0));
        assertEquals(20.0, results.results().get(1));

        verify(activitySessionMetricRepository, times(1)).findByActivitySessionUserIdAndActivityTypeIdAndMetricId(userId, activityTypeId, metricId);
        verify(unitRepository, times(1)).findById(unitId);
    }
}