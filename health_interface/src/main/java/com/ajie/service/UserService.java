package com.ajie.service;

import com.ajie.entity.User;

/*
 *  @Author 阿杰
 *  @create 2021年01月09日 13:37
 */
public interface UserService {
    User findByUsername(String username);
}
