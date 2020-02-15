package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;


    // 查询所有检查项
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    // 新增
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
