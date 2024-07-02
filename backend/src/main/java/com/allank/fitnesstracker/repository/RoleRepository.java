package com.allank.fitnesstracker.repository;

import com.allank.fitnesstracker.models.Erole;
import com.allank.fitnesstracker.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Erole name);
}
