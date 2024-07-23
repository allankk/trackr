package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.ActivitySession;
import com.allank.fitnesstracker.models.ActivityType;
import com.allank.fitnesstracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySessionRepository extends JpaRepository<ActivitySession, Long> {
    List<ActivitySession> findByUser(User user);
    List<ActivitySession> findAllByActivityTypesContains(ActivityType activityType);
}
