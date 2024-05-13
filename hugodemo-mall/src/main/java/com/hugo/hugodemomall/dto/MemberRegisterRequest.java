package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberRegisterRequest {
    @Schema(description = "註冊會員使用", example = "123456@gmail.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "會員密碼", example = "123456", minLength = 6)
    @Size(min = 6)
    @NotBlank
    private String password;

    @Schema(description = "會員姓名(可以選擇不填)", example = "Hugo")
    private String name;

    @Schema(description = "年齡(可以選擇不填)", example = "18")
    private Integer age;
}
