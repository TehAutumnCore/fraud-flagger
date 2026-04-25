package com.gary.fraudflagger.service;

import com.gary.fraudflagger.model.FraudFlag;
import com.gary.fraudflagger.model.Transaction;
import com.gary.fraudflagger.repository.FraudFlagRepository;
import com.gary.fraudflagger.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service //Service - tells spring this is a service and to manage it for me
public class FraudDetectionService {

    private final TransactionRepository transactionRepository;
    private final FraudFlagRepository  fraudFlagRepository;

    public FraudDetectionService(TransactionRepository transactionRepository,
                                 FraudFlagRepository fraudFlagRepository) {
        this.transactionRepository = transactionRepository;
        this.fraudFlagRepository = fraudFlagRepository;
    }

    public void evaluate(Transaction transaction) { //main method; takes one transaction and runs all 4 rules
        List<String> triggeredRules = new ArrayList<>(); //a list that collects the names of every rule that fired
        int riskScore = 0; //starts at 0, adds points per rule hit, capped at 100. at end if anything triggered, create and save a FraudFlag

        //Rule 1: Large Transaction - Over 10,000
        if (transaction.getAmount().doubleValue() > 10_000) {
            triggeredRules.add("LARGE_TRANSACTION");
            riskScore += 30;
        }

        //Rule 2: High frequency - more than 3 transactions from same account in 10 minutes
        LocalDateTime tenMinutesAgo = transaction.getTimestamp().minusMinutes(10);
        List<Transaction> recentTransactions = transactionRepository
                .findByAccountAndTimestampBetween(
                        transaction.getAccount(),
                        tenMinutesAgo,
                        transaction.getTimestamp()
                );
        if (recentTransactions.size() > 3) {
            triggeredRules.add("HIGH_FREQUENCY");
            riskScore += 30;
        }

        //Rule 3: Duplicate transaction - same amount and merchant within 5 minutes
        LocalDateTime fiveMinutesAgo = transaction.getTimestamp().minusMinutes(5);
        List<Transaction> duplicates = transactionRepository
                .findByAccountAndAmountAndMerchantAndTimestampBetween(
                        transaction.getAccount(),
                        transaction.getAmount(),
                        transaction.getMerchant(),
                        fiveMinutesAgo,
                        transaction.getTimestamp()
                );
        if (duplicates.size() > 1) {
            triggeredRules.add("DUPLICATE_TRANSACTION");
            riskScore += 25;
        }

        //Rule 4: Unusual hour - midnight to 5am
        int hour = transaction.getTimestamp().getHour();
        if(hour >= 0 && hour < 5) {
            triggeredRules.add("UNUSUAL_HOUR");
            riskScore += 15;
        }


        //Cap the score at 100
        riskScore = Math.min(riskScore, 100);

        //Only create a flag if atleast one rule is triggered
        if (!triggeredRules.isEmpty()) {
            FraudFlag flag = new FraudFlag();
            flag.setTransaction(transaction);
            flag.setRiskScore(riskScore);
            flag.setTriggeredRules(String.join(", ", triggeredRules));
            flag.setStatus("PENDING");
            flag.setFlaggedAt(LocalDateTime.now());
            flag.setReason("Automated fraud detection");
            fraudFlagRepository.save(flag);
        }
    }
}
