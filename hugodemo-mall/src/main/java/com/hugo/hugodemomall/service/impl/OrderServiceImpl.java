package com.hugo.hugodemomall.service.impl;

import com.hugo.hugodemomall.dao.*;
import com.hugo.hugodemomall.dto.BuyItem;
import com.hugo.hugodemomall.dto.CreateOrderRequest;
import com.hugo.hugodemomall.dto.OrderQueryParams;
import com.hugo.hugodemomall.model.*;
import com.hugo.hugodemomall.service.OrderService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MemberDao memberDao;


    private Integer vipUpgradePrice = 10000;

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for (Order order:orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);
        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        // 檢查user 是否存在
        User user = userDao.getUserById(userId);

        if(user ==null){
            log.warn("不存在的 {} userId",userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem:createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            if(product == null){
                log.warn("商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {} ，欲購買數量 {}",buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //扣除商品庫存
            productDao.updateStock(product.getProductId(),product.getStock()-buyItem.getQuantity());

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

        // 檢查訂單總金額有沒有超過升級VIP的金額(一萬)
        Integer priceTotal = orderDao.getOrderByTotalPrice(userId);

        // 查詢VIP role權限ID
        Role normalRole = roleDao.getRoleByName("ROLE_VIP_MEMBER");

        // 查詢會員是否已經有VIP資格
        MemberHasRole memberHasRole = memberDao.getMemberHasRoleByMemberId(userId,normalRole.getRoleId());

        // 滿足金額和目前VIP的話添加VIP權限
        if(priceTotal>=vipUpgradePrice && memberHasRole ==null){
            memberDao.addRoleForMemberId(userId,normalRole);
        }

        return orderId;
    }
}
