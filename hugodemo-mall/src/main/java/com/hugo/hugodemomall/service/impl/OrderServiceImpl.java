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
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MemberDao memberDao;


    private Integer vipUpgradePrice = 10000;

//    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

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
    public Integer createOrder(Integer memberId, CreateOrderRequest createOrderRequest) {


        // 檢查user 是否存在
        Member member = memberDao.getMemberById(memberId);

        if(member ==null){
            log.warn("不存在的 {} memberId",memberId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for(BuyItem buyItem:createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            if(product == null){
                log.warn("商品 {} 不存在",buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {} ，欲購買數量 {}",buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
        Integer orderId = orderDao.createOrder(memberId,totalAmount);

        orderDao.createrOrderItems(orderId,orderItemList);

        // 檢查訂單總金額有沒有超過升級VIP的金額(一萬)
        Integer priceTotal = orderDao.getOrderByTotalPrice(memberId);

        // 查詢VIP role權限ID
        Role normalRole = roleDao.getRoleByName("ROLE_VIP_MEMBER");

        // 查詢會員是否已經有VIP資格
        MemberHasRole memberHasRole = memberDao.getMemberHasRoleByMemberId(memberId,normalRole.getRoleId());

        // 滿足金額和目前VIP的話添加VIP權限
        if(priceTotal>=vipUpgradePrice && memberHasRole ==null){
            memberDao.addRoleForMemberId(memberId,normalRole);
        }

        return orderId;
    }

    @Transactional
    @Override
    public void deleteOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        if(order==null){
            log.warn("沒有此訂單ID {}",orderId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 查詢order明細
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        // 刪除order和orderItem
        orderDao.deleteOrderById(orderId);
        orderDao.deleteOrderItemsByOrderId(orderId);

        // 將刪除訂單的商品加回庫存
        for(OrderItem orderItem:orderItemList){
            // 查詢原商品數量
            Product product = productDao.getProductById(orderItem.getProductId());
            if(product == null){
                log.warn("找不到此商品 {},ProductId: {}",orderItem.getProductName(),orderItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            // 添加商品數量回去回去
            productDao.updateStock(orderItem.getProductId(),product.getStock()+orderItem.getQuantity());
        }
    }
}
