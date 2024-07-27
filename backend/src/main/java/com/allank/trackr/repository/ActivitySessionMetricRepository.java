package com.allank.trackr.repository;

import com.allank.trackr.models.ActivitySessionMetric;
import com.allank.trackr.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySessionMetricRepository extends JpaRepository<ActivitySessionMetric, Long> {
    List<ActivitySessionMetric> findAllByActivityType(ActivityType activityType);
    List<ActivitySessionMetric> findByActivitySessionUserIdAndActivityTypeIdAndMetricId(Long userId, Long activityTypeId, Long metricId);
}
