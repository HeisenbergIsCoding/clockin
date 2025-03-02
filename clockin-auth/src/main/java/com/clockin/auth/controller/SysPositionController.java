package com.clockin.auth.controller;

import com.clockin.auth.common.R;
import com.clockin.auth.dto.SysPositionDTO;
import com.clockin.auth.service.SysPositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 崗位控制器
 */
@RestController
@RequestMapping("/api/positions")
public class SysPositionController {

    @Autowired
    private SysPositionService positionService;

    @GetMapping
    @PreAuthorize("@ss.hasPermission('system:position:list')")
    public R<List<SysPositionDTO>> getAllPositions(
            @RequestParam(required = false) Integer status) {
        return R.success(positionService.getAllPositions(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:position:query')")
    public R<SysPositionDTO> getPositionById(
            @PathVariable Long id) {
        return R.success(positionService.getPositionById(id));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermission('system:position:add')")
    public R<SysPositionDTO> addPosition(@Valid @RequestBody SysPositionDTO positionDTO) {
        return R.success(positionService.addPosition(positionDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:position:edit')")
    public R<SysPositionDTO> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody SysPositionDTO positionDTO) {
        return R.success(positionService.updatePosition(id, positionDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ss.hasPermission('system:position:delete')")
    public R<Void> deletePosition(
            @PathVariable Long id) {
        positionService.deletePosition(id);
        return R.success();
    }
}
