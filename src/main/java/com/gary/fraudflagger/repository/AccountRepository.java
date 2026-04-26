package com.gary.fraudflagger.repository;

import com.gary.fraudflagger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long>{
    Account findByAccountNumber(String accountNumber);
}
