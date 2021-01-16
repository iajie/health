package com.ajie.service.impl;

import com.ajie.dao.CheckGroupDao;
import com.ajie.entity.CheckGroup;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;
import com.ajie.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 10:36
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.findPage(queryString);
        long total = page.getTotal();
        List<CheckGroup> rows= page.getResult();
        return new PageResult(total,rows);
    }

    //根据检查组Id查询关联的检查项ID
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void deleteById(Integer checkGroupId) {
        //先清理检查组与检查项的关系表
        checkGroupDao.deleteAssocication(checkGroupId);
        //再删除检查组
        checkGroupDao.deleteById(checkGroupId);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1、修改检查组信息
        checkGroupDao.edit(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        //2、清理关联关系 再添加
        checkGroupDao.deleteAssocication(checkGroupId);

        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    //建立检查组和检查项的多对多关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
