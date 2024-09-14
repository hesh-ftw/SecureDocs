package com.secure.docs.repository;

import com.secure.docs.model.AppRole;
import com.secure.docs.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
