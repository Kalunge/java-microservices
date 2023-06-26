package com.tifay.orderService.service;

import com.tifay.orderService.model.OrderRequest;
import lombok.Data;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
