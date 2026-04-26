package com.gary.fraudflagger.service;

import com.gary.fraudflagger.model.Transaction;
import com.gary.fraudflagger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;

    public TransactionService(TransactionRepository transactionRepository,
                              FraudDetectionService fraudDetectionService) {
        this.transactionRepository = transactionRepository;
        this.fraudDetectionService = fraudDetectionService;
    }

    public Transaction submitTransaction(Transaction transaction) {
        //Step 1: Save the transaction to the database
        Transaction saved = transactionRepository.save(transaction);

        //Step 2: Run fraud detection rules against it
        fraudDetectionService.evaluate(saved);

        //Step 3: Return the saved Transaction
        return saved;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
