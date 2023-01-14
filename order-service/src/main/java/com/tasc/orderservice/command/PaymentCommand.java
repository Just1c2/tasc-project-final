package com.tasc.orderservice.command;

import com.tasc.orderservice.connector.PaymentConnector;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.payment.request.OrderPaymentRequest;
import vn.tass.microservice.model.payment.response.OrderPaymentResponse;

@Component
@Log4j2
public class PaymentCommand {

    @Autowired
    PaymentConnector paymentConnector;

    public OrderPaymentResponse createdOrderPayment(long orderId, long userId, long amount, double requireBalance) throws ApplicationException {

        OrderPaymentRequest request = new OrderPaymentRequest();
        request.setOrderId(orderId);
        request.setUserId(userId);
        request.setAmount(amount);
        request.setRequireBalance(requireBalance);

        return paymentConnector.createTransaction(request);
    }

    public BaseResponse rollback(long transactionId) {
        return paymentConnector.rollback(transactionId);
    }
}
