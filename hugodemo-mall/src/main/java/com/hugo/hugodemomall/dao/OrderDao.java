package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void  createrOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
