package com.gary.fraudflagger.repository;

import com.gary.fraudflagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
}
