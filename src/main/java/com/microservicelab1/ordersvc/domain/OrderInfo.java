package com.microservicelab1.ordersvc.domain;

import java.util.UUID;

public class OrderInfo {
    public final String orderId;
    private OrderState orderState;
    private String promisedTime;

    public enum OrderState {
        PENDING_ACCEPTANCE, ACCEPTED
    }

    public OrderInfo() {
        this.orderId = UUID.randomUUID().toString();
    }
}
