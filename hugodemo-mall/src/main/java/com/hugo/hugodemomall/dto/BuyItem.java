package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuyItem {
    @Schema(description = "商品ID", example = "1")
    @NotNull
    private Integer productId;

    @Schema(description = "購買數量", example = "5")
    @NotNull
    private Integer quantity;
}
