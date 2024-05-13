package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
@Data
public class CreateOrderRequest {
    @Schema(description = "購買商品列表", example ="\"buyItemList\": [ { \"productId\": 1, \"quantity\": 10 }")
    @NotEmpty
    private List<BuyItem> buyItemList;
}
