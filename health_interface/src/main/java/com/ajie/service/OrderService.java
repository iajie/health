package com.ajie.service;

import com.ajie.result.Result;

import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月08日 13:31
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception ;
}
