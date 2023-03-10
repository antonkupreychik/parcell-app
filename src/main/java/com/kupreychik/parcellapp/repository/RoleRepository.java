package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Role} entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by name
     *
     * @param name role name
     * @return role by name
     */
    Optional<Role> findByName(String name);
}
