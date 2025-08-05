package com.example.fintech_api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepositoryImpl implements SessionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}