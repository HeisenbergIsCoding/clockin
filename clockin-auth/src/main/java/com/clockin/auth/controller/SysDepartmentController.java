package com.clockin.auth.controller;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysDepartmentDTO;
import com.clockin.auth.service.SysDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部門控制器
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class SysDepartmentController {

    private final SysDepartmentService departmentService;

    @GetMapping("/tree")
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    public R<List<SysDepartmentDTO>> getDepartmentTree() {
        return R.success(departmentService.getDepartmentTree());
    }

    @GetMapping
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    public R<List<SysDepartmentDTO>> getAllDepartments(
            @RequestParam(required = false) Integer status) {
        return R.success(departmentService.getAllDepartments(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public R<SysDepartmentDTO> getDepartmentById(
            @PathVariable Long id) {
        return R.success(departmentService.getDepartmentById(id));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('system:dept:add')")
    public R<SysDepartmentDTO> addDepartment(@RequestBody SysDepartmentDTO departmentDTO) {
        return R.success(departmentService.addDepartment(departmentDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:dept:edit')")
    public R<SysDepartmentDTO> updateDepartment(
            @PathVariable Long id,
            @RequestBody SysDepartmentDTO departmentDTO) {
        return R.success(departmentService.updateDepartment(id, departmentDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:dept:delete')")
    public R<Void> deleteDepartment(
            @PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return R.success();
    }

    @GetMapping("/children/{parentId}")
    @PreAuthorize("@ss.hasPermission('system:dept:list')")
    public R<List<SysDepartmentDTO>> getChildDepartments(
            @PathVariable Long parentId) {
        return R.success(departmentService.getChildDepartments(parentId));
    }
}
