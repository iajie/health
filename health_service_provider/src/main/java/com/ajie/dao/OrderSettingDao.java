package com.ajie.dao;

import com.ajie.entity.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
 *  @Author 阿杰
 *  @create 2021年01月05日 16:42
 */
public interface OrderSettingDao {
    //添加预约设置
    void add(OrderSetting orderSetting);

    //根据日期修改预约人数
    void editNumberByOrderDate(OrderSetting orderSetting);

    //根据日期查询预约设置的条数
    long findCountByOrderDate(Date orderDate);

    //根据月份(1-31号之间)查询 对应的预约设置
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    //根据指定日期(日) 查询预约设置
    OrderSetting findByOrderDate(String orderDate);

    //跟新预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
