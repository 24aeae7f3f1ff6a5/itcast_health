package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    public void addList(List<OrderSetting> orderSettings) {
        // 批量导入
        if (orderSettings != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {

                long count = orderSettingDao.findOrderSettingCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    orderSettingDao.updateNumberByOrderSetting(orderSetting);
                } else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> findOrderSettingMapByMonth(String date) {
        String beginDate = date + "-1";
        String endDate = date + "-31";
        Map paramsMap = new HashMap<>();
        paramsMap.put("beginDate", beginDate);
        paramsMap.put("endDate", endDate);
        List<OrderSetting> list = orderSettingDao.findOrderSettingMapByMonth(paramsMap);

        List<Map> mapList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", orderSetting.getOrderDate().getDate());
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                mapList.add(map);
            }
        }


        return mapList;
    }
}
