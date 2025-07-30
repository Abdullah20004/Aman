package com.example.fintech_api.controller;

import com.example.fintech_api.dto.PidDto;
import com.example.fintech_api.dto.SessionCountDto;
import com.example.fintech_api.dto.TransactionServerSummaryDto;
import com.example.fintech_api.dto.TransactionServerSummaryTwoDto;
import com.example.fintech_api.dto.AmbiguousTransactionCountDto;
import com.example.fintech_api.dto.AmbiguousByServiceCategoryDto;
import com.example.fintech_api.dto.AmbiguousByServiceCategoryStatusDto;
import com.example.fintech_api.dto.SystemExceptionDto;
import com.example.fintech_api.dto.BalanceDto;
import com.example.fintech_api.dto.ProcessorStatusDto;
import com.example.fintech_api.dto.AutomatedTransactionStatusDto;
import com.example.fintech_api.model.StatusCode;
import com.example.fintech_api.model.User;
import com.example.fintech_api.service.SessionService;
import com.example.fintech_api.service.StatusCodeService;
import com.example.fintech_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class FintechController {
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

    @GetMapping("/sessions")
    public List<SessionCountDto> getSessionCounts() {
        return sessionService.getSessionCounts();
    }

    @GetMapping("/sessions-with-app")
    public List<SessionCountDto> getSessionCountsWithAppName() {
        return sessionService.getSessionCountsWithAppName();
    }

    @GetMapping("/pids")
    public List<PidDto> getPids(@RequestParam String usename,
                                @RequestParam String clientAddr,
                                @RequestParam String state) {
        return sessionService.getPidsByUserAndClient(usename, clientAddr, state);
    }

    @GetMapping("/transaction-server-summary")
    public List<TransactionServerSummaryDto> getTransactionServerSummary(@RequestParam String usename) {
        return sessionService.getTransactionServerSummary(usename);
    }

    @GetMapping("/transaction-server-summary-two")
    public CompletableFuture<List<TransactionServerSummaryTwoDto>> getTransactionServerSummaryTwo(@RequestParam String usename) {
        return sessionService.getTransactionServerSummaryTwo(usename);
    }

    @GetMapping("/ambiguous-transaction-count")
    public CompletableFuture<List<AmbiguousTransactionCountDto>> getAmbiguousTransactionCount() {
        return sessionService.getAmbiguousTransactionCount();
    }

    @GetMapping("/ambiguous-by-service-category")
    public CompletableFuture<List<AmbiguousByServiceCategoryDto>> getAmbiguousByServiceCategory() {
        return sessionService.getAmbiguousByServiceCategory();
    }

    @GetMapping("/ambiguous-by-service-category-status")
    public CompletableFuture<List<AmbiguousByServiceCategoryStatusDto>> getAmbiguousByServiceCategoryStatus() {
        return sessionService.getAmbiguousByServiceCategoryStatus();
    }

    @GetMapping("/system-exceptions")
    public CompletableFuture<List<SystemExceptionDto>> getSystemExceptions() {
        return sessionService.getSystemExceptions();
    }

    @GetMapping("/closing-balances")
    public CompletableFuture<List<BalanceDto>> getClosingBalances() {
        return sessionService.getClosingBalances();
    }

    @GetMapping("/opening-balances")
    public CompletableFuture<List<BalanceDto>> getOpeningBalances() {
        return sessionService.getOpeningBalances();
    }

    @GetMapping("/processor-status")
    public CompletableFuture<List<ProcessorStatusDto>> getProcessorStatus() {
        return sessionService.getProcessorStatus();
    }

    @GetMapping("/automated-transaction-status")
    public CompletableFuture<List<AutomatedTransactionStatusDto>> getAutomatedTransactionStatus() {
        return sessionService.getAutomatedTransactionStatus();
    }

    @GetMapping("/partition-status")
    public CompletableFuture<String> getPartitionStatus() {
        return sessionService.getPartitionStatus();
    }
}