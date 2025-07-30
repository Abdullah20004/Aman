package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class SessionCountDto {
    private String usename;
    private String clientAddr;
    private String state;
    private String applicationName;
    private Long sessionsCount;
}