package com.allank.fitnesstracker.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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
    private Set<Metric> metrics = new HashSet<>();

    @OneToMany(mappedBy = "activityType", cascade = CascadeType.ALL)
    private Set<UserActivityType> userActivityTypes = new HashSet<>();

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

    public Set<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Set<Metric> metrics) {
        this.metrics = metrics;
    }

    public Set<UserActivityType> getUserActivityTypes() {
        return userActivityTypes;
    }

    public void setUserActivityTypes(Set<UserActivityType> userActivityTypes) {
        this.userActivityTypes = userActivityTypes;
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
}
