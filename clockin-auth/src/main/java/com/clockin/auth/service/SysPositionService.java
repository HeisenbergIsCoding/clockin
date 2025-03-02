package com.clockin.auth.service;

import com.clockin.auth.dto.SysPositionDTO;

import java.util.List;

/**
 * 崗位服務接口
 */
public interface SysPositionService {

    /**
     * 查詢崗位列表
     *
     * @param status 狀態過濾
     * @return 崗位列表
     */
    List<SysPositionDTO> getAllPositions(Integer status);

    /**
     * 根據ID獲取崗位
     *
     * @param id 崗位ID
     * @return 崗位信息
     */
    SysPositionDTO getPositionById(Long id);

    /**
     * 新增崗位
     *
     * @param positionDTO 崗位信息
     * @return 新增后的崗位
     */
    SysPositionDTO addPosition(SysPositionDTO positionDTO);

    /**
     * 更新崗位
     *
     * @param id 崗位ID
     * @param positionDTO 崗位信息
     * @return 更新后的崗位
     */
    SysPositionDTO updatePosition(Long id, SysPositionDTO positionDTO);

    /**
     * 刪除崗位
     *
     * @param id 崗位ID
     */
    void deletePosition(Long id);
}
