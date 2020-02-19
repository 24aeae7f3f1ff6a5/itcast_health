package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderSettingServiceImpl  implements OrderSettingService{

    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    public void addList(List<OrderSetting> orderSettings) {
        // 批量导入
        if (orderSettings != null && orderSettings.size()>0) {
            for (OrderSetting orderSetting : orderSettings) {

                long count = orderSettingDao.findOrderSettingCountByOrderDate(orderSetting.getOrderDate());
                if (count>0){
                    orderSettingDao.updateNumberByOrderSetting(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
}
