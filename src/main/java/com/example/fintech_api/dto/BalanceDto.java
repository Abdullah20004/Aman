package com.example.fintech_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BalanceDto {
    private String dataType;
    private LocalDate balanceDay;
    private Long txnCount;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private Long executionTime;
}