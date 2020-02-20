package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Setmeal findById(Integer id);

    Integer[] findCheckGroupIdsBySetmealId(Integer id);

    void edit(Integer[] checkgroupIds, Setmeal setmeal);

    void delete(Integer id);

    List<Setmeal> getSetmeal();
}
