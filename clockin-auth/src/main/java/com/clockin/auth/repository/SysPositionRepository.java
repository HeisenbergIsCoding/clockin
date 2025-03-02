package com.clockin.auth.repository;

import com.clockin.auth.entity.SysPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 崗位資料訪問層
 */
@Repository
public interface SysPositionRepository extends JpaRepository<SysPosition, Long> {

    /**
     * 根據狀態查詢崗位列表
     *
     * @param status 狀態
     * @return 崗位列表
     */
    List<SysPosition> findByStatus(Integer status);

    /**
     * 根據排序查詢崗位列表
     *
     * @return 崗位列表
     */
    List<SysPosition> findAllByOrderByOrderNum();

    /**
     * 根據崗位編碼查詢崗位
     *
     * @param positionCode 崗位編碼
     * @return 崗位
     */
    SysPosition findByPositionCode(String positionCode);
}
