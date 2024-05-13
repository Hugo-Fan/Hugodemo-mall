package com.hugo.hugodemomall.dto;

import com.hugo.hugodemomall.constant.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductRequest {
    @Schema(description = "商品名稱", example = "蘋果")
    @NotNull
    private String productName;

    @Schema(description = "商品分類", example = "FOOD、CAR、E_BOOK")
    @NotNull
    private ProductCategory category;

    @Schema(description = "商品圖片url", example = "https://xxxx.jpg")
    @NotNull
    private String  imageUrl;

    @Schema(description = "商品價格", example = "100")
    @NotNull
    private Integer price;

    @Schema(description = "商品數量", example = "999")
    @NotNull
    private Integer stock;

    @Schema(description = "商品描述(可以選擇不填)", example = "來自富士山的蘋果")
    private String description;

}
