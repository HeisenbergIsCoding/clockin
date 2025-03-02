package com.clockin.auth.repository;

import com.clockin.auth.entity.SysDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部門資料訪問層
 */
@Repository
public interface SysDepartmentRepository extends JpaRepository<SysDepartment, Long> {

    /**
     * 根據狀態查詢部門列表
     *
     * @param status 狀態
     * @return 部門列表
     */
    List<SysDepartment> findByStatus(Integer status);

    /**
     * 根據父部門ID查詢子部門列表
     *
     * @param parentId 父部門ID
     * @return 子部門列表
     */
    List<SysDepartment> findByParentIdOrderByOrderNum(Long parentId);

    /**
     * 根據部門編碼查詢部門
     *
     * @param deptCode 部門編碼
     * @return 部門
     */
    SysDepartment findByDeptCode(String deptCode);

    /**
     * 查詢頂級部門列表
     *
     * @return 頂級部門列表
     */
    List<SysDepartment> findByParentIdIsNullOrderByOrderNum();
}
