package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
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

    // 分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        List<CheckItem> list = checkItemDao.pageQuery(queryString);
        PageInfo<CheckItem> checkItemPageInfo = new PageInfo<>(list);

        return new PageResult(checkItemPageInfo.getTotal(), checkItemPageInfo.getList());
    }

    // 根据主键id删除
    @Override
    public void delete(Integer id) {
        // 查询是否有关联关系
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count>0) {
          throw  new RuntimeException("当前检查项被检查组引用，不能删除");
        }
        // 删除检查项
        checkItemDao.deleteById(id);

    }
}
