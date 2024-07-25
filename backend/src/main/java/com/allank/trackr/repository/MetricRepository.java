package com.allank.trackr.repository;

import com.allank.trackr.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    Metric findByName(String name);
}
