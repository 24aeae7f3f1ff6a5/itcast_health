package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);

    Setmeal findById(Integer id);

    Integer[] findCheckGroupIdsBySetmealId(Integer id);

    void edit(Setmeal setmeal);

    Integer deleteSetmealAndCheckGroupBySetmealId(Integer id);

    void delete(Integer id);

    List<Setmeal> getSetmeal();


}
