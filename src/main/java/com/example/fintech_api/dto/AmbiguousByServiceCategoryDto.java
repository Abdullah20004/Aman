package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class AmbiguousByServiceCategoryDto {
    private String ambiguousFlag;
    private String serviceCategory;
    private Long count;
}