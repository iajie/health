package com.ajie.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	public static final String VALIDATE_CODE = "SMS_208965510";//发送短信验证码
	public static final String ORDER_NOTICE = "SMS_208985524";//体检预约成功通知
	/**
	 * 发送短信
	 * @param phoneNumbers
	 * @param param
	 * @throws ClientException
	 */
	public static void sendShortMessage(String templateCode,String phoneNumbers,String param) throws ClientException {
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数
		final String accessKeyId = "LTAI4Fy3GWhBHPxmVKmYuBp4";
		final String accessKeySecret = "IFzcmKjZCaNhe8xbljYvQy7WQFo8uY";
		//设置访问秘钥
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		//创建阿里云短信客户端
		IAcsClient client = new DefaultAcsClient(profile);
		//创建阿里云请求
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		//设置发送的电话号码
		request.putQueryParameter("PhoneNumbers", phoneNumbers);
		//设置使用的短信签名
		request.putQueryParameter("SignName", "阿杰通讯");
		//设置使用的短信模板
		request.putQueryParameter("TemplateCode", templateCode);
		//设置发送的code
		request.putQueryParameter("TemplateParam", "{\"code\":\""+param+"\"}");
		CommonResponse response = client.getCommonResponse(request);
	}
}
