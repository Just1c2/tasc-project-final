package vn.tass.microservice.model.payment.request;

import lombok.Data;

@Data
public class OrderPaymentRequest {
    private long orderId;
    private long amount;
    private long userId;
    private double requireBalance;
}
