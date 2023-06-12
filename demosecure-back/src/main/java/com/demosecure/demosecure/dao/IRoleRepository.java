package com.demosecure.demosecure.dao;


import com.demosecure.demosecure.model.RoleEntity;
import com.demosecure.demosecure.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleName name);
}