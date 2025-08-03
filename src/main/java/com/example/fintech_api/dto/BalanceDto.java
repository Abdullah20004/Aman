package com.example.fintech_api.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class BalanceDto {
    private String dataType;
    private Date balanceDay;
    private Long txnCount;
    private Timestamp fromTime;
    private Timestamp toTime;
    private String executionTime;
}