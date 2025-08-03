package com.example.fintech_api.repository;

import com.example.fintech_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM gle_prod_config.users WHERE login_id_1 = :loginId", nativeQuery = true)
    List<User> findByLoginId(@Param("loginId") String loginId);
}