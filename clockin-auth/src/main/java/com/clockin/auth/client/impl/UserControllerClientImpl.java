package com.clockin.auth.client.impl;

import com.clockin.auth.client.UserControllerClient;
import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysDepartmentDTO;
import com.clockin.auth.dto.SysPositionDTO;
import com.clockin.auth.dto.SysUserDTO;
import com.clockin.auth.service.SysDepartmentService;
import com.clockin.auth.service.SysPositionService;
import com.clockin.auth.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用戶控制器客戶端實現，用於Feign內部調用
 * <p>
 * 實現了UserControllerClient接口，提供用戶、部門和崗位信息查詢功能
 */
@RestController
@RequiredArgsConstructor
public class UserControllerClientImpl implements UserControllerClient {

    private final SysUserService userService;
    private final SysDepartmentService departmentService;
    private final SysPositionService positionService;

    @Override
    public R<SysUserDTO> getUserById(@PathVariable("id") Long id) {
        return R.success(userService.getUserById(id));
    }

    @Override
    public R<SysUserDTO> getUserByUsername(@PathVariable("username") String username) {
        return R.success(userService.getUserByUsername(username));
    }

    @Override
    public R<List<SysUserDTO>> getUsersByDepartment(@PathVariable("departmentId") Long departmentId) {
        return R.success(userService.getUsersByDepartment(departmentId));
    }

    @Override
    public R<List<SysDepartmentDTO>> getAllDepartments() {
        return R.success(departmentService.getAllDepartments(null));
    }

    @Override
    public R<SysDepartmentDTO> getDepartmentById(@PathVariable("id") Long id) {
        return R.success(departmentService.getDepartmentById(id));
    }

    @Override
    public R<List<SysDepartmentDTO>> getDepartmentTree() {
        return R.success(departmentService.getDepartmentTree());
    }

    @Override
    public R<List<SysPositionDTO>> getAllPositions() {
        return R.success(positionService.getAllPositions(null));
    }

    @Override
    public R<SysPositionDTO> getPositionById(@PathVariable("id") Long id) {
        return R.success(positionService.getPositionById(id));
    }
}
