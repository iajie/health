package com.ajie.service;

import com.ajie.entity.Setmeal;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;

import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 18:25
 */
public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer setmealId);

    List<Setmeal> findAllSetmeal();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
