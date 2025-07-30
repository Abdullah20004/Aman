package com.example.fintech_api.repository;

import com.example.fintech_api.dto.PidDto;
import com.example.fintech_api.dto.SessionCountDto;
import com.example.fintech_api.dto.TransactionServerSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionRepository extends JpaRepository<Object, Long> {
    @Query(value = "SELECT usename, client_addr, state, count(1) sessions_count " +
            "FROM pg_stat_activity " +
            "GROUP BY usename, client_addr, state " +
            "ORDER BY sessions_count DESC", nativeQuery = true)
    List<Object[]> findSessionCounts();

    @Query(value = "SELECT usename, client_addr, state, application_name, count(1) sessions_count " +
            "FROM pg_stat_activity " +
            "GROUP BY usename, client_addr, state, application_name " +
            "ORDER BY sessions_count DESC", nativeQuery = true)
    List<Object[]> findSessionCountsWithAppName();

    @Query(value = "SELECT pid " +
            "FROM pg_stat_activity " +
            "WHERE usename = :usename AND client_addr = :clientAddr AND state = :state " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findPidsByUserAndClient(@Param("usename") String usename,
                                           @Param("clientAddr") String clientAddr,
                                           @Param("state") String state);

    @Query(value = "SELECT usename, " +
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
            "GROUP BY usename", nativeQuery = true)
    List<Object[]> findTransactionServerSummary(@Param("usename") String usename);

    @Query(value = "SELECT usename, " +
            "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'idle' THEN sessions_count ELSE 0 END) AS txn_1_ideal, " +
            "SUM(CASE WHEN client_addr = '10.100.149.31' AND state = 'idle' THEN sessions_count ELSE 0 END) AS txn_2_ideal, " +
            "SUM(CASE WHEN client_addr = '10.100.149.21' AND state = 'active' THEN sessions_count ELSE 0 END) AS txn_1_active, " +
            "SUM(CASE WHEN client_addr = '10.100.149.31' AND state = 'active' THEN sessions_count ELSE 0 END) AS txn_2_active " +
            "FROM (SELECT usename, client_addr, state, count(1) AS sessions_count " +
            "FROM pg_stat_activity " +
            "WHERE usename = :usename " +
            "GROUP BY usename, client_addr, state) sq1 " +
            "GROUP BY usename", nativeQuery = true)
    List<Object[]> findTransactionServerSummaryTwo(@Param("usename") String usename);

    @Query(value = "SELECT to_char(CLOCK_TIMESTAMP() - interval '5 minutes', 'YYYYMMDDHH24MISS')", nativeQuery = true)
    String getTimestampLastFiveMinutes();

    @Query(value = "SELECT service_transaction_type, " +
            "SUM(CASE WHEN ambiguous_flag = 'Y' THEN txn_count ELSE 0 END) AS is_ambiguous, " +
            "SUM(CASE WHEN ambiguous_flag <> 'Y' THEN txn_count ELSE 0 END) AS not_ambiguous " +
            "FROM (SELECT service_transaction_type, ambiguous_flag, count(1) AS txn_count " +
            "FROM gle_prod_data.transactions_catalog " +
            "WHERE transaction_date >= current_date - 10 " +
            "AND transaction_status = '0' " +
            "AND transaction_time >= to_timestamp(:timestamp, 'YYYYMMDDHH24MISS') " +
            "GROUP BY service_transaction_type, ambiguous_flag) sq1 " +
            "GROUP BY service_transaction_type " +
            "ORDER BY service_transaction_type", nativeQuery = true)
    List<Object[]> findAmbiguousTransactionCount(@Param("timestamp") String timestamp);

    @Query(value = "SELECT ambiguous_flag, service_category, count(1) AS count " +
            "FROM gle_prod_data.transactions_catalog " +
            "WHERE transaction_date = current_date - 10 " +
            "AND transaction_time >= to_timestamp(:timestamp, 'YYYYMMDDHH24MISS') " +
            "AND transaction_status = '0' " +
            "GROUP BY ambiguous_flag, service_category", nativeQuery = true)
    List<Object[]> findAmbiguousByServiceCategory(@Param("timestamp") String timestamp);

    @Query(value = "SELECT ambiguous_flag, service_category, " +
            "SUM(CASE WHEN transaction_status = '0' THEN txn_count ELSE 0 END) AS success_count, " +
            "SUM(CASE WHEN transaction_status <> '0' THEN txn_count ELSE 0 END) AS failed_count " +
            "FROM (SELECT transaction_status, ambiguous_flag, service_category, count(1) AS txn_count " +
            "FROM gle_prod_data.transactions_catalog " +
            "WHERE transaction_date = current_date " +
            "AND transaction_time >= to_timestamp(:timestamp, 'YYYYMMDDHH24MISS') " +
            "GROUP BY transaction_status, ambiguous_flag, service_category) sq1 " +
            "GROUP BY ambiguous_flag, service_category " +
            "ORDER BY ambiguous_flag, service_category", nativeQuery = true)
    List<Object[]> findAmbiguousByServiceCategoryStatus(@Param("timestamp") String timestamp);

    @Query(value = "SELECT exception_date, module_name, exception_message, count(1) AS txn_count, " +
            "min(exception_time) AS from_time, max(exception_time) AS to_time " +
            "FROM gle_prod_data.general_exception_log " +
            "WHERE exception_date >= current_date " +
            "AND exception_time >= CLOCK_TIMESTAMP() - interval '60 minutes' " +
            "GROUP BY exception_date, module_name, exception_message " +
            "ORDER BY to_time DESC", nativeQuery = true)
    List<Object[]> findSystemExceptions();

    @Query(value = "SELECT data_type, balance_day, txn_count, from_time, to_time, " +
            "int_get_time_difference(from_time, to_time) AS execution_time " +
            "FROM (SELECT 'closing' AS data_type, balance_day, count(1) AS txn_count, " +
            "min(db_time) AS from_time, max(db_time) AS to_time " +
            "FROM gle_prod_data.all_wallets_closing_balances awcb " +
            "WHERE balance_day >= current_date - 30 " +
            "GROUP BY balance_day) sq1 " +
            "ORDER BY balance_day", nativeQuery = true)
    List<Object[]> findClosingBalances();

    @Query(value = "SELECT data_type, balance_day, txn_count, from_time, to_time, " +
            "int_get_time_difference(from_time, to_time) AS execution_time " +
            "FROM (SELECT 'opening' AS data_type, balance_day, count(1) AS txn_count, " +
            "min(db_time) AS from_time, max(db_time) AS to_time " +
            "FROM gle_prod_data.all_wallets_opening_balances awcb " +
            "WHERE balance_day >= current_date - 30 " +
            "GROUP BY balance_day) sq1 " +
            "ORDER BY balance_day", nativeQuery = true)
    List<Object[]> findOpeningBalances();

    @Query(value = "SELECT processor_id, processor_running_status, last_run, transactions_count, " +
            "last_run_since_seconds, 120 - last_run_since_seconds AS next_run_in_seconds " +
            "FROM (SELECT status, processor_id, processor_running_status, last_run, transactions_count " +
            "FROM automated_transactions_processors " +
            "WHERE status = 'ACT') sq1", nativeQuery = true)
    List<Object[]> findProcessorStatus();

    @Query(value = "SELECT db_date, service_code, at_status, transaction_status, " +
            "count(distinct user_a_id) AS user_a_count, " +
            "count(distinct user_b_account_id) AS user_b_count, " +
            "round(sum(transaction_value), 2) AS total_value, " +
            "count(1) AS txn_count, min(executed_on_time) AS from_time, max(executed_on_time) AS to_time " +
            "FROM gle_prod_data.automated_transactions_queue " +
            "WHERE db_date = current_date " +
            "AND service_code = 'COMMISSION_TO_MAIN' " +
            "GROUP BY db_date, service_code, at_status, transaction_status " +
            "ORDER BY service_code", nativeQuery = true)
    List<Object[]> findAutomatedTransactionStatus();
}