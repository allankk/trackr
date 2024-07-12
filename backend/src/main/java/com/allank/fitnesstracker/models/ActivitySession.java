package com.allank.fitnesstracker.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class ActivitySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "activity_type_id")
    private ActivityType activityType;

    @ElementCollection
    @CollectionTable(name = "activity_session_metrics", joinColumns = @JoinColumn(name = "activity_session_id"))
    @MapKeyJoinColumn(name = "metric_id")
    @Column(name = "value")
    private Map<Metric, String> metrics = new HashMap<>();

    private LocalDate date;

    public Map<Metric, String> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<Metric, String> metrics) {
        this.metrics = metrics;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}