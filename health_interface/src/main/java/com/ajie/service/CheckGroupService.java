package com.ajie.service;

import com.ajie.entity.CheckGroup;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;

import java.util.List;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 10:29
 */
public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void deleteById(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    List<CheckGroup> findAll();
}
