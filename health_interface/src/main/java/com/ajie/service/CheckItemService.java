package com.ajie.service;

import com.ajie.entity.CheckItem;
import com.ajie.result.CheckItemDeleteException;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;

import java.util.List;

/*  体检项服务接口
 *  @Author 阿杰
 *  @create 2021年01月02日 17:35
 */
public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteById(Integer id) throws CheckItemDeleteException;

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();
}
