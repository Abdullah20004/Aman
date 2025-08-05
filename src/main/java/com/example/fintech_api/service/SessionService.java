package com.example.fintech_api.service;

import com.example.fintech_api.dto.StoredQueryDto;
import com.example.fintech_api.model.StoredQuery;
import com.example.fintech_api.repository.SessionRepository;
import com.example.fintech_api.repository.StoredQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SessionService {
    private final StoredQueryRepository storedQueryRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GeminiService geminiService;

    @Autowired
    public SessionService(StoredQueryRepository storedQueryRepository,
                          NamedParameterJdbcTemplate jdbcTemplate,
                          GeminiService geminiService) {
        this.storedQueryRepository = storedQueryRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.geminiService = geminiService;
    }

    @Transactional
    public StoredQueryDto createStoredQuery(StoredQueryDto dto) throws Exception {
        if (dto.getQueryName() == null || dto.getQueryName().isEmpty()) {
            throw new IllegalArgumentException("Query name is required");
        }
        if (dto.getDescription() == null || dto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        StoredQuery query = new StoredQuery();
        query.setQueryCode(UUID.randomUUID().toString());
        query.setQueryName(dto.getQueryName());
        query.setDescription(dto.getDescription());
        query.setIsEnabled(true);
        query.setCreatedOn(LocalDateTime.now());

        String sqlCode = dto.getSqlCode();
        if (sqlCode == null || sqlCode.isEmpty()) {
            sqlCode = geminiService.generateSqlFromDescription(dto.getDescription());
        }
        validateSql(sqlCode);
        query.setSqlCode(sqlCode);

        StoredQuery savedQuery = storedQueryRepository.save(query);
        StoredQueryDto result = new StoredQueryDto();
        result.setId(savedQuery.getId());
        result.setQueryCode(savedQuery.getQueryCode());
        result.setQueryName(savedQuery.getQueryName());
        result.setDescription(savedQuery.getDescription());
        result.setSqlCode(savedQuery.getSqlCode());
        result.setIsEnabled(savedQuery.getIsEnabled());
        return result;
    }

    @Transactional
    public StoredQueryDto toggleQueryStatus(String queryCode, boolean isEnabled) {
        StoredQuery query = storedQueryRepository.findByQueryCode(queryCode)
                .orElseThrow(() -> new IllegalArgumentException("Query not found: " + queryCode));
        query.setIsEnabled(isEnabled);
        StoredQuery savedQuery = storedQueryRepository.save(query);

        StoredQueryDto result = new StoredQueryDto();
        result.setId(savedQuery.getId());
        result.setQueryCode(savedQuery.getQueryCode());
        result.setQueryName(savedQuery.getQueryName());
        result.setDescription(savedQuery.getDescription());
        result.setSqlCode(savedQuery.getSqlCode());
        result.setIsEnabled(savedQuery.getIsEnabled());
        return result;
    }

    public List<Map<String, Object>> executeStoredQuery(String queryCode, Map<String, Object> params) {
        StoredQuery query = storedQueryRepository.findByQueryCodeAndEnabled(queryCode)
                .orElseThrow(() -> new IllegalArgumentException("Query not found or disabled: " + queryCode));
        validateSql(query.getSqlCode());
        return jdbcTemplate.queryForList(query.getSqlCode(), params);
    }

    private void validateSql(String sql) throws IllegalArgumentException {
        String lowerSql = sql.toLowerCase().trim();
        if (!lowerSql.startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT queries are allowed");
        }
        if (lowerSql.contains("insert") || lowerSql.contains("update") ||
                lowerSql.contains("delete") || lowerSql.contains("drop")) {
            throw new IllegalArgumentException("SQL query must be a SELECT statement");
        }
    }
}