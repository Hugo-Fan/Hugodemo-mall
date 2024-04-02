package com.hugo.hugodemomall.dto;

import com.hugo.hugodemomall.constant.ProductCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ProductRequest {
    @NotNull
    private String productName;
    @NotNull
    private ProductCategory category;
    @NotNull
    private String  imageUrl;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;

    private String description;

}
