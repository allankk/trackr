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

    private String notes;

    @ElementCollection
    @CollectionTable(name = "activity_session_metrics", joinColumns = @JoinColumn(name = "activity_session_id"))
    @MapKeyJoinColumn(name = "metric_id")
    @Column(name = "value")
    private Map<Metric, Double> metrics = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "activity_session_units", joinColumns = @JoinColumn(name = "activity_session_id"))
    @MapKeyJoinColumn(name = "metric_id")
    @Column(name = "user_unit")
    private Map<Metric, String> userUnits = new HashMap<>();

    private LocalDate date;

    public Map<Metric, Double> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<Metric, Double> metrics) {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<Metric, String> getUserUnits() {
        return userUnits;
    }

    public void setUserUnits(Map<Metric, String> userUnits) {
        this.userUnits = userUnits;
    }
}