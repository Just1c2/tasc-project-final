package vn.tass.microservice.model.warehouse.response;

import lombok.Data;
import vn.tass.microservice.model.BaseResponse;
import vn.tass.microservice.model.warehouse.dto.CreateTransactionData;

@Data
public class CreateTransactionResponse extends BaseResponse {
    private CreateTransactionData data;
}
