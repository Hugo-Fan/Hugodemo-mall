package com.hugo.hugodemomall.model;

import com.hugo.hugodemomall.constant.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
@Data
public class Product {
    @Schema(description = "商品ID", example = "1")
    private Integer productId;
    @Schema(description = "商品名稱", example = "蘋果")
    private String productName;
    @Schema(description = "商品分類", example = "FOOD、CAR、E_BOOK")
    private ProductCategory category;
    @Schema(description = "商品圖片url", example = "https://xxxx.jpg")
    private String  imageUrl;
    @Schema(description = "商品價格", example = "100")
    private Integer price;
    @Schema(description = "商品數量", example = "999")
    private Integer stock;
    @Schema(description = "商品描述", example = "來自富士山的蘋果")
    private String description;
    @Schema(description = "商品建立時間", example = "2024-51-06 22:51:10")
    private Date createdDate;
    @Schema(description = "商品最後修改時間", example = "2024-51-08 22:51:10")
    private Date lastModifiedDate;
}
