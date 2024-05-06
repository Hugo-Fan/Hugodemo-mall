package com.hugo.hugodemomall.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Order {

    private Integer orderId;
    private Integer memberId;
    private Integer totalAmount;
    private Date createdDate;
    private Date lastModifiedDate;

    private List<OrderItem> orderItemList;
}
