package com.clockin.auth.service.impl;

import com.clockin.auth.dto.SysPositionDTO;
import com.clockin.auth.entity.SysPosition;
import com.clockin.common.exception.BusinessException;
import com.clockin.auth.repository.SysPositionRepository;
import com.clockin.auth.repository.SysUserRepository;
import com.clockin.auth.service.SysPositionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 崗位服務實現類
 */
@Service
@CacheConfig(cacheNames = "position")
public class SysPositionServiceImpl implements SysPositionService {

    @Autowired
    private SysPositionRepository positionRepository;
    
    @Autowired
    private SysUserRepository userRepository;

    @Override
    @Cacheable(key = "'all:' + #status")
    public List<SysPositionDTO> getAllPositions(Integer status) {
        List<SysPosition> positions;
        
        if (status != null) {
            positions = positionRepository.findByStatus(status);
        } else {
            positions = positionRepository.findAllByOrderByOrderNum();
        }
        
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#id")
    public SysPositionDTO getPositionById(Long id) {
        SysPosition position = positionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("崗位不存在"));
        
        return convertToDTO(position);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public SysPositionDTO addPosition(SysPositionDTO positionDTO) {
        // 檢查崗位編碼唯一性
        if (positionDTO.getPositionCode() != null && 
                positionRepository.findByPositionCode(positionDTO.getPositionCode()) != null) {
            throw new BusinessException("崗位編碼已存在");
        }
        
        SysPosition position = new SysPosition();
        BeanUtils.copyProperties(positionDTO, position);
        
        // 設置默認狀態為啟用
        if (position.getStatus() == null) {
            position.setStatus(1);
        }
        
        position = positionRepository.save(position);
        
        return convertToDTO(position);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public SysPositionDTO updatePosition(Long id, SysPositionDTO positionDTO) {
        SysPosition position = positionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("崗位不存在"));
        
        // 檢查崗位編碼唯一性
        if (positionDTO.getPositionCode() != null && 
                !positionDTO.getPositionCode().equals(position.getPositionCode())) {
            SysPosition existingPos = positionRepository.findByPositionCode(positionDTO.getPositionCode());
            if (existingPos != null && !existingPos.getId().equals(id)) {
                throw new BusinessException("崗位編碼已存在");
            }
        }
        
        BeanUtils.copyProperties(positionDTO, position);
        position.setId(id);  // 確保ID不變
        
        position = positionRepository.save(position);
        
        return convertToDTO(position);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deletePosition(Long id) {
        // 檢查崗位是否存在
        SysPosition position = positionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("崗位不存在"));
        
        // 檢查是否有用戶關聯
        long userCount = userRepository.countByPositionId(id);
        if (userCount > 0) {
            throw new BusinessException("崗位下存在用戶，無法刪除");
        }
        
        positionRepository.delete(position);
    }

    /**
     * 將實體轉換為DTO
     *
     * @param position 崗位實體
     * @return 崗位DTO
     */
    private SysPositionDTO convertToDTO(SysPosition position) {
        SysPositionDTO dto = new SysPositionDTO();
        BeanUtils.copyProperties(position, dto);
        return dto;
    }
}
