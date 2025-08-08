package com.microservice_auth.feature.user.repository;


import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {



    Optional<User> findByEmail(String email);


    @Modifying
    @Query("UPDATE User u SET u.accountStatus = :status WHERE u.id = :id")
    void updateAccountStatus(@Param("id") Long id, @Param("status") AccountStatus status);


}
