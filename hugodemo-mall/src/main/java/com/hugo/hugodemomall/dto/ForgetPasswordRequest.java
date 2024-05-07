package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordRequest {
    @NotBlank
    private String email;
}
