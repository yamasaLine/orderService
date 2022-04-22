package com.microservicelab1.ordersvc.rpc;

import org.springframework.stereotype.Component;

@Component
public class KitchenClient {
    public static final String URI = "ticket/create/{customerId}";

    public String createTicket(final String customerId) {
        return null;
    }

}
