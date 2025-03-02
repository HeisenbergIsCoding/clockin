package com.clockin.record.service.impl;

import com.clockin.record.client.AuthServiceClient;
import com.clockin.record.common.R;
import com.clockin.record.dto.DepartmentDTO;
import com.clockin.record.dto.PositionDTO;
import com.clockin.record.dto.SysUserDTO;
import com.clockin.record.exception.ApiException;
import com.clockin.record.service.UserService;
import com.clockin.record.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 用戶服務實現類
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Override
    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public SysUserDTO getUserById(Long id) {
        try {
            R<SysUserDTO> response = authServiceClient.getUserById(id);
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取用戶信息失敗: {}", response.getMessage());
            return null;
        } catch (Exception e) {
            log.error("調用認證服務獲取用戶信息異常", e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "users", key = "#username", unless = "#result == null")
    public SysUserDTO getUserByUsername(String username) {
        try {
            R<SysUserDTO> response = authServiceClient.getUserByUsername(username);
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取用戶信息失敗: {}", response.getMessage());
            return null;
        } catch (Exception e) {
            log.error("調用認證服務獲取用戶信息異常", e);
            return null;
        }
    }

    @Override
    public SysUserDTO getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        if (username == null) {
            throw new ApiException("未獲取到當前登錄用戶");
        }
        return getUserByUsername(username);
    }

    @Override
    @Cacheable(value = "usersByDept", key = "#departmentId", unless = "#result.isEmpty()")
    public List<SysUserDTO> getUsersByDepartment(Long departmentId) {
        try {
            R<List<SysUserDTO>> response = authServiceClient.getUsersByDepartment(departmentId);
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取部門用戶列表失敗: {}", response.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("調用認證服務獲取部門用戶列表異常", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = "departments", unless = "#result.isEmpty()")
    public List<DepartmentDTO> getAllDepartments() {
        try {
            R<List<DepartmentDTO>> response = authServiceClient.getAllDepartments();
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取所有部門信息失敗: {}", response.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("調用認證服務獲取所有部門信息異常", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = "departments", key = "#id", unless = "#result == null")
    public DepartmentDTO getDepartmentById(Long id) {
        try {
            R<DepartmentDTO> response = authServiceClient.getDepartmentById(id);
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取部門信息失敗: {}", response.getMessage());
            return null;
        } catch (Exception e) {
            log.error("調用認證服務獲取部門信息異常", e);
            return null;
        }
    }

    @Override
    @Cacheable(value = "departmentTree", unless = "#result.isEmpty()")
    public List<DepartmentDTO> getDepartmentTree() {
        try {
            R<List<DepartmentDTO>> response = authServiceClient.getDepartmentTree();
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取部門樹結構失敗: {}", response.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("調用認證服務獲取部門樹結構異常", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = "positions", unless = "#result.isEmpty()")
    public List<PositionDTO> getAllPositions() {
        try {
            R<List<PositionDTO>> response = authServiceClient.getAllPositions();
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取所有崗位信息失敗: {}", response.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("調用認證服務獲取所有崗位信息異常", e);
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = "positions", key = "#id", unless = "#result == null")
    public PositionDTO getPositionById(Long id) {
        try {
            R<PositionDTO> response = authServiceClient.getPositionById(id);
            if (R.isSuccess(response)) {
                return R.getData(response);
            }
            log.error("獲取崗位信息失敗: {}", response.getMessage());
            return null;
        } catch (Exception e) {
            log.error("調用認證服務獲取崗位信息異常", e);
            return null;
        }
    }
}
