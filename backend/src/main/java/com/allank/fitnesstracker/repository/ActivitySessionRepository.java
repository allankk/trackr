package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.ActivitySession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitySessionRepository extends JpaRepository<ActivitySession, Long> {
}
