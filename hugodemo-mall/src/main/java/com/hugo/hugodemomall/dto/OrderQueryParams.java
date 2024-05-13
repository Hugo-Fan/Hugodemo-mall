package com.hugo.hugodemomall.dto;

import lombok.Data;

@Data
public class OrderQueryParams {
    private Integer memberId;
    private Integer limit;
    private Integer offset;
}
