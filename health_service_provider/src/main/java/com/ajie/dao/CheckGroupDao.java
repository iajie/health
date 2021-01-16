package com.ajie.dao;

import com.ajie.entity.CheckGroup;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 10:35
 */
public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String,Integer> map);

    Page<CheckGroup> findPage(String queryString);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssocication(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

}
