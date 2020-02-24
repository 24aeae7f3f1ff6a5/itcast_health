package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){
        // 接收预约信息
        // 获取电话号码
        String telephone = (String) map.get("telephone");
        // 获取redis中的验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        // 获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        // 判断验证码是否为空或验证码不一致
        if (code == null || !code.equals(validateCode)) {
            return  new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        // 如果验证码符合条件
        // 调用service执行预约操作
        Result result = null;
        // 设置预约途径
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        try {
            result = orderService.order(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
