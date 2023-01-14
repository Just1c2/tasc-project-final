package vn.tass.microservice.model.warehouse.request;

import lombok.Data;

@Data
public class CreateTransactionRequest {
    private long productId;
    private int total;
    private long orderId;
}
