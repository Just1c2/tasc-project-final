package com.tasc.orderservice.service;

import com.tasc.orderservice.command.PaymentCommand;
import com.tasc.orderservice.command.WarehouseCommand;
import com.tasc.orderservice.connector.ProductConnectorFeign;
import com.tasc.orderservice.entity.OrderEntity;
import com.tasc.orderservice.model.request.CreateOrderRequest;
import com.tasc.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.BaseResponseV2;
import vn.tass.microservice.model.ERROR;
import vn.tass.microservice.model.constants.ORDER;
import vn.tass.microservice.model.constants.PAYMENT;
import vn.tass.microservice.model.constants.WAREHOUSE;
import vn.tass.microservice.model.dto.product.ProductDTO;
import vn.tass.microservice.model.payment.response.OrderPaymentResponse;
import vn.tass.microservice.model.userauthen.UserDTO;
import vn.tass.microservice.model.warehouse.response.CreateTransactionResponse;

@Service
@Log4j2
public class OrderService extends BaseService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductConnectorFeign productConnectorFeign;

    @Autowired
    WarehouseCommand wareHouseCommand;

    @Autowired
    PaymentCommand paymentCommand;

    public BaseResponse createdOrder(CreateOrderRequest request) throws ApplicationException {
        log.info("created order with request {}", request);

        BaseResponse response = new BaseResponse();

        UserDTO userDTO = getUserDTO();

        if (request.getProductId() < 1 || request.getTotal() < 1) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        BaseResponseV2<ProductDTO> productInfoResponse = productConnectorFeign.findByIdConnect(request.getProductId());

        if (!productInfoResponse.isSuccess()) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        ProductDTO productDTO = productInfoResponse.getData();

        if (productDTO == null) {
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        OrderEntity order = new OrderEntity();

        order.setIsSuccess(ORDER.SUCCESS_STATUS.FAIL);
        order.setUserId(userDTO.getUserId());
        order.setStatus(ORDER.STATUS.CREATED);
        order.setProductId(request.getProductId());
        order.setTotalItems(request.getTotal());
        order.setTotalPrice(order.getTotalItems() * productDTO.getPrice());

        orderRepository.saveAndFlush(order);

        CreateTransactionResponse createTransactionResponse = wareHouseCommand.createWarehouseTransaction(request.getProductId(), request.getTotal(), order.getId());

        if (!createTransactionResponse.isSuccess()) {
            log.debug("error {}", createTransactionResponse);

            order.setStatus(ORDER.STATUS.FAIL);
            order.setErrorCode("");
            order.setPaymentStatus(0);
            order.setWarehouseStatus(WAREHOUSE.STATUS.FAIL);

            orderRepository.saveAndFlush(order);

            response.setMessage(createTransactionResponse.getMessage());
            response.setCode(createTransactionResponse.getCode());
            response.setErrorCode(createTransactionResponse.getErrorCode());

            return response;
        }

        order.setWarehouseStatus(WAREHOUSE.STATUS.SUCCESS);

        order.setWarehouseTransId(createTransactionResponse.getData().getTransactionId());

        order.setStatus(ORDER.STATUS.SUCCESS);

        orderRepository.saveAndFlush(order);

        OrderPaymentResponse paymentResponse = paymentCommand.createdOrderPayment(order.getId(), order.getUserId(), order.getTotalItems(), order.getTotalPrice());

        if (!paymentResponse.isSuccess()) {
            wareHouseCommand.rollbackTransaction(order.getWarehouseTransId(), order.getId());

            order.setWarehouseStatus(WAREHOUSE.STATUS.ROLLBACK);
            order.setWarehouseTransId(createTransactionResponse.getData().getTransactionId());

            order.setPaymentStatus(PAYMENT.STATUS.FAIL);

            order.setStatus(ORDER.STATUS.FAIL);

            orderRepository.saveAndFlush(order);

            response.setMessage(paymentResponse.getMessage());
            response.setCode(paymentResponse.getCode());
            response.setErrorCode(paymentResponse.getErrorCode());

            return response;
        }

        order.setPaymentStatus(PAYMENT.STATUS.SUCCESS);
        order.setPaymentTransId(paymentResponse.getData().getTransactionId());
        order.setStatus(ORDER.STATUS.SUCCESS);
        orderRepository.saveAndFlush(order);

        return new BaseResponse();
    }

}
