package com.clockin.record.client;

import com.clockin.record.common.R;
import com.clockin.record.dto.DepartmentDTO;
import com.clockin.record.dto.PositionDTO;
import com.clockin.record.dto.SysUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * 認證服務客戶端
 * <p>
 * 使用 Spring Web Client 與認證服務進行通信，獲取用戶、部門和崗位信息
 */
@HttpExchange(url = "/internal/api/user")
public interface AuthServiceClient {

    /**
     * 根據ID獲取用戶信息
     *
     * @param id 用戶ID
     * @return 用戶信息
     */
    @GetExchange("/user/{id}")
    R<SysUserDTO> getUserById(@PathVariable("id") Long id);

    /**
     * 根據用戶名獲取用戶信息
     *
     * @param username 用戶名
     * @return 用戶信息
     */
    @GetExchange("/user/username/{username}")
    R<SysUserDTO> getUserByUsername(@PathVariable("username") String username);

    /**
     * 根據部門ID獲取用戶列表
     *
     * @param departmentId 部門ID
     * @return 用戶列表
     */
    @GetExchange("/user/dept/{departmentId}")
    R<List<SysUserDTO>> getUsersByDepartment(@PathVariable("departmentId") Long departmentId);

    /**
     * 獲取所有部門信息
     *
     * @return 部門列表
     */
    @GetExchange("/dept/all")
    R<List<DepartmentDTO>> getAllDepartments();

    /**
     * 根據ID獲取部門詳情
     *
     * @param id 部門ID
     * @return 部門詳情
     */
    @GetExchange("/dept/{id}")
    R<DepartmentDTO> getDepartmentById(@PathVariable("id") Long id);

    /**
     * 獲取部門樹結構
     *
     * @return 部門樹結構
     */
    @GetExchange("/dept/tree")
    R<List<DepartmentDTO>> getDepartmentTree();

    /**
     * 獲取所有崗位信息
     *
     * @return 崗位列表
     */
    @GetExchange("/positions")
    R<List<PositionDTO>> getAllPositions();

    /**
     * 根據ID獲取崗位詳情
     *
     * @param id 崗位ID
     * @return 崗位詳情
     */
    @GetExchange("/position/{id}")
    R<PositionDTO> getPositionById(@PathVariable("id") Long id);
}
