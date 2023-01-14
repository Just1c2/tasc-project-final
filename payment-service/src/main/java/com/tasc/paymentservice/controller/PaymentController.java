package com.tasc.paymentservice.controller;

import com.tasc.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.payment.request.OrderPaymentRequest;
import vn.tass.microservice.model.payment.request.RollBackPaymentRequest;
import vn.tass.microservice.model.payment.response.OrderPaymentResponse;

@RestController
@RequestMapping("/transaction")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create")
    public OrderPaymentResponse createTransaction(@RequestBody OrderPaymentRequest request) throws
            ApplicationException{

        return paymentService.createOrderPayment(request);
    }

    @PutMapping("rollback")
    public BaseResponse rollbackTransaction(@RequestBody RollBackPaymentRequest request) throws
            ApplicationException{

        return paymentService.rollbackOrderPayment(request);
    }


}
