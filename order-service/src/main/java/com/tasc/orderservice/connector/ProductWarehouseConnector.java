package com.tasc.orderservice.connector;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.warehouse.request.CreateTransactionRequest;
import vn.tass.microservice.model.warehouse.request.RollbackTransactionRequest;
import vn.tass.microservice.model.warehouse.response.CreateTransactionResponse;

@FeignClient(value = "warehouse-service", url = "${tasc.warehouse.address}")
public interface ProductWarehouseConnector {

    @PostMapping("/create")
    CreateTransactionResponse createTransaction(@RequestBody CreateTransactionRequest request);

    @PutMapping("/rollback")
    BaseResponse rollbackTransaction(@RequestBody RollbackTransactionRequest request);

}
