package com.hugo.hugodemomall.model;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    private Integer userId;
    private String email;
    private String password;
    private Date created_date;
    private Date last_modified_date;
}
