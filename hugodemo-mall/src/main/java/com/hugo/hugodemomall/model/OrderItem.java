package com.hugo.hugodemomall.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderItem {
    @Schema(description = "訂單詳細商品清單ID", example = "1")
    private Integer orderItemId;
    @Schema(description = "訂單ID", example = "1")
    private Integer orderId;
    @Schema(description = "商品ID", example = "1")
    private Integer productId;
    @Schema(description = "商品數量", example = "10")
    private Integer quantity;
    @Schema(description = "單一商品總金額", example = "1000000")
    private Integer amount;
    @Schema(description = "商品名稱", example = "蘋果")
    private String productName;
    @Schema(description = "商品圖片uri", example = "https://xxxx.jpg")
    private String imageUrl;
}
