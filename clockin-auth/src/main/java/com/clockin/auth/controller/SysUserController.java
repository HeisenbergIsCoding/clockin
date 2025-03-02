package com.clockin.auth.controller;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysUserDTO;
import com.clockin.auth.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系統用戶管理控制器
 */
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 分頁查詢用戶列表
     *
     * @param username     用戶名（可選）
     * @param name         姓名（可選）
     * @param status       狀態（可選）
     * @param departmentId 部門ID（可選）
     * @param pageable     分頁參數
     * @return 用戶分頁列表
     */
    @GetMapping("/page")
    public R<Page<SysUserDTO>> getUserPage(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long departmentId,
            Pageable pageable) {
        return R.success(userService.getUserPage(username, name, status, departmentId, pageable));
    }

    /**
     * 根據ID獲取用戶詳情
     *
     * @param id 用戶ID
     * @return 用戶詳情
     */
    @GetMapping("/{id}")
    public R<SysUserDTO> getUserById(@PathVariable Long id) {
        return R.success(userService.getUserById(id));
    }

    /**
     * 添加用戶
     *
     * @param userDTO 用戶信息
     * @return 添加後的用戶信息
     */
    @PostMapping
    public R<SysUserDTO> addUser(@RequestBody SysUserDTO userDTO) {
        return R.success(userService.addUser(userDTO));
    }

    /**
     * 修改用戶
     *
     * @param id      用戶ID
     * @param userDTO 用戶信息
     * @return 修改後的用戶信息
     */
    @PutMapping("/{id}")
    public R<SysUserDTO> updateUser(@PathVariable Long id, @RequestBody SysUserDTO userDTO) {
        return R.success(userService.updateUser(id, userDTO));
    }

    /**
     * 刪除用戶
     *
     * @param id 用戶ID
     * @return 成功響應
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.success();
    }

    /**
     * 批量刪除用戶
     *
     * @param ids 用戶ID列表
     * @return 成功響應
     */
    @DeleteMapping("/batch")
    public R<Void> batchDeleteUser(@RequestBody List<Long> ids) {
        userService.batchDeleteUser(ids);
        return R.success();
    }

    /**
     * 重置密碼
     *
     * @param id 用戶ID
     * @return 重置後的密碼
     */
    @PostMapping("/{id}/reset-password")
    public R<String> resetPassword(@PathVariable Long id) {
        String password = userService.resetUserPassword(id);
        return R.success(password);
    }

    /**
     * 根據部門ID獲取用戶列表
     *
     * @param departmentId 部門ID
     * @return 用戶列表
     */
    @GetMapping("/dept/{departmentId}")
    public R<List<SysUserDTO>> getUsersByDepartment(@PathVariable Long departmentId) {
        return R.success(userService.getUsersByDepartment(departmentId));
    }

    /**
     * 根據崗位ID獲取用戶列表
     *
     * @param positionId 崗位ID
     * @return 用戶列表
     */
    @GetMapping("/position/{positionId}")
    public R<List<SysUserDTO>> getUsersByPosition(@PathVariable Long positionId) {
        return R.success(userService.getUsersByPosition(positionId));
    }
}
