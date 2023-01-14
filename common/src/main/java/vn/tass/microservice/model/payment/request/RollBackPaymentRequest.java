package vn.tass.microservice.model.payment.request;

import lombok.Data;

@Data
public class RollBackPaymentRequest {

    private long userId;
    private long transactionId;
}
