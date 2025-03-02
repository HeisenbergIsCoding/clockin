package com.clockin.auth.repository;

import com.clockin.auth.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系統角色資源庫
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    /**
     * 根據角色編碼查詢角色
     *
     * @param code 角色編碼
     * @return 角色信息
     */
    Optional<SysRole> findByCode(String code);

    /**
     * 根據角色名稱查詢角色
     *
     * @param name 角色名稱
     * @return 角色信息
     */
    Optional<SysRole> findByName(String name);

    /**
     * 檢查角色編碼是否存在
     *
     * @param code 角色編碼
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 檢查角色名稱是否存在
     *
     * @param name 角色名稱
     * @return 是否存在
     */
    boolean existsByName(String name);
}
