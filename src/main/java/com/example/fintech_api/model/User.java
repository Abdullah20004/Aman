package com.example.fintech_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    private Long id;
    private String loginId1;
    private String username;
    // Add other fields as per your users table schema
}