package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.ActivityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityGroupRepository extends JpaRepository<ActivityGroup, Long> {
}
