package com.gary.fraudflagger.repository;

import com.gary.fraudflagger.model.FraudFlag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudFlagRepository extends JpaRepository<FraudFlag, Long> {
}
