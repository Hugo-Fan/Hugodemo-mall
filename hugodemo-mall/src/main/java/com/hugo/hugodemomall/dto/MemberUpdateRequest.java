package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdateRequest {
    @Email
    @NotBlank
    private String email;
    private String name;
    private Integer age;
}
