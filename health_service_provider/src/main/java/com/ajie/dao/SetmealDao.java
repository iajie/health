package com.ajie.dao;

import com.ajie.entity.Setmeal;
import com.ajie.result.PageResult;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 18:25
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String,Integer> map);

    Page<Setmeal> findPage(String queryString);

    void deleteAssocication(Integer id);

    void deleteById(Integer id);

    void edit(Setmeal setmeal);

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    List<Setmeal> findAllSetmeal();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
