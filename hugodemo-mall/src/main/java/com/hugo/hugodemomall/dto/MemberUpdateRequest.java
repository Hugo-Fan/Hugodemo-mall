package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdateRequest {

    @Schema(description = "會員Email，比對身份用", example = "123456@gmail.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "會員姓名(可以選擇不填)", example = "Hugo")
    private String name;

    @Schema(description = "年齡(可以選擇不填)", example = "18")
    private Integer age;
}
