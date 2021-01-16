package com.ajie.service;

import com.ajie.entity.OrderSetting;

import java.util.List;
import java.util.Map;

/*  预约设置服务接口
 *  @Author 阿杰
 *  @create 2021年01月05日 16:35
 */
public interface OrderSettingService {
    void add(List<OrderSetting> data);

    List<Map<String,Object>> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
