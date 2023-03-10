package com.tasc.paymentservice.service;

import com.tasc.paymentservice.domain.PaymentDomain;
import com.tasc.paymentservice.entity.OrderPaymentEntity;
import com.tasc.paymentservice.entity.UserBalance;
import com.tasc.paymentservice.repository.OrderPaymentRepository;
import com.tasc.paymentservice.repository.UserBalanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.ERROR;
import vn.tass.microservice.model.payment.dto.OrderPaymentData;
import vn.tass.microservice.model.payment.request.OrderPaymentRequest;
import vn.tass.microservice.model.payment.request.RollBackPaymentRequest;
import vn.tass.microservice.model.payment.response.OrderPaymentResponse;

import java.util.Optional;

@Service
@Log4j2
public class PaymentService {

    @Autowired
    OrderPaymentRepository orderPaymentRepository;

    @Autowired
    UserBalanceRepository userBalanceRepository;

    @Autowired
    PaymentDomain paymentDomain;

    @Transactional
    public OrderPaymentResponse createOrderPayment(OrderPaymentRequest request) throws ApplicationException {

    log.info("Created order payment - {}", request);

    this.validateRequest(request);

        UserBalance userBalance = paymentDomain.getBalanceInfoByUserId(request.getUserId());

        paymentDomain.validateUserBalance(userBalance, request.getRequireBalance());

        userBalance.setBalance(userBalance.getBalance() - request.getRequireBalance());

        userBalanceRepository.save(userBalance);

        OrderPaymentEntity orderPayment = new OrderPaymentEntity(request);

        orderPaymentRepository.save(orderPayment);

        OrderPaymentResponse response = new OrderPaymentResponse();

        OrderPaymentData data = new OrderPaymentData();
        data.setTransactionId(orderPayment.getOrderId());

        response.setData(data);
        log.info("created order payment transaction DONE");

        return response;
    }

    @Transactional
    public BaseResponse rollbackOrderPayment(RollBackPaymentRequest request) throws ApplicationException{

        validateRollback(request);

        Optional<OrderPaymentEntity> orderPaymentOpt = orderPaymentRepository.findById(request.getTransactionId());

        if (orderPaymentOpt.isEmpty()) {
            throw new ApplicationException(ERROR.ORDER_PAYMENT_TRANSACTION_INVALID);
        }

        OrderPaymentEntity orderPayment = orderPaymentOpt.get();

        orderPayment.setStatus(2);
        orderPaymentRepository.save(orderPayment);

        UserBalance userBalance = paymentDomain.getBalanceInfoByUserId(request.getUserId());

        userBalance.setBalance(userBalance.getBalance() + orderPayment.getAmount());

        userBalanceRepository.save(userBalance);

        return new BaseResponse();
    }

    private void validateRequest(OrderPaymentRequest request) {
        if (request.getAmount() < 1) {
            log.debug("order payment service failed - product amount invalid");
            throw new ApplicationException(ERROR.INVALID_PARAM, "product amount is invalid");
        }

        if (request.getUserId() < 1) {
            log.debug("order payment service failed - userId invalid");
            throw new ApplicationException(ERROR.INVALID_PARAM, "userId is invalid");
        }
    }

    private void validateRollback(RollBackPaymentRequest request) {
        if (request.getTransactionId() < 1) {
            log.debug("order payment rollback failed - invalid transactionId");
            throw new ApplicationException(ERROR.INVALID_PARAM, "invalid param");
        }

        if (request.getUserId() < 1) {
            log.debug("order payment rollback transaction fail - orderId invalid");
            throw new ApplicationException(ERROR.INVALID_PARAM, "order id is invalid");
        }
    }
}
