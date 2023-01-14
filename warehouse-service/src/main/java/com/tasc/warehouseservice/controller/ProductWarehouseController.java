package com.tasc.warehouseservice.controller;


import com.tasc.warehouseservice.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.warehouse.request.CreateTransactionRequest;
import vn.tass.microservice.model.warehouse.request.RollbackTransactionRequest;
import vn.tass.microservice.model.warehouse.response.CreateTransactionResponse;

@RestController
@RequestMapping("/transaction")
public class ProductWarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @PostMapping("/create")
    public CreateTransactionResponse createTransaction(@RequestBody CreateTransactionRequest request) throws
            ApplicationException{

        return warehouseService.createTransactionResponse(request);
    }

    @PutMapping("/rollback")
    public BaseResponse createTransaction(@RequestBody RollbackTransactionRequest request) throws
            ApplicationException {
        return warehouseService.rollbackTransaction(request);

    }
}
