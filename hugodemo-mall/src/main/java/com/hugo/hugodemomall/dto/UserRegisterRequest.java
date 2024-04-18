package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    private String name;

    private Integer age;
}
