package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    // 查询所有检查项
    List<CheckItem> findAll();
}