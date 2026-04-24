package com.gary.fraudflagger.repository;

import com.gary.fraudflagger.model.Account;
import com.gary.fraudflagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

    List<Transaction> findByAccountAndTimestampBetween(
            Account account,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Transaction> findByAccountAndAmountAndMerchantAndTimestampBetween(
            Account account,
            BigDecimal amount,
            String merchant,
            LocalDateTime start,
            LocalDateTime end

    );
}
