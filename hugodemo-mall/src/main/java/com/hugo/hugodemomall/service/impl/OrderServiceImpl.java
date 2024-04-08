package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.OrderDao;
import com.hugo.hugodemomall.dao.ProductDao;
import com.hugo.hugodemomall.dto.BuyItem;
import com.hugo.hugodemomall.dto.CreateOrderRequest;
import com.hugo.hugodemomall.model.OrderItem;
import com.hugo.hugodemomall.model.Product;
import com.hugo.hugodemomall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem:createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            // 計算總價格
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            // 轉換BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }


        // 創建訂單
        Integer orderId = orderDao.createOrder(userId,totalAmount);

        orderDao.createrOrderItems(orderId,orderItemList);

        return orderId;
    }
}
