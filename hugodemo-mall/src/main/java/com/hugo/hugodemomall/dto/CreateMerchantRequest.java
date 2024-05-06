package com.hugo.hugodemomall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMerchantRequest {
    @NotNull
    private Integer memberId;
}
