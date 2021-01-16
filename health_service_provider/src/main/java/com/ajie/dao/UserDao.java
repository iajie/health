package com.ajie.dao;

import com.ajie.entity.User;

/*
 *  @Author 阿杰
 *  @create 2021年01月09日 13:49
 */
public interface UserDao {
    User findByUsername(String username);
}
