package com.tasc.warehouseservice.repository;

import com.tasc.warehouseservice.entity.ProductWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductWarehouseRepository extends JpaRepository<ProductWarehouse, Long> {
}
