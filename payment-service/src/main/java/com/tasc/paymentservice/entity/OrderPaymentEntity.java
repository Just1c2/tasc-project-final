package com.tasc.paymentservice.entity;

import lombok.Data;
import vn.tass.microservice.model.payment.request.OrderPaymentRequest;

import javax.persistence.*;

@Entity
@Data
@Table(name = "order_payment")
public class OrderPaymentEntity {

    @Id
    private long orderId;
    private long amount;
    private int status;

    public OrderPaymentEntity() {

    }

    public OrderPaymentEntity(OrderPaymentRequest request) {
        this.setStatus(1);
        this.setOrderId(request.getOrderId());
        this.setAmount(request.getAmount());
    }
}
