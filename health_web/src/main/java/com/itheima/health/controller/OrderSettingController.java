package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.util.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile excelFile){
        try {
            // 读取并导入excel文件
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size()>0) {
                List<OrderSetting> orderSettings = new ArrayList<>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettings.add(orderSetting);
                }
                orderSettingService.addList(orderSettings);
            }

            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping(value = "/findOrderSettingMapByMonth")
    public Result findOrderSettingMapByMonth(String date){
        try {
            List<Map> map = orderSettingService.findOrderSettingMapByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

}
