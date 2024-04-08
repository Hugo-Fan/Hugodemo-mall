package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.CreateOrderRequest;
import com.hugo.hugodemomall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
