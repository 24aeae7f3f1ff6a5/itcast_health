package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    // 查询所有检查项
    List<CheckItem> findAll();
    // 新增
    void add(CheckItem checkItem);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);
}
