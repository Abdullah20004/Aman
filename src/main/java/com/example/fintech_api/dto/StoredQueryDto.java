package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class StoredQueryDto {
    private Long id;
    private String queryCode;
    private String queryName;
    private String description;
    private String sqlCode;
    private Boolean isEnabled;
}