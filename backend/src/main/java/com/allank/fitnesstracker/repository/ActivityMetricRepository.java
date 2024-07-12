package com.allank.fitnesstracker.repository;


import com.allank.fitnesstracker.models.ActivityMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMetricRepository extends JpaRepository<ActivityMetric, Long> {
}
