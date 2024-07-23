package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.ActivitySessionMetric;
import com.allank.fitnesstracker.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySessionMetricRepository extends JpaRepository<ActivitySessionMetric, Long> {
    List<ActivitySessionMetric> findAllByActivityType(ActivityType activityType);
}
