package com.tasc.warehouseservice.domain;

import com.tasc.warehouseservice.entity.ProductWarehouse;
import com.tasc.warehouseservice.repository.ProductWarehouseRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.ERROR;

import java.util.Optional;

@Component
@Log4j2
public class ProductWarehouseDomain {

    @Autowired
    private ProductWarehouseRepository productWarehouseRepository;

    public ProductWarehouse getProductWarehouseInfoByProductId(long productId) throws
            ApplicationException {
        Optional<ProductWarehouse> productWareHouseOpt =
                productWarehouseRepository.findById(productId);

        if (productWareHouseOpt.isEmpty()) {
            log.debug("not load warehouse info by productId {}", productId);
            throw new ApplicationException(ERROR.INVALID_PARAM, "param total is invalid");
        }

        return productWareHouseOpt.get();

    }

    public void validateWarehouseTotal(ProductWarehouse productWarehouse , long total) throws ApplicationException{
        if (productWarehouse.getTotal() < total) {
            log.debug("warehouse productID {} - total {} - request total {}", productWarehouse.getProductId(),
                    productWarehouse.getTotal(), total);

            throw new ApplicationException(ERROR.TOTAL_PRODUCT_INVALID);
        }
    }



}
