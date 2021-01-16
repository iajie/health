package com.ajie.dao;

import com.ajie.entity.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

/*
 *  @Author 阿杰
 *  @create 2021年01月02日 17:46
 */
public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

}
