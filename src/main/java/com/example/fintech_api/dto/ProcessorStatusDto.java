package com.example.fintech_api.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProcessorStatusDto {
    private String processorId;
    private String processorRunningStatus;
    private Timestamp lastRun;
    private Long transactionsCount;
    private Long lastRunSinceSeconds;
    private Long nextRunInSeconds;
}