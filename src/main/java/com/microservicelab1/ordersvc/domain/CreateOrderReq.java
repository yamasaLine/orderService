package com.microservicelab1.ordersvc.domain;

import java.util.List;

public class CreateOrderReq {
    private int consumerId;
    private String paymentMethod;
    private String deliverAddr;
    private String deliverTime;
    private int restaurantId;
    private List<String> orderLineItems;

    public CreateOrderReq(int consumerId, String paymentMethod, String deliverAddr, String deliverTime, int restaurantId, List<String> orderLineItems) {
        this.consumerId = consumerId;
        this.paymentMethod = paymentMethod;
        this.deliverAddr = deliverAddr;
        this.deliverTime = deliverTime;
        this.restaurantId = restaurantId;
        this.orderLineItems = orderLineItems;
    }

    public int getConsumerId() {
        return consumerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDeliverAddr() {
        return deliverAddr;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public List<String> getOrderLineItems() {
        return orderLineItems;
    }
}
