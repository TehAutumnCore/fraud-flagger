package com.gary.fraudflagger.config;

import com.gary.fraudflagger.model.Account;
import com.gary.fraudflagger.model.Transaction;
import com.gary.fraudflagger.model.User;
import com.gary.fraudflagger.repository.AccountRepository;
import com.gary.fraudflagger.repository.UserRepository;
import com.gary.fraudflagger.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository,
                                       BCryptPasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User analyst = new User();
                analyst.setUsername("analyst1");
                analyst.setPassword(passwordEncoder.encode("password"));
                analyst.setRole("ROLE_ANALYST");
                userRepository.save(analyst);

                User manager = new User();
                manager.setUsername("manager1");
                manager.setPassword(passwordEncoder.encode("password"));
                manager.setRole("ROLE_MANAGER");
                userRepository.save(manager);

                User admin = new User();
                admin.setUsername("admin1");
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);

                System.out.println(">>> Seeded 3 test users: analyst1, manager1, admin1 (password: password)");
            }
        };
    }

    @Bean
    public CommandLineRunner seedTransactions(AccountRepository accountRepository,
                                              TransactionService transactionService) {
        return args -> {

            // Seed accounts if they don't exist yet
            if (accountRepository.findByAccountNumber("ACC-001") == null) {
                Account alice = new Account();
                alice.setAccountNumber("ACC-001");
                alice.setOwnerName("Alice");
                accountRepository.save(alice);
            }
            if (accountRepository.findByAccountNumber("ACC-002") == null) {
                Account bob = new Account();
                bob.setAccountNumber("ACC-002");
                bob.setOwnerName("Bob");
                accountRepository.save(bob);
            }
            if (accountRepository.findByAccountNumber("ACC-003") == null) {
                Account carol = new Account();
                carol.setAccountNumber("ACC-003");
                carol.setOwnerName("Carol");
                accountRepository.save(carol);
            }

            // Skip if transactions already exist
            if (transactionService.getAllTransactions().size() > 0) return;

            Account alice = accountRepository.findByAccountNumber("ACC-001");
            Account bob   = accountRepository.findByAccountNumber("ACC-002");
            Account carol = accountRepository.findByAccountNumber("ACC-003");

            LocalDateTime now = LocalDateTime.now();

            // Rule: LARGE_TRANSACTION — amount over $10,000
            submitAt(transactionService, alice, new BigDecimal("15000.00"), "Offshore Wire Transfer", now.minusMinutes(30));

            // Rule: UNUSUAL_HOUR — between midnight and 5am
            submitAt(transactionService, bob, new BigDecimal("250.00"), "Night Market ATM",
                    now.minusDays(1).withHour(2).withMinute(14));

            // Rule: DUPLICATE_TRANSACTION — same amount + merchant within 5 minutes
            submitAt(transactionService, carol, new BigDecimal("499.99"), "Best Buy", now.minusMinutes(3));
            submitAt(transactionService, carol, new BigDecimal("499.99"), "Best Buy", now.minusMinutes(1));

            // Rule: HIGH_FREQUENCY — 4+ transactions from same account within 10 minutes
            submitAt(transactionService, alice, new BigDecimal("120.00"), "Gas Station",    now.minusMinutes(9));
            submitAt(transactionService, alice, new BigDecimal("85.00"),  "Coffee Shop",    now.minusMinutes(7));
            submitAt(transactionService, alice, new BigDecimal("200.00"), "Grocery Store",  now.minusMinutes(5));
            submitAt(transactionService, alice, new BigDecimal("340.00"), "Pharmacy",       now.minusMinutes(2));

            System.out.println(">>> Seeded demo transactions — dashboard should show flags");
        };
    }

    private void submitAt(TransactionService transactionService, Account account,
                          BigDecimal amount, String merchant, LocalDateTime timestamp) {
        Transaction t = new Transaction();
        t.setAccount(account);
        t.setAmount(amount);
        t.setMerchant(merchant);
        t.setTimestamp(timestamp);
        transactionService.submitTransaction(t);
    }
}