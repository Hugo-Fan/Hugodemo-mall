package com.hugo.hugodemomall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
@Data
public class Member {
    @Schema(description = "會員ID", example = "1")
    private Integer memberId;
    @Schema(description = "會員Email", example = "123456@gmail.com")
    private String email;

    @JsonIgnore // 不回傳密碼
    private String password;
    @Schema(description = "會員姓名", example = "Hugo")
    private String name;
    @Schema(description = "年齡", example = "18")
    private Integer age;
    @Schema(description = "會員註冊時間", example = "2024-51-06 22:51:10")
    private Date created_date;
    @Schema(description = "會員資料最後修改時間", example = "2024-51-08 22:51:10")
    private Date last_modified_date;

}
