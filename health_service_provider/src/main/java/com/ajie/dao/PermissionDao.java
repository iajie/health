package com.ajie.dao;

import com.ajie.entity.Permission;

import java.util.Set;

/*
 *  @Author 阿杰
 *  @create 2021年01月09日 13:58
 */
public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
