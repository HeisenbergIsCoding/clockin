package com.clockin.auth.service;

import com.clockin.auth.dto.SysDepartmentDTO;
import com.clockin.auth.entity.SysDepartment;

import java.util.List;

/**
 * 部門服務接口
 */
public interface SysDepartmentService {

    /**
     * 獲取部門樹結構
     *
     * @return 部門樹列表
     */
    List<SysDepartmentDTO> getDepartmentTree();

    /**
     * 查詢部門列表
     *
     * @param status 狀態過濾
     * @return 部門列表
     */
    List<SysDepartmentDTO> getAllDepartments(Integer status);

    /**
     * 根據ID獲取部門
     *
     * @param id 部門ID
     * @return 部門信息
     */
    SysDepartmentDTO getDepartmentById(Long id);

    /**
     * 新增部門
     *
     * @param departmentDTO 部門信息
     * @return 新增后的部門
     */
    SysDepartmentDTO addDepartment(SysDepartmentDTO departmentDTO);

    /**
     * 更新部門
     *
     * @param id 部門ID
     * @param departmentDTO 部門信息
     * @return 更新后的部門
     */
    SysDepartmentDTO updateDepartment(Long id, SysDepartmentDTO departmentDTO);

    /**
     * 刪除部門
     *
     * @param id 部門ID
     */
    void deleteDepartment(Long id);

    /**
     * 獲取子部門
     *
     * @param parentId 父部門ID
     * @return 子部門列表
     */
    List<SysDepartmentDTO> getChildDepartments(Long parentId);

    /**
     * 查詢部門的所有子部門ID（包括自身）
     *
     * @param departmentId 部門ID
     * @return 部門及其子部門ID列表
     */
    List<Long> getDepartmentAndChildIds(Long departmentId);
}
