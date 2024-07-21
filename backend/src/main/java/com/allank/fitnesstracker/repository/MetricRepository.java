package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    Metric findByName(String name);
}
