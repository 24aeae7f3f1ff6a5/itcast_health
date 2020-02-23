package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.util.SMSUtils;
import com.itheima.health.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(value = "/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone) {
        String param = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, param);
        } catch (ClientException e) {
            e.printStackTrace();
            // 发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 打印验证码
        System.out.println(param);
        // 验证码发送成功，将对应的手机号和验证码存入redis并设置倒计时
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, param);
        // 响应发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
