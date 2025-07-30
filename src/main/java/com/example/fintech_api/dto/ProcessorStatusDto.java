package com.example.fintech_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessorStatusDto {
    private String processorId;
    private String processorRunningStatus;
    private LocalDateTime lastRun;
    private Long transactionsCount;
    private Long lastRunSinceSeconds;
    private Long nextRunInSeconds;
}