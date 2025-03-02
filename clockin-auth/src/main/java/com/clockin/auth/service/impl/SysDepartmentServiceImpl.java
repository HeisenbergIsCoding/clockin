package com.clockin.auth.service.impl;

import com.clockin.auth.dto.SysDepartmentDTO;
import com.clockin.auth.entity.SysDepartment;
import com.clockin.common.exception.BusinessException;
import com.clockin.auth.repository.SysDepartmentRepository;
import com.clockin.auth.repository.SysUserRepository;
import com.clockin.auth.service.SysDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部門服務實現類
 */
@Service
@CacheConfig(cacheNames = "department")
public class SysDepartmentServiceImpl implements SysDepartmentService {

    @Autowired
    private SysDepartmentRepository departmentRepository;
    
    @Autowired
    private SysUserRepository userRepository;

    @Override
    @Cacheable(key = "'tree'")
    public List<SysDepartmentDTO> getDepartmentTree() {
        // 查詢頂級部門
        List<SysDepartment> rootDepartments = departmentRepository.findByParentIdIsNullOrderByOrderNum();
        
        // 構建樹結構
        return buildDepartmentTree(rootDepartments);
    }

    @Override
    @Cacheable(key = "'all:' + #status")
    public List<SysDepartmentDTO> getAllDepartments(Integer status) {
        List<SysDepartment> departments;
        
        if (status != null) {
            departments = departmentRepository.findByStatus(status);
        } else {
            departments = departmentRepository.findAll();
        }
        
        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#id")
    public SysDepartmentDTO getDepartmentById(Long id) {
        SysDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部門不存在"));
        
        return convertToDTO(department);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public SysDepartmentDTO addDepartment(SysDepartmentDTO departmentDTO) {
        // 檢查部門編碼唯一性
        if (departmentDTO.getDeptCode() != null && 
                departmentRepository.findByDeptCode(departmentDTO.getDeptCode()) != null) {
            throw new BusinessException("部門編碼已存在");
        }
        
        // 檢查父部門是否存在
        if (departmentDTO.getParentId() != null) {
            departmentRepository.findById(departmentDTO.getParentId())
                    .orElseThrow(() -> new BusinessException("父部門不存在"));
        }
        
        SysDepartment department = new SysDepartment();
        BeanUtils.copyProperties(departmentDTO, department);
        
        // 設置默認狀態為啟用
        if (department.getStatus() == null) {
            department.setStatus(1);
        }
        
        department = departmentRepository.save(department);
        
        return convertToDTO(department);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public SysDepartmentDTO updateDepartment(Long id, SysDepartmentDTO departmentDTO) {
        SysDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部門不存在"));
        
        // 檢查部門編碼唯一性
        if (departmentDTO.getDeptCode() != null && 
                !departmentDTO.getDeptCode().equals(department.getDeptCode())) {
            SysDepartment existingDept = departmentRepository.findByDeptCode(departmentDTO.getDeptCode());
            if (existingDept != null && !existingDept.getId().equals(id)) {
                throw new BusinessException("部門編碼已存在");
            }
        }
        
        // 檢查父部門是否存在，且不能將自己設為父部門
        if (departmentDTO.getParentId() != null) {
            if (Objects.equals(departmentDTO.getParentId(), id)) {
                throw new BusinessException("父部門不能為自己");
            }
            
            // 檢查父部門是否為自己的子部門
            List<Long> childIds = getDepartmentAndChildIds(id);
            if (childIds.contains(departmentDTO.getParentId())) {
                throw new BusinessException("父部門不能為自己的子部門");
            }
            
            departmentRepository.findById(departmentDTO.getParentId())
                    .orElseThrow(() -> new BusinessException("父部門不存在"));
        }
        
        BeanUtils.copyProperties(departmentDTO, department);
        department.setId(id);  // 確保ID不變
        
        department = departmentRepository.save(department);
        
        return convertToDTO(department);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteDepartment(Long id) {
        // 檢查部門是否存在
        SysDepartment department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部門不存在"));
        
        // 檢查是否有子部門
        List<SysDepartment> children = departmentRepository.findByParentIdOrderByOrderNum(id);
        if (!children.isEmpty()) {
            throw new BusinessException("存在子部門，無法刪除");
        }
        
        // 檢查是否有用戶關聯
        long userCount = userRepository.countByDepartmentId(id);
        if (userCount > 0) {
            throw new BusinessException("部門下存在用戶，無法刪除");
        }
        
        departmentRepository.delete(department);
    }

    @Override
    @Cacheable(key = "'children:' + #parentId")
    public List<SysDepartmentDTO> getChildDepartments(Long parentId) {
        List<SysDepartment> children = departmentRepository.findByParentIdOrderByOrderNum(parentId);
        
        return children.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getDepartmentAndChildIds(Long departmentId) {
        List<Long> ids = new ArrayList<>();
        ids.add(departmentId);
        
        // 遞歸獲取所有子部門ID
        addChildDepartmentIds(departmentId, ids);
        
        return ids;
    }

    /**
     * 遞歸獲取子部門ID
     *
     * @param parentId 父部門ID
     * @param ids ID列表
     */
    private void addChildDepartmentIds(Long parentId, List<Long> ids) {
        List<SysDepartment> children = departmentRepository.findByParentIdOrderByOrderNum(parentId);
        
        for (SysDepartment child : children) {
            ids.add(child.getId());
            addChildDepartmentIds(child.getId(), ids);
        }
    }

    /**
     * 構建部門樹結構
     *
     * @param rootDepartments 頂級部門
     * @return 部門樹
     */
    private List<SysDepartmentDTO> buildDepartmentTree(List<SysDepartment> rootDepartments) {
        List<SysDepartmentDTO> tree = new ArrayList<>();
        
        for (SysDepartment dept : rootDepartments) {
            SysDepartmentDTO deptDTO = convertToDTO(dept);
            
            // 遞歸獲取子部門
            deptDTO.setChildren(buildChildrenDepartmentTree(dept.getId()));
            
            tree.add(deptDTO);
        }
        
        return tree;
    }

    /**
     * 遞歸構建子部門樹結構
     *
     * @param parentId 父部門ID
     * @return 子部門樹
     */
    private List<SysDepartmentDTO> buildChildrenDepartmentTree(Long parentId) {
        List<SysDepartment> children = departmentRepository.findByParentIdOrderByOrderNum(parentId);
        List<SysDepartmentDTO> childrenTree = new ArrayList<>();
        
        for (SysDepartment child : children) {
            SysDepartmentDTO childDTO = convertToDTO(child);
            
            // 遞歸獲取子部門
            childDTO.setChildren(buildChildrenDepartmentTree(child.getId()));
            
            childrenTree.add(childDTO);
        }
        
        return childrenTree;
    }

    /**
     * 將實體轉換為DTO
     *
     * @param department 部門實體
     * @return 部門DTO
     */
    private SysDepartmentDTO convertToDTO(SysDepartment department) {
        SysDepartmentDTO dto = new SysDepartmentDTO();
        BeanUtils.copyProperties(department, dto);
        
        // 如果有父部門，設置父部門名稱
        if (department.getParentId() != null) {
            departmentRepository.findById(department.getParentId())
                    .ifPresent(parent -> dto.setParentName(parent.getDeptName()));
        }
        
        return dto;
    }
}
