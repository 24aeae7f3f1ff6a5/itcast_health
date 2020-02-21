package com.itheima.health.dao;

import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupDao {

    List<CheckGroup> findAll();

    List<CheckGroup> findCheckGroupListBySetmealId(Integer id);
}
