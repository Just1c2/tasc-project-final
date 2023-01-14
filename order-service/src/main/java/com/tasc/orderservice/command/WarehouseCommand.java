package com.tasc.orderservice.command;

import com.tasc.orderservice.connector.ProductWarehouseConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.warehouse.request.CreateTransactionRequest;
import vn.tass.microservice.model.warehouse.request.RollbackTransactionRequest;
import vn.tass.microservice.model.warehouse.response.CreateTransactionResponse;

@Component
public class WarehouseCommand {

    @Autowired
    ProductWarehouseConnector productWarehouseConnector;

    public CreateTransactionResponse createWarehouseTransaction(long productId, int total, long orderId) throws ApplicationException {
        CreateTransactionRequest request = new CreateTransactionRequest();

        request.setProductId(productId);
        request.setTotal(total);
        request.setOrderId(orderId);

        return productWarehouseConnector.createTransaction(request);
    }

    public BaseResponse rollbackTransaction(long transactionId , long orderId){

        RollbackTransactionRequest request = new RollbackTransactionRequest();
        request.setOrderId(orderId);
        request.setTransactionId(transactionId);

        return productWarehouseConnector.rollbackTransaction(request);
    }
}
