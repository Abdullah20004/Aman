package com.example.fintech_api.repository;

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

import java.util.List;

public interface SessionRepository {
    List<SessionCountDto> findSessionCounts();
    List<SessionCountDto> findSessionCountsWithAppName();
    List<PidDto> findPidsByUserAndClient(String usename, String clientAddr, String state);
    List<TransactionServerSummaryDto> findTransactionServerSummary(String usename);
    List<TransactionServerSummaryTwoDto> findTransactionServerSummaryTwo(String usename);
    String getTimestampLastFiveMinutes();
    List<AmbiguousTransactionCountDto> findAmbiguousTransactionCount(String startDate, String endDate);
    List<AmbiguousByServiceCategoryDto> findAmbiguousByServiceCategory(String startDate, String endDate);
    List<AmbiguousByServiceCategoryStatusDto> findAmbiguousByServiceCategoryStatus(String startDate, String endDate);
    List<SystemExceptionDto> findSystemExceptions(String startDate, String endDate);
    List<BalanceDto> findClosingBalances(String startDate, String endDate);
    List<BalanceDto> findOpeningBalances(String startDate, String endDate);
    List<ProcessorStatusDto> findProcessorStatus();
    List<AutomatedTransactionStatusDto> findAutomatedTransactionStatus(String startDate, String endDate);
}