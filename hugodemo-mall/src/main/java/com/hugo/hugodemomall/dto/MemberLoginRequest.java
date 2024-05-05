package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberLoginRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
