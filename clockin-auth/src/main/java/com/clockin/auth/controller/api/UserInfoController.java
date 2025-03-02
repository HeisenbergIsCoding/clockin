package com.clockin.auth.controller.api;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysUserDTO;
import com.clockin.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用戶信息控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private SysUserService userService;

    @GetMapping("/info")
    public R<SysUserDTO> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return R.success(userService.getUserByUsername(username));
    }

    @PutMapping("/info")
    public R<SysUserDTO> updateUserInfo(@RequestBody SysUserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SysUserDTO currentUser = userService.getUserByUsername(username);
        
        // 只允許修改自己的信息，且只能修改非敏感字段
        if (currentUser != null && currentUser.getId().equals(userDTO.getId())) {
            userDTO.setUsername(currentUser.getUsername()); // 不允許修改用戶名
            userDTO.setRoleIds(currentUser.getRoleIds()); // 不允許修改角色
            return R.success(userService.updateUser(userDTO.getId(), userDTO));
        }
        
        return R.fail("無權限修改此用戶信息");
    }

    @PostMapping("/avatar")
    public R<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return R.success(userService.updateAvatar(username, file));
    }
}
