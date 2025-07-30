package com.example.fintech_api.service;

import com.example.fintech_api.model.StatusCode;
import com.example.fintech_api.repository.StatusCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusCodeService {
    @Autowired
    private StatusCodeRepository statusCodeRepository;

    public List<StatusCode> getStatusCodesByPattern(String pattern) {
        return statusCodeRepository.findByStatusCodePattern(pattern);
    }
}