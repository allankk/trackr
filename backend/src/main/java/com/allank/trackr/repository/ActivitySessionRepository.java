package com.allank.trackr.repository;

import com.allank.trackr.models.ActivitySession;
import com.allank.trackr.models.ActivityType;
import com.allank.trackr.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ActivitySessionRepository extends JpaRepository<ActivitySession, Long> {
    List<ActivitySession> findByUser(User user);
    List<ActivitySession> findAllByActivityTypesContains(ActivityType activityType);
    List<ActivitySession> findByDateAfterAndDateBefore(Instant startOfDay, Instant endOfDay);
    List<ActivitySession> findByDate(Instant date);
    void deleteByUser(User user);
}
