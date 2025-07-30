package com.example.fintech_api.repository;

import com.example.fintech_api.model.StatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatusCodeRepository extends JpaRepository<StatusCode, String> {
    @Query(value = "SELECT * FROM status_codes WHERE status_code ILIKE :pattern", nativeQuery = true)
    List<StatusCode> findByStatusCodePattern(@Param("pattern") String pattern);
}