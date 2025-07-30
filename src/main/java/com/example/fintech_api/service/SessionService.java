package com.example.fintech_api.service;

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
import com.example.fintech_api.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<SessionCountDto> getSessionCounts() {
        List<Object[]> results = sessionRepository.findSessionCounts();
        return results.stream().map(result -> {
            SessionCountDto dto = new SessionCountDto();
            dto.setUsename(Objects.toString(result[0], null));
            dto.setClientAddr(Objects.toString(result[1], null));
            dto.setState(Objects.toString(result[2], null));
            dto.setSessionsCount(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<SessionCountDto> getSessionCountsWithAppName() {
        List<Object[]> results = sessionRepository.findSessionCountsWithAppName();
        return results.stream().map(result -> {
            SessionCountDto dto = new SessionCountDto();
            dto.setUsename(Objects.toString(result[0], null));
            dto.setClientAddr(Objects.toString(result[1], null));
            dto.setState(Objects.toString(result[2], null));
            dto.setApplicationName(Objects.toString(result[3], null));
            dto.setSessionsCount(result[4] instanceof Number ? ((Number) result[4]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<PidDto> getPidsByUserAndClient(String usename, String clientAddr, String state) {
        List<Object[]> results = sessionRepository.findPidsByUserAndClient(usename, clientAddr, state);
        return results.stream().map(result -> {
            PidDto dto = new PidDto();
            dto.setPid(result[0] instanceof Number ? ((Number) result[0]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<TransactionServerSummaryDto> getTransactionServerSummary(String usename) {
        List<Object[]> results = sessionRepository.findTransactionServerSummary(usename);
        return results.stream().map(result -> {
            TransactionServerSummaryDto dto = new TransactionServerSummaryDto();
            dto.setUsename(Objects.toString(result[0], null));
            dto.setTxn1Ideal(result[1] instanceof Number ? ((Number) result[1]).longValue() : 0L);
            dto.setTxn2Ideal(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            dto.setTxn3Ideal(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            dto.setTxn1Active(result[4] instanceof Number ? ((Number) result[4]).longValue() : 0L);
            dto.setTxn2Active(result[5] instanceof Number ? ((Number) result[5]).longValue() : 0L);
            dto.setTxn3Active(result[6] instanceof Number ? ((Number) result[6]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<List<TransactionServerSummaryTwoDto>> getTransactionServerSummaryTwo(String usename) {
        List<Object[]> results = sessionRepository.findTransactionServerSummaryTwo(usename);
        List<TransactionServerSummaryTwoDto> dtos = results.stream().map(result -> {
            TransactionServerSummaryTwoDto dto = new TransactionServerSummaryTwoDto();
            dto.setUsename(Objects.toString(result[0], null));
            dto.setTxn1Ideal(result[1] instanceof Number ? ((Number) result[1]).longValue() : 0L);
            dto.setTxn2Ideal(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            dto.setTxn1Active(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            dto.setTxn2Active(result[4] instanceof Number ? ((Number) result[4]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<AmbiguousTransactionCountDto>> getAmbiguousTransactionCount() {
        String timestamp = sessionRepository.getTimestampLastFiveMinutes();
        List<Object[]> results = sessionRepository.findAmbiguousTransactionCount(timestamp);
        List<AmbiguousTransactionCountDto> dtos = results.stream().map(result -> {
            AmbiguousTransactionCountDto dto = new AmbiguousTransactionCountDto();
            dto.setServiceTransactionType(Objects.toString(result[0], null));
            dto.setIsAmbiguous(result[1] instanceof Number ? ((Number) result[1]).longValue() : 0L);
            dto.setNotAmbiguous(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<AmbiguousByServiceCategoryDto>> getAmbiguousByServiceCategory() {
        String timestamp = sessionRepository.getTimestampLastFiveMinutes();
        List<Object[]> results = sessionRepository.findAmbiguousByServiceCategory(timestamp);
        List<AmbiguousByServiceCategoryDto> dtos = results.stream().map(result -> {
            AmbiguousByServiceCategoryDto dto = new AmbiguousByServiceCategoryDto();
            dto.setAmbiguousFlag(Objects.toString(result[0], null));
            dto.setServiceCategory(Objects.toString(result[1], null));
            dto.setCount(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<AmbiguousByServiceCategoryStatusDto>> getAmbiguousByServiceCategoryStatus() {
        String timestamp = sessionRepository.getTimestampLastFiveMinutes();
        List<Object[]> results = sessionRepository.findAmbiguousByServiceCategoryStatus(timestamp);
        List<AmbiguousByServiceCategoryStatusDto> dtos = results.stream().map(result -> {
            AmbiguousByServiceCategoryStatusDto dto = new AmbiguousByServiceCategoryStatusDto();
            dto.setAmbiguousFlag(Objects.toString(result[0], null));
            dto.setServiceCategory(Objects.toString(result[1], null));
            dto.setSuccessCount(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            dto.setFailedCount(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<SystemExceptionDto>> getSystemExceptions() {
        List<Object[]> results = sessionRepository.findSystemExceptions();
        List<SystemExceptionDto> dtos = results.stream().map(result -> {
            SystemExceptionDto dto = new SystemExceptionDto();
            dto.setExceptionDate(result[0] instanceof java.sql.Date ? ((java.sql.Date) result[0]).toLocalDate() : null);
            dto.setModuleName(Objects.toString(result[1], null));
            dto.setExceptionMessage(Objects.toString(result[2], null));
            dto.setTxnCount(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            dto.setFromTime(result[4] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[4]).toLocalDateTime() : null);
            dto.setToTime(result[5] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[5]).toLocalDateTime() : null);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<BalanceDto>> getClosingBalances() {
        List<Object[]> results = sessionRepository.findClosingBalances();
        List<BalanceDto> dtos = results.stream().map(result -> {
            BalanceDto dto = new BalanceDto();
            dto.setDataType(Objects.toString(result[0], null));
            dto.setBalanceDay(result[1] instanceof java.sql.Date ? ((java.sql.Date) result[1]).toLocalDate() : null);
            dto.setTxnCount(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            dto.setFromTime(result[3] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[3]).toLocalDateTime() : null);
            dto.setToTime(result[4] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[4]).toLocalDateTime() : null);
            dto.setExecutionTime(result[5] instanceof Number ? ((Number) result[5]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<BalanceDto>> getOpeningBalances() {
        List<Object[]> results = sessionRepository.findOpeningBalances();
        List<BalanceDto> dtos = results.stream().map(result -> {
            BalanceDto dto = new BalanceDto();
            dto.setDataType(Objects.toString(result[0], null));
            dto.setBalanceDay(result[1] instanceof java.sql.Date ? ((java.sql.Date) result[1]).toLocalDate() : null);
            dto.setTxnCount(result[2] instanceof Number ? ((Number) result[2]).longValue() : 0L);
            dto.setFromTime(result[3] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[3]).toLocalDateTime() : null);
            dto.setToTime(result[4] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[4]).toLocalDateTime() : null);
            dto.setExecutionTime(result[5] instanceof Number ? ((Number) result[5]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<ProcessorStatusDto>> getProcessorStatus() {
        List<Object[]> results = sessionRepository.findProcessorStatus();
        List<ProcessorStatusDto> dtos = results.stream().map(result -> {
            ProcessorStatusDto dto = new ProcessorStatusDto();
            dto.setProcessorId(Objects.toString(result[0], null));
            dto.setProcessorRunningStatus(Objects.toString(result[1], null));
            dto.setLastRun(result[2] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[2]).toLocalDateTime() : null);
            dto.setTransactionsCount(result[3] instanceof Number ? ((Number) result[3]).longValue() : 0L);
            dto.setLastRunSinceSeconds(result[4] instanceof Number ? ((Number) result[4]).longValue() : 0L);
            dto.setNextRunInSeconds(result[5] instanceof Number ? ((Number) result[5]).longValue() : 0L);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<List<AutomatedTransactionStatusDto>> getAutomatedTransactionStatus() {
        List<Object[]> results = sessionRepository.findAutomatedTransactionStatus();
        List<AutomatedTransactionStatusDto> dtos = results.stream().map(result -> {
            AutomatedTransactionStatusDto dto = new AutomatedTransactionStatusDto();
            dto.setDbDate(result[0] instanceof java.sql.Date ? ((java.sql.Date) result[0]).toLocalDate() : null);
            dto.setServiceCode(Objects.toString(result[1], null));
            dto.setAtStatus(Objects.toString(result[2], null));
            dto.setTransactionStatus(Objects.toString(result[3], null));
            dto.setUserACount(result[4] instanceof Number ? ((Number) result[4]).longValue() : 0L);
            dto.setUserBCount(result[5] instanceof Number ? ((Number) result[5]).longValue() : 0L);
            dto.setTotalValue(result[6] instanceof Number ? ((Number) result[6]).doubleValue() : 0.0);
            dto.setTxnCount(result[7] instanceof Number ? ((Number) result[7]).longValue() : 0L);
            dto.setFromTime(result[8] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[8]).toLocalDateTime() : null);
            dto.setToTime(result[9] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) result[9]).toLocalDateTime() : null);
            return dto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Async
    public CompletableFuture<String> getPartitionStatus() {
        // Placeholder logic: Return expected string for the next 3 days
        LocalDate today = LocalDate.now();
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (int i = 1; i <= 3; i++) {
            result.append("All tables are fine for ").append(today.plusDays(i).format(formatter));
            if (i < 3) {
                result.append("; ");
            }
        }
        return CompletableFuture.completedFuture(result.toString());
    }
}