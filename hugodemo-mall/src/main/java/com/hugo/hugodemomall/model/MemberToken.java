package com.hugo.hugodemomall.model;

import lombok.Data;

import java.util.Date;

@Data
public class MemberToken {

    private Integer memberTokenId;
    private Integer memberId;
    private String token;
    private Date created_date;
}
