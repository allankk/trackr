package com.allank.trackr.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean isDefault;

    @ManyToMany
    @JoinTable(
            name = "activity_type_metrics",
            joinColumns = @JoinColumn(name = "activity_type_id"),
            inverseJoinColumns = @JoinColumn(name = "metric_id"))
    private List<Metric> metrics = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(mappedBy = "activityTypes")
    private List<ActivitySession> activitySessions = new ArrayList<>();

    public ActivityType() {}

    public ActivityType(String name, String description, boolean isDefault, User user, List<Metric> metrics) {
        this.name = name;
        this.description = description;
        this.isDefault = isDefault;
        this.user = user;
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public List<ActivitySession> getActivitySessions() {
        return activitySessions;
    }

    public void setActivitySessions(List<ActivitySession> activitySessions) {
        this.activitySessions = activitySessions;
    }
}
