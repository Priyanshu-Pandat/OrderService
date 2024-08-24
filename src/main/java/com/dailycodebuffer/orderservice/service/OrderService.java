package com.dailycodebuffer.orderservice.service;

import com.dailycodebuffer.orderservice.model.OrderRequest;
import com.dailycodebuffer.orderservice.model.OrderResponce;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);



    OrderResponce getOrderDetails(long orderId);
}
