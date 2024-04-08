package com.hugo.hugodemomall.service;

import com.hugo.hugodemomall.dto.CreateOrderRequest;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
