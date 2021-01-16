package com.ajie.dao;

import com.ajie.entity.Role;

import java.util.Set;

/*
 *  @Author 阿杰
 *  @create 2021年01月09日 13:55
 */
public interface RoleDao {
    Set<Role> findByUserId(Integer userId);
}
