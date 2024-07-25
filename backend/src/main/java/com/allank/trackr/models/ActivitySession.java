package com.allank.trackr.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;

@Entity
public class ActivitySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "activity_session_activity_types",
            joinColumns = @JoinColumn(name = "activity_session_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_type_id"))
    private List<ActivityType> activityTypes = new ArrayList<>();

    private String notes;

    @OneToMany(mappedBy = "activitySession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivitySessionMetric> activitySessionMetrics = new ArrayList<>();

    private Instant date;

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
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

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<ActivitySessionMetric> getActivitySessionMetrics() {
        return activitySessionMetrics;
    }

    public void setActivitySessionMetrics(List<ActivitySessionMetric> activitySessionMetrics) {
        this.activitySessionMetrics = activitySessionMetrics;
    }
}