package com.hugo.hugodemomall.dao;

import com.hugo.hugodemomall.dto.OrderQueryParams;
import com.hugo.hugodemomall.model.Order;
import com.hugo.hugodemomall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    Integer createOrder(Integer memberId, Integer totalAmount);
    Integer getOrderByTotalPrice (Integer memberId);

    void  createrOrderItems(Integer orderId, List<OrderItem> orderItemList);

    void deleteOrderById(Integer orderId);

    void deleteOrderItemsByOrderId(Integer orderId);


}
