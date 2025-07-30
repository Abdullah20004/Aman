package com.example.fintech_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StatusCode {
    @Id
    private String statusCode;
    private String description;
    // Add other fields as per your status_codes table schema
}