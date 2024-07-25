package com.allank.trackr.repository;

import com.allank.trackr.models.ActivityGroup;
import com.allank.trackr.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {
    List<ActivityGroup> findByUserId(Long userId);
    List<ActivityGroup> findAllByActivityTypesContains(ActivityType activityType);
}
