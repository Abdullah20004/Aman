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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class SessionRepositoryImpl implements SessionRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SessionRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public SessionRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate jdbcTemplate1) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate1;
    }


    @Override
    public List<SessionCountDto> findSessionCounts() {
        String sql = "SELECT usename, client_addr, state, count(1) sessions_count " +
                "FROM pg_stat_activity " +
                "GROUP BY usename, client_addr, state " +
                "ORDER BY sessions_count DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SessionCountDto dto = new SessionCountDto();
            dto.setUsename(rs.getString("usename"));
            dto.setClientAddr(rs.getString("client_addr"));
            dto.setState(rs.getString("state"));
            dto.setSessionsCount(rs.getLong("sessions_count"));
            return dto;
        });
    }

    @Override
    public List<SessionCountDto> findSessionCountsWithAppName() {
        String sql = "SELECT usename, client_addr, state, application_name, count(1) sessions_count " +
                "FROM pg_stat_activity " +
                "GROUP BY usename, client_addr, state, application_name " +
                "ORDER BY sessions_count DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SessionCountDto dto = new SessionCountDto();
            dto.setUsename(rs.getString("usename"));
            dto.setClientAddr(rs.getString("client_addr"));
            dto.setState(rs.getString("state"));
            dto.setApplicationName(rs.getString("application_name"));
            dto.setSessionsCount(rs.getLong("sessions_count"));
            return dto;
        });
    }

    @Override
    public List<PidDto> findPidsByUserAndClient(String usename, String clientAddr, String state) {
        try {
            String sql = "SELECT pid " +
                    "FROM pg_stat_activity " +
                    "WHERE usename = :usename " +
                    "AND (client_addr = :clientAddr OR (:clientAddr IS NULL AND client_addr IS NULL)) " +
                    "AND state = :state " +
                    "LIMIT 10";
            Map<String, Object> params = new HashMap<>();
            params.put("usename", usename);
            params.put("clientAddr", clientAddr);
            params.put("state", state);
            return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
                PidDto dto = new PidDto();
                dto.setPid(rs.getLong("pid"));
                return dto;
            });
        } catch (DataAccessException e) {
            System.err.println("Error querying pg_stat_activity: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<TransactionServerSummaryDto> findTransactionServerSummary(String usename) {
        String sql = "SELECT usename, " +
                "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'idle' THEN sessions_count ELSE 0 END) txn_1_ideal, " +
                "SUM(CASE WHEN client_addr = '10.100.149.22' AND state = 'idle' THEN sessions_count ELSE 0 END) txn_2_ideal, " +
                "SUM(CASE WHEN client_addr = '10.100.149.23' AND state = 'idle' THEN sessions_count ELSE 0 END) txn_3_ideal, " +
                "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'active' THEN sessions_count ELSE 0 END) txn_1_active, " +
                "SUM(CASE WHEN client_addr = '10.100.149.22' AND state = 'active' THEN sessions_count ELSE 0 END) txn_2_active, " +
                "SUM(CASE WHEN client_addr = '10.100.149.23' AND state = 'active' THEN sessions_count ELSE 0 END) txn_3_active " +
                "FROM (SELECT usename, client_addr, state, count(1) sessions_count " +
                "FROM pg_stat_activity " +
                "WHERE usename = :usename " +
                "GROUP BY usename, client_addr, state) sq1 " +
                "GROUP BY usename";
        Map<String, Object> params = new HashMap<>();
        params.put("usename", usename);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            TransactionServerSummaryDto dto = new TransactionServerSummaryDto();
            dto.setUsename(rs.getString("usename"));
            dto.setTxn1Ideal(rs.getLong("txn_1_ideal"));
            dto.setTxn2Ideal(rs.getLong("txn_2_ideal"));
            dto.setTxn3Ideal(rs.getLong("txn_3_ideal"));
            dto.setTxn1Active(rs.getLong("txn_1_active"));
            dto.setTxn2Active(rs.getLong("txn_2_active"));
            dto.setTxn3Active(rs.getLong("txn_3_active"));
            return dto;
        });
    }

    @Override
    public List<TransactionServerSummaryTwoDto> findTransactionServerSummaryTwo(String usename) {
        String sql = "SELECT usename, " +
                "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'idle' THEN sessions_count ELSE 0 END) AS txn_1_ideal, " +
                "SUM(CASE WHEN client_addr = '10.100.149.31' AND state = 'idle' THEN sessions_count ELSE 0 END) AS txn_2_ideal, " +
                "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'active' THEN sessions_count ELSE 0 END) AS txn_1_active, " +
                "SUM(CASE WHEN client_addr = '10.100.149.31' AND state = 'active' THEN sessions_count ELSE 0 END) AS txn_2_active " +
                "FROM (SELECT usename, client_addr, state, count(1) AS sessions_count " +
                "FROM pg_stat_activity " +
                "WHERE usename = :usename " +
                "GROUP BY usename, client_addr, state) sq1 " +
                "GROUP BY usename";
        Map<String, Object> params = new HashMap<>();
        params.put("usename", usename);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            TransactionServerSummaryTwoDto dto = new TransactionServerSummaryTwoDto();
            dto.setUsename(rs.getString("usename"));
            dto.setTxn1Ideal(rs.getLong("txn_1_ideal"));
            dto.setTxn2Ideal(rs.getLong("txn_2_ideal"));
            dto.setTxn1Active(rs.getLong("txn_1_active"));
            dto.setTxn2Active(rs.getLong("txn_2_active"));
            return dto;
        });
    }

    @Override
    public String getTimestampLastFiveMinutes() {
        String sql = "SELECT to_char(CLOCK_TIMESTAMP() - interval '5 minutes', 'YYYYMMDDHH24MISS')";
        return jdbcTemplate.queryForObject(sql, new HashMap<>(), String.class);
    }

    @Override
    public List<AmbiguousTransactionCountDto> findAmbiguousTransactionCount(String startDate, String endDate) {
        String sql = "SELECT service_transaction_type, " +
                "SUM(CASE WHEN ambiguous_flag = 'Y' THEN txn_count ELSE 0 END) AS is_ambiguous, " +
                "SUM(CASE WHEN ambiguous_flag <> 'Y' THEN txn_count ELSE 0 END) AS not_ambiguous " +
                "FROM (SELECT service_transaction_type, ambiguous_flag, count(1) AS txn_count " +
                "FROM gle_prod_data.transactions_catalog " +
                "WHERE transaction_date BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "AND transaction_status = '0' " +
                "AND transaction_time >= to_timestamp(:startDate, 'YYYYMMDDHH24MISS') " +
                "AND transaction_time <= to_timestamp(:endDate, 'YYYYMMDDHH24MISS') " +
                "GROUP BY service_transaction_type, ambiguous_flag) sq1 " +
                "GROUP BY service_transaction_type " +
                "ORDER BY service_transaction_type";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AmbiguousTransactionCountDto dto = new AmbiguousTransactionCountDto();
            dto.setServiceTransactionType(rs.getString("service_transaction_type"));
            dto.setIsAmbiguous(rs.getLong("is_ambiguous"));
            dto.setNotAmbiguous(rs.getLong("not_ambiguous"));
            return dto;
        });
    }

    @Override
    public List<AmbiguousByServiceCategoryDto> findAmbiguousByServiceCategory(String startDate, String endDate) {
        String sql = "SELECT ambiguous_flag, service_category, count(1) AS count " +
                "FROM gle_prod_data.transactions_catalog " +
                "WHERE transaction_date BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "AND transaction_time >= to_timestamp(:startDate, 'YYYYMMDDHH24MISS') " +
                "AND transaction_time <= to_timestamp(:endDate, 'YYYYMMDDHH24MISS') " +
                "AND transaction_status = '0' " +
                "GROUP BY ambiguous_flag, service_category";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AmbiguousByServiceCategoryDto dto = new AmbiguousByServiceCategoryDto();
            dto.setAmbiguousFlag(rs.getString("ambiguous_flag"));
            dto.setServiceCategory(rs.getString("service_category"));
            dto.setCount(rs.getLong("count"));
            return dto;
        });
    }

    @Override
    public List<AmbiguousByServiceCategoryStatusDto> findAmbiguousByServiceCategoryStatus(String startDate, String endDate) {
        String sql = "SELECT ambiguous_flag, service_category, " +
                "SUM(CASE WHEN transaction_status = '0' THEN txn_count ELSE 0 END) AS success_count, " +
                "SUM(CASE WHEN transaction_status <> '0' THEN txn_count ELSE 0 END) AS failed_count " +
                "FROM (SELECT transaction_status, ambiguous_flag, service_category, count(1) AS txn_count " +
                "FROM gle_prod_data.transactions_catalog " +
                "WHERE transaction_date BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "AND transaction_time >= to_timestamp(:startDate, 'YYYYMMDDHH24MISS') " +
                "AND transaction_time <= to_timestamp(:endDate, 'YYYYMMDDHH24MISS') " +
                "GROUP BY transaction_status, ambiguous_flag, service_category) sq1 " +
                "GROUP BY ambiguous_flag, service_category " +
                "ORDER BY ambiguous_flag, service_category";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AmbiguousByServiceCategoryStatusDto dto = new AmbiguousByServiceCategoryStatusDto();
            dto.setAmbiguousFlag(rs.getString("ambiguous_flag"));
            dto.setServiceCategory(rs.getString("service_category"));
            dto.setSuccessCount(rs.getLong("success_count"));
            dto.setFailedCount(rs.getLong("failed_count"));
            return dto;
        });
    }

    @Override
    public List<SystemExceptionDto> findSystemExceptions(String startDate, String endDate) {
        String sql = "SELECT exception_date, module_name, exception_message, count(1) AS txn_count, " +
                "min(exception_time) AS from_time, max(exception_time) AS to_time " +
                "FROM gle_prod_data.general_exception_log " +
                "WHERE exception_date BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "AND exception_time >= to_timestamp(:startDate, 'YYYYMMDDHH24MISS') " +
                "AND exception_time <= to_timestamp(:endDate, 'YYYYMMDDHH24MISS') " +
                "GROUP BY exception_date, module_name, exception_message " +
                "ORDER BY to_time DESC";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            SystemExceptionDto dto = new SystemExceptionDto();
            dto.setExceptionDate(rs.getDate("exception_date"));
            dto.setModuleName(rs.getString("module_name"));
            dto.setExceptionMessage(rs.getString("exception_message"));
            dto.setTxnCount(rs.getLong("txn_count"));
            dto.setFromTime(rs.getTimestamp("from_time"));
            dto.setToTime(rs.getTimestamp("to_time"));
            return dto;
        });
    }

    @Override
    public List<BalanceDto> findClosingBalances(String startDate, String endDate) {
        String sql = "SELECT data_type, balance_day, txn_count, from_time, to_time, " +
                "gle_prod_config.int_get_time_difference(from_time, to_time) AS execution_time " +
                "FROM (SELECT 'closing' AS data_type, balance_day, count(1) AS txn_count, " +
                "min(db_time) AS from_time, max(db_time) AS to_time " +
                "FROM gle_prod_data.all_wallets_closing_balances awcb " +
                "WHERE balance_day BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "GROUP BY balance_day) sq1 " +
                "ORDER BY balance_day";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            BalanceDto dto = new BalanceDto();
            dto.setDataType(rs.getString("data_type"));
            dto.setBalanceDay(rs.getDate("balance_day"));
            dto.setTxnCount(rs.getLong("txn_count"));
            dto.setFromTime(rs.getTimestamp("from_time"));
            dto.setToTime(rs.getTimestamp("to_time"));
            dto.setExecutionTime(rs.getString("execution_time"));
            return dto;
        });
    }

    @Override
    public List<BalanceDto> findOpeningBalances(String startDate, String endDate) {
        String sql = "SELECT data_type, balance_day, txn_count, from_time, to_time, " +
                "gle_prod_config.int_get_time_difference(from_time, to_time) AS execution_time " +
                "FROM (SELECT 'opening' AS data_type, balance_day, count(1) AS txn_count, " +
                "min(db_time) AS from_time, max(db_time) AS to_time " +
                "FROM gle_prod_data.all_wallets_opening_balances awcb " +
                "WHERE balance_day BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "GROUP BY balance_day) sq1 " +
                "ORDER BY balance_day";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            BalanceDto dto = new BalanceDto();
            dto.setDataType(rs.getString("data_type"));
            dto.setBalanceDay(rs.getDate("balance_day"));
            dto.setTxnCount(rs.getLong("txn_count"));
            dto.setFromTime(rs.getTimestamp("from_time"));
            dto.setToTime(rs.getTimestamp("to_time"));
            dto.setExecutionTime(rs.getString("execution_time"));
            return dto;
        });
    }

    @Override
    public List<ProcessorStatusDto> findProcessorStatus() {
        // Get the underlying JdbcTemplate from the namedJdbcTemplate
        JdbcTemplate rawJdbcTemplate = namedJdbcTemplate.getJdbcTemplate();

        // Set the search_path
        rawJdbcTemplate.execute("SET search_path TO gle_prod_config, gle_prod_data");

        String sql = """
        SELECT 
            processor_id, 
            processor_running_status, 
            last_run, 
            transactions_count, 
            last_run_since_seconds, 
            120 - last_run_since_seconds AS next_run_in_seconds
        FROM (
            SELECT 
                processor_id, 
                processor_running_status, 
                last_run, 
                transactions_count,
                EXTRACT(EPOCH FROM (NOW() - last_run))::INT AS last_run_since_seconds
            FROM automated_transactions_processors
            WHERE status = 'ACT'
        ) sq1
    """;

        return namedJdbcTemplate.query(sql, (rs, rowNum) -> {
            ProcessorStatusDto dto = new ProcessorStatusDto();
            dto.setProcessorId(rs.getString("processor_id"));
            dto.setProcessorRunningStatus(rs.getString("processor_running_status"));
            dto.setLastRun(rs.getTimestamp("last_run"));
            dto.setTransactionsCount(rs.getLong("transactions_count"));
            dto.setLastRunSinceSeconds(rs.getLong("last_run_since_seconds"));
            dto.setNextRunInSeconds(rs.getLong("next_run_in_seconds"));
            return dto;
        });
    }




    @Override
    public List<AutomatedTransactionStatusDto> findAutomatedTransactionStatus(String startDate, String endDate) {
        String sql = "SELECT db_date, service_code, at_status, transaction_status, " +
                "count(distinct user_a_id) AS user_a_count, " +
                "count(distinct user_b_account_id) AS user_b_count, " +
                "round(sum(transaction_value), 2) AS total_value, " +
                "count(1) AS txn_count, min(executed_on_time) AS from_time, max(executed_on_time) AS to_time " +
                "FROM gle_prod_data.automated_transactions_queue " +
                "WHERE db_date BETWEEN to_date(:startDate, 'YYYYMMDD') AND to_date(:endDate, 'YYYYMMDD') " +
                "AND service_code = 'COMMISSION_TO_MAIN' " +
                "GROUP BY db_date, service_code, at_status, transaction_status " +
                "ORDER BY service_code";
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            AutomatedTransactionStatusDto dto = new AutomatedTransactionStatusDto();
            dto.setDbDate(rs.getDate("db_date"));
            dto.setServiceCode(rs.getString("service_code"));
            dto.setAtStatus(rs.getString("at_status"));
            dto.setTransactionStatus(rs.getString("transaction_status"));
            dto.setUserACount(rs.getLong("user_a_count"));
            dto.setUserBCount(rs.getLong("user_b_count"));
            dto.setTotalValue(rs.getDouble("total_value"));
            dto.setTxnCount(rs.getLong("txn_count"));
            dto.setFromTime(rs.getTimestamp("from_time"));
            dto.setToTime(rs.getTimestamp("to_time"));
            return dto;
        });
    }
}