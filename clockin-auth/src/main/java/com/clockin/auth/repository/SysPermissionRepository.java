package com.clockin.auth.repository;

import com.clockin.auth.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 系統權限資源庫
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    /**
     * 根據權限編碼查詢權限
     *
     * @param code 權限編碼
     * @return 權限信息
     */
    Optional<SysPermission> findByCode(String code);

    /**
     * 檢查權限編碼是否存在
     *
     * @param code 權限編碼
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 根據父權限ID查詢權限列表
     *
     * @param parentId 父權限ID
     * @return 權限列表
     */
    List<SysPermission> findByParentIdOrderBySort(Long parentId);

    /**
     * 根據權限類型查詢權限列表
     *
     * @param type 權限類型
     * @return 權限列表
     */
    List<SysPermission> findByType(Integer type);

    /**
     * 根據ID集合查詢權限列表
     *
     * @param ids ID集合
     * @return 權限列表
     */
    List<SysPermission> findByIdIn(Set<Long> ids);
}
