package com.tasc.warehouseservice.entity;

import lombok.Data;
import vn.tass.microservice.model.warehouse.request.CreateTransactionRequest;

import javax.persistence.*;


@Entity
@Data
@Table(name = "order_transaction")
public class OrderTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;

    private long orderId;

    private long total;

    private int status;

    public OrderTransaction(){

    }

    public OrderTransaction(CreateTransactionRequest request){
        this.setStatus(1);
        this.setProductId(request.getProductId());
        this.setTotal(request.getTotal());
        this.setOrderId(request.getOrderId());
    }
}
