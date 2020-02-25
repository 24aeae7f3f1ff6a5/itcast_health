package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;


    @Override
    public Result order(Map map) throws Exception {
        // 套餐是否可以预约？
        String setmeal_Id = (String) map.get("setmealId");
        String date = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(date);
        // 根据预约时间查询预约对象
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        // 该时间段不可以预约
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            // 预约已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        // 用户是否注册？
        String telephone = (String) map.get("telephone");

        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            // 用户是否重复预约？(预约时间、套餐id、用户id)
            Integer member_id = member.getId();

            Order order = new Order(member_id, orderDate, null, null, Integer.parseInt(setmeal_Id));
            // 为什么是用list接收，为什么不是一个对象，同一会员同一时间是否只应该允许预约一个套餐
            List<Order> orders = orderDao.findByCondition(order);
            if (orders != null && orders.size() > 0) {
                // 重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }else {
            // 注册会员
            String idCard = (String) map.get("idCard");
            String sex = (String) map.get("sex");
            String name = (String) map.get("name");
            member = new Member();
            member.setIdCard(idCard);
            member.setSex(sex);
            member.setName(name);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        // 保存套餐预约信息
        Order order = new Order(member.getId(),orderDate,(String) map.get("orderType"),Order.ORDERSTATUS_NO, Integer.parseInt(setmeal_Id));
        orderDao.add(order);
        // 更新预约人数
        orderSettingDao.editReservationsByOrderDate(orderDate);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);

    }
}
