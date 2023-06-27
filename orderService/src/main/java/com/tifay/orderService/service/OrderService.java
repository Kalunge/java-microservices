package com.tifay.orderService.service;

import com.tifay.orderService.model.OrderRequest;
import com.tifay.orderService.model.OrderResponse;
import lombok.Data;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
