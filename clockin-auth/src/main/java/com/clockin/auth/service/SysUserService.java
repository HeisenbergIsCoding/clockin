package com.clockin.auth.service;

import com.clockin.auth.dto.SysUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 系統用戶服務接口
 */
public interface SysUserService {

    /**
     * 根據用戶名獲取用戶
     *
     * @param username 用戶名
     * @return 用戶DTO
     */
    SysUserDTO getUserByUsername(String username);

    /**
     * 根據ID獲取用戶
     *
     * @param id 用戶ID
     * @return 用戶DTO
     */
    SysUserDTO getUserById(Long id);

    /**
     * 分頁查詢用戶列表
     *
     * @param username 用戶名（模糊查詢）
     * @param name 姓名（模糊查詢）
     * @param status 狀態
     * @param departmentId 部門ID
     * @param pageable 分頁參數
     * @return 用戶分頁結果
     */
    Page<SysUserDTO> getUserPage(String username, String name, Integer status, 
                               Long departmentId, Pageable pageable);

    /**
     * 新增用戶
     *
     * @param userDTO 用戶信息
     * @return 新增后的用戶
     */
    SysUserDTO addUser(SysUserDTO userDTO);

    /**
     * 更新用戶
     *
     * @param id 用戶ID
     * @param userDTO 用戶信息
     * @return 更新后的用戶
     */
    SysUserDTO updateUser(Long id, SysUserDTO userDTO);

    /**
     * 刪除用戶
     *
     * @param id 用戶ID
     */
    void deleteUser(Long id);

    /**
     * 更新用戶狀態
     *
     * @param id 用戶ID
     * @param status 狀態
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * 重置用戶密碼
     *
     * @param id 用戶ID
     * @return 重置后的密碼
     */
    String resetUserPassword(Long id);

    /**
     * 更新當前用戶信息
     *
     * @param username 當前用戶名
     * @param userDTO 用戶信息
     * @return 更新后的用戶
     */
    SysUserDTO updateUserInfo(String username, SysUserDTO userDTO);

    /**
     * 更新用戶密碼
     *
     * @param username 用戶名
     * @param oldPassword 舊密碼
     * @param newPassword 新密碼
     */
    void updatePassword(String username, String oldPassword, String newPassword);

    /**
     * 更新用戶頭像
     *
     * @param username 用戶名
     * @param file 頭像文件
     * @return 頭像URL
     */
    String updateAvatar(String username, MultipartFile file);

    /**
     * 根據部門ID查詢用戶列表
     *
     * @param departmentId 部門ID
     * @return 用戶列表
     */
    List<SysUserDTO> getUsersByDepartment(Long departmentId);

    /**
     * 根據崗位ID查詢用戶列表
     *
     * @param positionId 崗位ID
     * @return 用戶列表
     */
    List<SysUserDTO> getUsersByPosition(Long positionId);

    /**
     * 批量刪除用戶
     *
     * @param ids 用戶ID列表
     */
    void batchDeleteUser(List<Long> ids);
}
