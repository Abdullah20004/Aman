package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class AmbiguousByServiceCategoryStatusDto {
    private String ambiguousFlag;
    private String serviceCategory;
    private Long successCount;
    private Long failedCount;
}