package com.example.fintech_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "stored_queries", schema = "gle_prod_config")
@Data
public class StoredQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query_code", unique = true, nullable = false)
    private String queryCode;

    @Column(name = "query_name", nullable = false)
    private String queryName;

    @Column(name = "description")
    private String description;

    @Column(name = "sql_code", nullable = false)
    private String sqlCode;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
}