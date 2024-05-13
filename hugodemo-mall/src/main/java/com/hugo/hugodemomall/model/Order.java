package com.hugo.hugodemomall.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Order {

    @Schema(description = "訂單ID", example = "1")
    private Integer orderId;
    @Schema(description = "會員ID", example = "1")
    private Integer memberId;
    @Schema(description = "訂單總金額", example = "1")
    private Integer totalAmount;
    @Schema(description = "訂單新增時間", example = "2024-51-06 22:51:10")
    private Date createdDate;
    @Schema(description = "訂單最後修改時間", example = "2024-51-08 22:51:10")
    private Date lastModifiedDate;

    @Schema(description = "訂單商品明細")
    private List<OrderItem> orderItemList;
}
