package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.model.Order;
import com.hugo.hugodemomall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    Integer createOrder(Integer userId, Integer totalAmount);

    void  createrOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
