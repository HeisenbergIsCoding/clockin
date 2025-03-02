package com.clockin.auth.repository;

import com.clockin.auth.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系統用戶數據訪問層
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根據用戶名查詢用戶
     *
     * @param username 用戶名
     * @return 用戶
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 根據用戶名檢查用戶是否存在
     *
     * @param username 用戶名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根據電子郵件檢查用戶是否存在
     *
     * @param email 電子郵件
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根據電子郵件查詢用戶
     *
     * @param email 電子郵件
     * @return 用戶
     */
    Optional<SysUser> findByEmail(String email);

    /**
     * 根據部門ID查詢用戶數量
     *
     * @param departmentId 部門ID
     * @return 用戶數量
     */
    long countByDepartmentId(Long departmentId);

    /**
     * 根據崗位ID查詢用戶數量
     *
     * @param positionId 崗位ID
     * @return 用戶數量
     */
    long countByPositionId(Long positionId);

    /**
     * 根據部門ID查詢用戶列表
     *
     * @param departmentId 部門ID
     * @return 用戶列表
     */
    List<SysUser> findByDepartmentId(Long departmentId);

    /**
     * 根據崗位ID查詢用戶列表
     *
     * @param positionId 崗位ID
     * @return 用戶列表
     */
    List<SysUser> findByPositionId(Long positionId);

    /**
     * 根據角色ID查詢用戶列表
     *
     * @param roleId 角色ID
     * @return 用戶列表
     */
    @Query("SELECT u FROM SysUser u JOIN u.roles r WHERE r.id = :roleId")
    List<SysUser> findByRoleId(@Param("roleId") Long roleId);

    /**
     * 根據部門ID列表查詢用戶列表
     *
     * @param departmentIds 部門ID列表
     * @return 用戶列表
     */
    List<SysUser> findByDepartmentIdIn(List<Long> departmentIds);

    /**
     * 分頁查詢用戶列表
     *
     * @param username 用戶名（模糊查詢）
     * @param name 姓名（模糊查詢）
     * @param status 狀態
     * @param departmentId 部門ID
     * @param pageable 分頁參數
     * @return 用戶列表
     */
    @Query("SELECT u FROM SysUser u WHERE " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:name IS NULL OR u.name LIKE %:name%) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:departmentId IS NULL OR u.departmentId = :departmentId)")
    Page<SysUser> findUsers(
            @Param("username") String username,
            @Param("name") String name,
            @Param("status") Integer status,
            @Param("departmentId") Long departmentId,
            Pageable pageable);
}
