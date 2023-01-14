package com.tasc.paymentservice.repository;

import com.tasc.paymentservice.entity.OrderPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentRepository extends JpaRepository<OrderPaymentEntity, Long> {
}
