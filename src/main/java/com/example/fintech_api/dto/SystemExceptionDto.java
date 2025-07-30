package com.example.fintech_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SystemExceptionDto {
    private LocalDate exceptionDate;
    private String moduleName;
    private String exceptionMessage;
    private Long txnCount;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
}