package com.allank.trackr.repository;

import com.allank.trackr.models.ActivityType;
import com.allank.trackr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    List<ActivityType> findByIsDefault(boolean isDefault);
    List<ActivityType> findByUserId(Long userId);
    void deleteByUser(User user);
}
