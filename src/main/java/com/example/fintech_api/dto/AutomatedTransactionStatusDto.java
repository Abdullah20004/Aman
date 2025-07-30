package com.example.fintech_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AutomatedTransactionStatusDto {
    private LocalDate dbDate;
    private String serviceCode;
    private String atStatus;
    private String transactionStatus;
    private Long userACount;
    private Long userBCount;
    private Double totalValue;
    private Long txnCount;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}