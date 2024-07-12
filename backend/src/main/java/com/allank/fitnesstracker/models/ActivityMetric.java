package com.allank.fitnesstracker.models;

import jakarta.persistence.*;

@Entity
public class ActivityMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @ManyToOne
    @JoinColumn(name = "metric_id")
    private Metric metric;
}

