package com.ajie.util;

import java.util.Random;

/*  随机生成验证码的工具类
 *  @Author 阿杰
 *  @create 2021年01月07日 10:39
 */
public class ValidateCodeUtils {

    /**
     * 生成随机验证码
     * @param length 4 或者 6 位
     * @return
     */
    public static Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            code = new Random().nextInt(9999);
            if (code < 1000) {
                code = code + 1000; //生成4位随机数
            }
        } else if (length == 6) {
            code = new Random().nextInt(999999);
            if (code < 100000) {
                code = code + 100000;//生成6位随机数
            }
        } else {
            throw new RuntimeException("只能生成4位或者6位的验证码");
        }
        return code;
    }

    public static String generateValidateCodeString(int length) {
        Random random = new Random();
        String hash = Integer.toHexString(random.nextInt());
        return hash.substring(0,length);
    }
}
