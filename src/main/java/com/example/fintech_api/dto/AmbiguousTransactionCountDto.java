package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class AmbiguousTransactionCountDto {
    private String serviceTransactionType;
    private Long isAmbiguous;
    private Long notAmbiguous;
}