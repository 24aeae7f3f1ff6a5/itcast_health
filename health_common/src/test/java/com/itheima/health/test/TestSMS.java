package com.itheima.health.test;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.util.SMSUtils;

public class TestSMS {

    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("SMS_175582541","15019235866","6379");
    }
}
