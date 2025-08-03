package com.example.fintech_api.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class AutomatedTransactionStatusDto {
    private Date dbDate;
    private String serviceCode;
    private String atStatus;
    private String transactionStatus;
    private Long userACount;
    private Long userBCount;
    private Double totalValue;
    private Long txnCount;
    private Timestamp fromTime;
    private Timestamp toTime;
}