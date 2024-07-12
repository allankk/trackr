package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.UserActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityTypeRepository extends JpaRepository<UserActivityType, Long> {
}
