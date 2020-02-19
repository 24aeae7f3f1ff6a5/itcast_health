package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addList(List<OrderSetting> orderSettings);

    List<Map> findOrderSettingMapByMonth(String date);
}