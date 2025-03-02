package com.clockin.auth.service;

import com.clockin.auth.config.JwtConfig;
import com.clockin.auth.dto.LoginRequest;
import com.clockin.auth.dto.LoginResponse;
import com.clockin.auth.entity.SysUser;
import com.clockin.auth.repository.SysUserRepository;
import com.clockin.auth.util.JwtTokenUtil;
import com.clockin.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 認證服務
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final SysUserRepository userRepository;
    private final JwtConfig jwtConfig;

    /**
     * 用戶登錄
     *
     * @param loginRequest 登錄請求
     * @return 登錄響應
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // 通過 Spring Security 進行身份驗證
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            
            // 設置身份驗證信息到 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 獲取用戶信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 生成 JWT Token
            String token = jwtTokenUtil.generateToken(userDetails);
            
            // 獲取用戶角色
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            
            // 獲取用戶信息
            SysUser user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new BusinessException("用戶不存在"));
            
            // 構建登錄響應
            return LoginResponse.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .realName(user.getName())
                    .token(token)
                    .expiresIn(jwtConfig.getExpiration())
                    .roles(roles.toArray(new String[0]))
                    .build();
        } catch (Exception e) {
            log.error("用戶登錄失敗: {}", e.getMessage(), e);
            throw new BusinessException("用戶名或密碼錯誤");
        }
    }
    
    /**
     * 獲取當前登錄用戶
     *
     * @return 用戶信息
     */
    public SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
