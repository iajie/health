package com.ajie.jobs;

import com.ajie.constant.RedisConstant;
import com.ajie.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/*  自定义Job，定时清理垃圾图片
 *  @Author 阿杰
 *  @create 2021年01月05日 11:41
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void ClearImg() {
        //根据redis中两个集合的set进行差值计算，获得垃圾图片名称
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (set != null) {
            for (String s : set) {
               //删除七牛云服务器上的文件
                QiniuUtils.deleteFileFormQiniu(s);
                //删除redis集合中的集合名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
                System.out.println("自定义任务删除垃圾图片"+s);
            }
        }
    }
}
