package com.ajie.controller;

import com.ajie.constant.MessageConstant;
import com.ajie.constant.RedisMessageConstant;
import com.ajie.entity.Order;
import com.ajie.result.Result;
import com.ajie.service.OrderService;
import com.ajie.util.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/*  体检预约
 *  @Author 阿杰
 *  @create 2021年01月08日 13:18
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        //从Redis中取出保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //将用户提交的验证进行比对
        String validateCode = (String) map.get("validateCode");
        if (validateCode != null && validateCodeInRedis != null && validateCode.equals(validateCodeInRedis)) {
            //如果比对成功，则调用服务进行处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN); //设置预约类型(微信预约、电话预约)
            Result result = null;
            try {
                result = orderService.order(map);//通过Dubbo远程调用服务实现在线预约处理
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.isFlag()) {
                //预约成功，发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            //如果比对不成功，就返回给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
