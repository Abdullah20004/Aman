package com.example.fintech_api.repository;

import com.example.fintech_api.model.StoredQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoredQueryRepository extends JpaRepository<StoredQuery, Long> {
    Optional<StoredQuery> findByQueryCode(String queryCode);

    @Query("SELECT sq FROM StoredQuery sq WHERE sq.queryCode = :queryCode AND sq.isEnabled = true")
    Optional<StoredQuery> findByQueryCodeAndEnabled(@Param("queryCode") String queryCode);
}