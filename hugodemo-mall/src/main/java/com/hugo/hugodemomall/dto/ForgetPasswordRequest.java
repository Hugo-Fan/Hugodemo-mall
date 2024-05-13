package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordRequest {

    @Schema(description = "會員註冊時的Email，發送臨時密碼使用", example = "123456@gmail.com")
    @NotBlank
    private String email;
}
