package com.hugo.hugodemomall.controller;

import com.hugo.hugodemomall.dto.CreateOrderRequest;
import com.hugo.hugodemomall.dto.OrderQueryParams;
import com.hugo.hugodemomall.model.Order;
import com.hugo.hugodemomall.service.OrderService;
import com.hugo.hugodemomall.util.Page;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/members/{memberId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer memberId,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setMemberId(memberId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        //取得 order List
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        // 取得order 總數
        Integer count = orderService.countOrder(orderQueryParams);

        // 分頁

        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @PostMapping("/members/{memberId}/add/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer memberId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
       Integer orderId = orderService.createOrder(memberId,createOrderRequest);

       Order order = orderService.getOrderById(orderId);

       return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @DeleteMapping("/members/orders/{memberId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer memberId){
        orderService.deleteOrderById(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
