package com.clockin.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登錄請求DTO
 */
@Data
@Schema(description = "登錄請求")
public class LoginRequest {

    @Schema(description = "用戶名", example = "admin", required = true)
    @NotBlank(message = "用戶名不能為空")
    private String username;

    @Schema(description = "密碼", example = "password", required = true)
    @NotBlank(message = "密碼不能為空")
    private String password;
}
