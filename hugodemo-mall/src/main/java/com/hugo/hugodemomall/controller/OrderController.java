package com.hugo.hugodemomall.controller;

import com.hugo.hugodemomall.dto.CreateOrderRequest;
import com.hugo.hugodemomall.dto.OrderQueryParams;
import com.hugo.hugodemomall.model.Order;
import com.hugo.hugodemomall.service.OrderService;
import com.hugo.hugodemomall.util.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Operation(
            summary = "查詢購物訂單",
            description = """
                    查詢會員的購物訂單  
                    可以在URL加入以下參數  
                    limit  可以選擇顯示幾筆  
                    offset 可以選擇跳過幾筆
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "成功取得購物訂單"
                    )
            }

    )
    @GetMapping("/members/{memberId}/orders")
    public ResponseEntity<Page<Order>> getOrders(
            @Parameter(description = "會員ID", required = true)
            @PathVariable Integer memberId,

            @Parameter(description = "顯示訂單數量")
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,

            @Parameter(description = "跳過訂單數量")
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
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

    @Operation(
            summary = "新增購物訂單",
            description = "添加購物訂單",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "成功新增購物訂單"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "缺少訂單資料",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "不存在的memberId,商品不存在，商品庫存數量不足",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/members/{memberId}/add/orders")
    public ResponseEntity<Order> createOrder(
                            @Parameter(description = "會員ID", required = true)
                            @PathVariable Integer memberId,
                            @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        Integer orderId = orderService.createOrder(memberId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(
            summary = "刪除購物訂單",
            description = "刪除購物訂單",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "成功刪除購物訂單",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "找不到商品",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "不存在的orderId",
                            content = @Content()
                    )
            }
    )
    @DeleteMapping("/members/orders/{memberId}")
    public ResponseEntity<?> deleteOrder(
            @Parameter(description = "會員ID", required = true)
            @PathVariable Integer memberId) {
        orderService.deleteOrderById(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
