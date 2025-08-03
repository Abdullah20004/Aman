package com.example.fintech_api.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class SystemExceptionDto {
    private Date exceptionDate;
    private String moduleName;
    private String exceptionMessage;
    private Long txnCount;
    private Timestamp fromTime;
    private Timestamp toTime;
}