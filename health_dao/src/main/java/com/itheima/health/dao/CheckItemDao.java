package com.itheima.health.dao;

import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    // 查询所有检查项
    List<CheckItem> findAll();

    // 新增
    void add(CheckItem checkItem);

    List<CheckItem> pageQuery(String queryString);

    long findCountByCheckItemId(Integer id);

    void deleteById(Integer id);

    List<CheckItem> findCheckItemListByCheckGroupId(Integer id);
}
