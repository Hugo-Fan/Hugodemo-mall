package com.hugo.hugodemomall.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMerchantRequest {
    @Schema(description = "會員ID", example = "1")
    @NotNull
    private Integer memberId;
}
