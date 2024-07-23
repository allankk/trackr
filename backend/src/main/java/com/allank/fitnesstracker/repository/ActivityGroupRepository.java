package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.ActivityGroup;
import com.allank.fitnesstracker.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {
    List<ActivityGroup> findByUserId(Long userId);
    List<ActivityGroup> findAllByActivityTypesContains(ActivityType activityType);
}
