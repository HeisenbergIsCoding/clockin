package com.clockin.auth.controller;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.LoginRequest;
import com.clockin.auth.dto.LoginResponse;
import com.clockin.auth.entity.SysUser;
import com.clockin.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 認證控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用戶登錄
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return R.success(loginResponse);
    }

    /**
     * 檢查用戶是否登入
     */
    @GetMapping("/check")
    public R<SysUser> checkLogin() {
        SysUser currentUser = authService.getCurrentUser();
        return R.success(currentUser);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        // JWT 無需在服務端處理登出，前端刪除令牌即可
        return R.success();
    }
}
