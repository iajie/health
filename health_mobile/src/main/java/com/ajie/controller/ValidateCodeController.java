package com.ajie.controller;

import com.ajie.constant.MessageConstant;
import com.ajie.constant.RedisMessageConstant;
import com.ajie.result.Result;
import com.ajie.util.SMSUtils;
import com.ajie.util.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/*  处理验证码的控制类
 *  @Author 阿杰
 *  @create 2021年01月08日 12:00
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    private Jedis getResource() {
        return jedisPool.getResource();
    }

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //发送验证码，redis保存5分钟
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        Jedis jedis = getResource();
        // 15523285124 + 001 预约  300 5分钟
        jedis.setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,validateCode);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //发送验证码，redis保存5分钟
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        Jedis jedis = getResource();
        // 15523285124 + 002 登录  300 5分钟
        jedis.setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        jedis.close();
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,validateCode);
    }
}
