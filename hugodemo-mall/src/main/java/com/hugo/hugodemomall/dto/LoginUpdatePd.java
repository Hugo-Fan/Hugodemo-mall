package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUpdatePd {
        @Schema(description = "比對是不是會員本人使用", example = "123456@gmail.com")
        @NotBlank
        @Email
        private String email;

        @Schema(description = "舊密碼", example = "123456", minLength = 6)
        @NotBlank
        @Size(min = 5)
        private String oldPassword;

        @Schema(description = "新密碼", example = "123456", minLength = 6)
        @NotBlank
        @Size(min = 5)
        private String newPassword;
}
