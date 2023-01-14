package com.tasc.paymentservice.repository;

import com.tasc.paymentservice.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
}
