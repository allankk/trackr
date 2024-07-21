package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
