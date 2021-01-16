package com.ajie.service.impl;

import com.ajie.constant.RedisConstant;
import com.ajie.dao.SetmealDao;
import com.ajie.entity.Setmeal;
import com.ajie.result.PageResult;
import com.ajie.result.QueryPageBean;
import com.ajie.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月04日 18:52
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String outPutPath;//属性文件中指定的生成目录文件

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
        this.saveRedisPic(setmeal.getImg());
        //生成静态页面(套餐列表、套餐详情)
        generateMobileStaticHtml();
    }

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        List<Setmeal> allSetmeal = setmealDao.findAllSetmeal();
        //用于生成套餐列表的静态页面
        this.generateMobileSetmealListHtml(allSetmeal);
        //用于生成套餐列表的详情页面
        this.generateMobileSetmealDetailHtml(allSetmeal);
    }

    //用于生成套餐列表所需的静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map<String,Object> map = new HashMap<>();
        //为模板准备数据
        map.put("setmealList",list);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情页面(可以是多个)
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+ setmeal.getId() +".html", map);
        }
    }

    //用于生成静态页面
    public void generateHtml(String templateName,String htmlPageName,Map<String,Object> map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new FileWriter(new File(outPutPath+ "/" + htmlPageName));
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将进入数据库的图片名称保存到redis
    private void saveRedisPic(String fileName) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.edit(setmeal);
        Integer setmealId = setmeal.getId();
        setmealDao.deleteAssocication(setmealId);
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }

    //操作套餐表和检查组表的关联关系t_setmeal_checkgroup
    private void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

    //删除套餐
    public void deleteById(Integer setmealId){
        setmealDao.deleteAssocication(setmealId);
        setmealDao.deleteById(setmealId);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer setmealId) {
        return setmealDao.findCheckGroupIdsBySetmealId(setmealId);
    }

    @Override
    public List<Setmeal> findAllSetmeal() {
        return setmealDao.findAllSetmeal();
    }

    //查询套餐详情页面
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //查询套餐预约占比
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
