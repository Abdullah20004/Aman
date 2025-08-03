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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<SessionCountDto> getSessionCounts() {
        return sessionRepository.findSessionCounts();
    }

    public List<SessionCountDto> getSessionCountsWithAppName() {
        return sessionRepository.findSessionCountsWithAppName();
    }

    public List<PidDto> getPidsByUserAndClient(String usename, String clientAddr, String state) {
        return sessionRepository.findPidsByUserAndClient(usename, clientAddr, state);
    }

    public List<TransactionServerSummaryDto> getTransactionServerSummary(String usename) {
        return sessionRepository.findTransactionServerSummary(usename);
    }

    public List<TransactionServerSummaryTwoDto> getTransactionServerSummaryTwo(String usename) {
        return sessionRepository.findTransactionServerSummaryTwo(usename);
    }

    public String getTimestampLastFiveMinutes() {
        return sessionRepository.getTimestampLastFiveMinutes();
    }

    public List<AmbiguousTransactionCountDto> getAmbiguousTransactionCount(String startDate, String endDate) {
        return sessionRepository.findAmbiguousTransactionCount(startDate, endDate);
    }

    public List<AmbiguousByServiceCategoryDto> getAmbiguousByServiceCategory(String startDate, String endDate) {
        return sessionRepository.findAmbiguousByServiceCategory(startDate, endDate);
    }

    public List<AmbiguousByServiceCategoryStatusDto> getAmbiguousByServiceCategoryStatus(String startDate, String endDate) {
        return sessionRepository.findAmbiguousByServiceCategoryStatus(startDate, endDate);
    }

    public List<SystemExceptionDto> getSystemExceptions(String startDate, String endDate) {
        return sessionRepository.findSystemExceptions(startDate, endDate);
    }

    public List<BalanceDto> getClosingBalances(String startDate, String endDate) {
        return sessionRepository.findClosingBalances(startDate, endDate);
    }

    public List<BalanceDto> getOpeningBalances(String startDate, String endDate) {
        return sessionRepository.findOpeningBalances(startDate, endDate);
    }

    public List<ProcessorStatusDto> getProcessorStatus() {
        return sessionRepository.findProcessorStatus();
    }

    public List<AutomatedTransactionStatusDto> getAutomatedTransactionStatus(String startDate, String endDate) {
        return sessionRepository.findAutomatedTransactionStatus(startDate, endDate);
    }
}