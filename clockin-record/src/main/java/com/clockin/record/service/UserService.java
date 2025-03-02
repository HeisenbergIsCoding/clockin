package com.clockin.record.service;

import com.clockin.record.dto.DepartmentDTO;
import com.clockin.record.dto.PositionDTO;
import com.clockin.record.dto.SysUserDTO;

import java.util.List;

/**
 * 用戶服務接口
 * <p>
 * 用於從auth模組獲取用戶、部門和崗位信息
 */
public interface UserService {

    /**
     * 根據ID獲取用戶信息
     *
     * @param id 用戶ID
     * @return 用戶信息
     */
    SysUserDTO getUserById(Long id);

    /**
     * 根據用戶名獲取用戶信息
     *
     * @param username 用戶名
     * @return 用戶信息
     */
    SysUserDTO getUserByUsername(String username);

    /**
     * 獲取當前登錄用戶信息
     *
     * @return 當前用戶信息
     */
    SysUserDTO getCurrentUser();

    /**
     * 根據部門ID獲取用戶列表
     *
     * @param departmentId 部門ID
     * @return 用戶列表
     */
    List<SysUserDTO> getUsersByDepartment(Long departmentId);

    /**
     * 獲取所有部門信息
     *
     * @return 部門列表
     */
    List<DepartmentDTO> getAllDepartments();

    /**
     * 根據ID獲取部門詳情
     *
     * @param id 部門ID
     * @return 部門詳情
     */
    DepartmentDTO getDepartmentById(Long id);

    /**
     * 獲取部門樹結構
     *
     * @return 部門樹結構
     */
    List<DepartmentDTO> getDepartmentTree();

    /**
     * 獲取所有崗位信息
     *
     * @return 崗位列表
     */
    List<PositionDTO> getAllPositions();

    /**
     * 根據ID獲取崗位詳情
     *
     * @param id 崗位ID
     * @return 崗位詳情
     */
    PositionDTO getPositionById(Long id);
}
