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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        logger.info("Fetching PIDs for usename: {}, clientAddr: {}, state: {}", usename, clientAddr, state);
        List<PidDto> pids = sessionService.getPidsByUserAndClient(usename, clientAddr, state);
        logger.info("Found {} PIDs", pids.size());
        return pids;
    }

    @GetMapping("/transaction-server-summary")
    public List<TransactionServerSummaryDto> getTransactionServerSummary(@RequestParam String usename) {
        return sessionService.getTransactionServerSummary(usename);
    }

    @GetMapping("/transaction-server-summary-two")
    public List<TransactionServerSummaryTwoDto> getTransactionServerSummaryTwo(@RequestParam String usename) {
        return sessionService.getTransactionServerSummaryTwo(usename);
    }

    @GetMapping("/ambiguous-transaction-count")
    public List<AmbiguousTransactionCountDto> getAmbiguousTransactionCount(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getAmbiguousTransactionCount(startDate, endDate);
    }

    @GetMapping("/ambiguous-by-service-category")
    public List<AmbiguousByServiceCategoryDto> getAmbiguousByServiceCategory(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getAmbiguousByServiceCategory(startDate, endDate);
    }

    @GetMapping("/ambiguous-by-service-category-status")
    public List<AmbiguousByServiceCategoryStatusDto> getAmbiguousByServiceCategoryStatus(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getAmbiguousByServiceCategoryStatus(startDate, endDate);
    }

    @GetMapping("/system-exceptions")
    public List<SystemExceptionDto> getSystemExceptions(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getSystemExceptions(startDate, endDate);
    }

    @GetMapping("/closing-balances")
    public List<BalanceDto> getClosingBalances(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getClosingBalances(startDate, endDate);
    }

    @GetMapping("/opening-balances")
    public List<BalanceDto> getOpeningBalances(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getOpeningBalances(startDate, endDate);
    }

    @GetMapping("/processor-status")
    public List<ProcessorStatusDto> getProcessorStatus() {
        return sessionService.getProcessorStatus();
    }

    @GetMapping("/automated-transaction-status")
    public List<AutomatedTransactionStatusDto> getAutomatedTransactionStatus(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return sessionService.getAutomatedTransactionStatus(startDate, endDate);
    }
}