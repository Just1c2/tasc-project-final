package com.tasc.orderservice.connector;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.payment.request.OrderPaymentRequest;
import vn.tass.microservice.model.payment.response.OrderPaymentResponse;

@FeignClient(value = "payment-service", url = "${tasc.payment.address}")
public interface PaymentConnector {

    @PostMapping("/create")
    OrderPaymentResponse createTransaction(@RequestBody OrderPaymentRequest request);

    @PutMapping("rollback/{transId}")
    BaseResponse rollback(@PathVariable(name = "transId") long transactionId);
}
