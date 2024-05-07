package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ForgetUpdatePd {
        @NotBlank
        private String email;
        @NotBlank
        private String token;
        @NotBlank
        private String newPassword;
}
