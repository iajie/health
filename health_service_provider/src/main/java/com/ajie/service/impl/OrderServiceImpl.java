package com.ajie.service.impl;

import com.ajie.constant.MessageConstant;
import com.ajie.dao.MemberDao;
import com.ajie.dao.OrderDao;
import com.ajie.dao.OrderSettingDao;
import com.ajie.entity.Member;
import com.ajie.entity.Order;
import com.ajie.entity.OrderSetting;
import com.ajie.result.Result;
import com.ajie.service.OrderService;
import com.ajie.util.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*  体检预约服务
 *  @Author 阿杰
 *  @create 2021年01月08日 15:03
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;
    //体检预约
    public Result order(Map map) throws Exception{
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约(同一个用户在同一天预约了同一个套餐)，如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //判断是否在重复预约
            Integer memberId = member.getId();//会员ID
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐编号
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            //根据会员、预约日期和套餐编号查询当天该会员是否预约了同一个套餐
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                //用户已经预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        } else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName(map.get("name").toString());
            member.setIdCard(map.get("idCard").toString());
            member.setSex(map.get("sex").toString());
            member.setRegTime(new Date());//注册日期
            //会员注册
            memberDao.add(member);
        }
        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType(map.get("orderType").toString());
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt(map.get("setmealId").toString()));
        orderDao.add(order);
        orderSetting.setReservations(orderSetting.getReservations() + 1); //已预约人数加1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约ID查询预约信息(体检人姓名、预约日期、套餐名称、预约类型)
    public Map findById(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date date = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(date));
        }
        return map;
    }
}