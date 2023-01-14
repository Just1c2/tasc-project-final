package com.tasc.warehouseservice.repository;

import com.tasc.warehouseservice.entity.OrderTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
}
