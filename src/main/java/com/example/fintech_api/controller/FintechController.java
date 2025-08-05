package com.example.fintech_api.controller;

import com.example.fintech_api.dto.StoredQueryDto;
import com.example.fintech_api.model.StatusCode;
import com.example.fintech_api.model.User;
import com.example.fintech_api.service.SessionService;
import com.example.fintech_api.service.StatusCodeService;
import com.example.fintech_api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FintechController {
    private static final Logger logger = LoggerFactory.getLogger(FintechController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private StatusCodeService statusCodeService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam String loginId) {
        return userService.getUsersByLoginId(loginId);
    }

    @GetMapping("/status-codes")
    public List<StatusCode> getStatusCodes() {
        return statusCodeService.getStatusCodesByPattern("%int_check_privilege_profile.0002%");
    }

    @PostMapping("/stored-queries")
    public ResponseEntity<StoredQueryDto> createStoredQuery(@RequestBody StoredQueryDto dto) {
        try {
            StoredQueryDto result = sessionService.createStoredQuery(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating stored query: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/stored-queries/{queryCode}/toggle")
    public ResponseEntity<StoredQueryDto> toggleQueryStatus(@PathVariable String queryCode,
                                                            @RequestParam boolean isEnabled) {
        try {
            StoredQueryDto result = sessionService.toggleQueryStatus(queryCode, isEnabled);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("Error toggling query status: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/stored-queries/{queryCode}/execute")
    public ResponseEntity<List<Map<String, Object>>> executeStoredQuery(
            @PathVariable String queryCode,
            @RequestBody Map<String, Object> params) {
        try {
            List<Map<String, Object>> results = sessionService.executeStoredQuery(queryCode, params);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            logger.error("Error executing query: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error executing query: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}