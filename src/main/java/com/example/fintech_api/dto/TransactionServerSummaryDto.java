package com.example.fintech_api.dto;

import lombok.Data;

@Data
public class TransactionServerSummaryDto {
    private String usename;
    private Long txn1Ideal;
    private Long txn2Ideal;
    private Long txn3Ideal;
    private Long txn1Active;
    private Long txn2Active;
    private Long txn3Active;
}