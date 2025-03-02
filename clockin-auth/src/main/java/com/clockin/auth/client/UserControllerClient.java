package com.clockin.auth.client;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysDepartmentDTO;
import com.clockin.auth.dto.SysPositionDTO;
import com.clockin.auth.dto.SysUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用於模組間通信的用戶信息接口
 * <p>
 * 此控制器為內部服務間通信使用，不應暴露到外部網絡
 */
@RestController
@RequestMapping("/internal/api/user")
public interface UserControllerClient {

    @GetMapping("/{id}")
    R<SysUserDTO> getUserById(@PathVariable Long id);

    @GetMapping("/username/{username}")
    R<SysUserDTO> getUserByUsername(@PathVariable String username);

    @GetMapping("/dept/{departmentId}")
    R<List<SysUserDTO>> getUsersByDepartment(@PathVariable Long departmentId);

    @GetMapping("/dept/all")
    R<List<SysDepartmentDTO>> getAllDepartments();

    @GetMapping("/dept/{id}")
    R<SysDepartmentDTO> getDepartmentById(@PathVariable Long id);

    @GetMapping("/dept/tree")
    R<List<SysDepartmentDTO>> getDepartmentTree();

    @GetMapping("/positions")
    R<List<SysPositionDTO>> getAllPositions();

    @GetMapping("/position/{id}")
    R<SysPositionDTO> getPositionById(@PathVariable Long id);
}
