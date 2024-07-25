package com.allank.trackr.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class ActivityGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "activity_group_activity_types",
            joinColumns = @JoinColumn(name = "activity_group_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_type_id"))
    private Set<ActivityType> activityTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Set<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(Set<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }
}