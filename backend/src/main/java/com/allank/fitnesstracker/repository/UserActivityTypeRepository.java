package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.UserActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityTypeRepository extends JpaRepository<UserActivityType, Long> {
    List<UserActivityType> findByUserId(Long userId);
}
