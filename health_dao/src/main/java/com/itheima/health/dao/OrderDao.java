package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;

public interface OrderDao {
    List<Order> findByCondition(Order order);

    void add(Order order);
}
