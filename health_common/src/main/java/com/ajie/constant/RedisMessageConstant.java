package com.ajie.constant;

/*
 *  @Author 阿杰
 *  @create 2021年01月07日 10:48
 */
public class RedisMessageConstant {
    public static final String SENDTYPE_ORDER = "001"; //用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "002"; //用于缓存手机号快速登陆时发送的验证码
    public static final String SENDTYPE_GETPWD = "003"; //用于缓存找回密码时发送的验证码
}
