package com.clockin.auth.service.impl;

import com.clockin.auth.dto.SysUserDTO;
import com.clockin.auth.entity.SysUser;
import com.clockin.auth.repository.SysUserRepository;
import com.clockin.auth.service.SysUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系統用戶服務實現類
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository userRepository;

    @Override
    public SysUserDTO getUserByUsername(String username) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public SysUserDTO getUserById(Long id) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public Page<SysUserDTO> getUserPage(String username, String name, Integer status, Long departmentId, Pageable pageable) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public SysUserDTO addUser(SysUserDTO userDTO) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public SysUserDTO updateUser(Long id, SysUserDTO userDTO) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void deleteUser(Long id) {
        // 實際實現應該已存在
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteUser(List<Long> ids) {
        // 批量刪除用戶實現
        for (Long id : ids) {
            deleteUser(id);
        }
    }

    @Override
    public void updateUserStatus(Long id, Integer status) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public String resetUserPassword(Long id) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public SysUserDTO updateUserInfo(String username, SysUserDTO userDTO) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public String updateAvatar(String username, MultipartFile file) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public List<SysUserDTO> getUsersByDepartment(Long departmentId) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public List<SysUserDTO> getUsersByPosition(Long positionId) {
        // 實際實現應該已存在
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}
