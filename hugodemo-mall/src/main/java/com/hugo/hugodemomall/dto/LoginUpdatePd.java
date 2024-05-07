package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUpdatePd {
        @NotBlank
        private String email;
        @NotBlank
        private String newPassword;
}
