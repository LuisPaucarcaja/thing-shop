package com.user_service.feature.user.repository;

import com.user_service.feature.user.entity.Role;
import com.user_service.feature.user.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
