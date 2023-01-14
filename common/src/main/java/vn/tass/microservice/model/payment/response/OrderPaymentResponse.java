package vn.tass.microservice.model.payment.response;


import lombok.Data;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.payment.dto.OrderPaymentData;

@Data
public class OrderPaymentResponse extends BaseResponse {

    private OrderPaymentData data;
}
