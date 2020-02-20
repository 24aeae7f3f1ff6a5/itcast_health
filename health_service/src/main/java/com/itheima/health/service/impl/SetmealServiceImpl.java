package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        this.setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

    }


    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Integer[] findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public void edit(Integer[] checkgroupIds, Setmeal setmeal) {
        // 使用套餐id查询原img信息
        Setmeal setmeal_db = setmealDao.findById(setmeal.getId());
        String img = setmeal_db.getImg();
        if (setmeal.getImg()!=null && !setmeal.getImg().equals(img)){
            QiniuUtils.deleteFileFromQiniu(img);
        }

        // 保存编辑套餐信息
        setmealDao.edit(setmeal);
        // 删除中间表的数据
        setmealDao.deleteSetmealAndCheckGroupBySetmealId(setmeal.getId());
        // 新增中间表的数据
        this.setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
    }

    // 删除套餐
    @Override
    public void delete(Integer id) {
        // 获取数据库原setmeal对象信息
        Setmeal setmeal_db = findById(id);

        // 清空中间表数据
        Integer count = setmealDao.deleteSetmealAndCheckGroupBySetmealId(id);
        if (count>0){
           throw  new RuntimeException("当前套餐和检查组之间存在关联关系，不能删除");
        }
        // 删除套餐
        setmealDao.delete(id);

        // 删除七牛云的图片
        String img = setmeal_db.getImg();
        if (img !=null && "".equals(img)){
            QiniuUtils.deleteFileFromQiniu(img);
        }


    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", id);
                map.put("checkgroup_id", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
}
