package vn.tass.microservice.model.warehouse.request;

import lombok.Data;

@Data
public class RollbackTransactionRequest {
    private long orderId;
    private long transactionId;
}
