package com.clockin.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登錄響應DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登錄響應")
public class LoginResponse {

    @Schema(description = "用戶ID")
    private Long userId;

    @Schema(description = "用戶名")
    private String username;

    @Schema(description = "真實姓名")
    private String realName;

    @Schema(description = "JWT令牌")
    private String token;

    @Schema(description = "令牌過期時間(秒)")
    private Long expiresIn;

    @Schema(description = "角色列表")
    private String[] roles;
}
