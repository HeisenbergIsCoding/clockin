package com.clockin.auth.service;

import com.clockin.auth.entity.SysUser;
import com.clockin.auth.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 用戶詳情服務實現
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserRepository userRepository;

    /**
     * 根據用戶名加載用戶詳情
     *
     * @param username 用戶名
     * @return 用戶詳情
     * @throws UsernameNotFoundException 用戶名未找到異常
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("用戶登入: {}", username);

        // 查詢用戶
        SysUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用戶不存在: " + username));

        // 檢查用戶狀態
        if (user.getStatus() != 1) {
            log.warn("用戶已被禁用: {}", username);
            throw new UsernameNotFoundException("用戶已被禁用");
        }

        // 更新最後登入時間
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 獲取用戶角色
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                // 添加權限
                .collect(Collectors.toList());

        // 添加用戶所有權限
        user.getRoles().forEach(role -> {
            if (role.getPermissions() != null) {
                role.getPermissions().forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                });
            }
        });

        // 返回 Spring Security 用戶
        return new User(
                user.getUsername(),
                user.getPassword(),
                true,       // 啟用
                true,       // 帳號未過期
                true,       // 憑證未過期
                true,       // 帳號未鎖定
                authorities
        );
    }
}
