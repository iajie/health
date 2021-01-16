package com.ajie.service.impl;

import com.ajie.dao.PermissionDao;
import com.ajie.dao.RoleDao;
import com.ajie.dao.UserDao;
import com.ajie.entity.Permission;
import com.ajie.entity.Role;
import com.ajie.entity.User;
import com.ajie.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/*
 *  @Author 阿杰
 *  @create 2021年01月09日 13:50
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //根据用户名查询用户信息(角色信息、权限信息)
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息，不包括角色信息
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        //根据用户ID查询角色信息
        Set<Role> roles = roleDao.findByUserId(userId);//用户关联角色
        //根据角色ID查询权限
        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions =  permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);//角色关联权限
        }
        user.setRoles(roles);
        return user;
    }
}
