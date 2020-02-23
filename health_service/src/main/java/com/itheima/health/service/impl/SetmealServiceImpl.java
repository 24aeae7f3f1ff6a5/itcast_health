package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.util.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    @Value("${out_put_path}")
    private String outputpath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        this.setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    private void generateMobileStaticHtml() {
        // 查询所有套餐
        List<Setmeal> setmealList = this.getSetmeal();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐详情静态页面（多个）
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        if (setmealList != null && setmealList.size()>0) {
            for (Setmeal setmeal : setmealList) {
                Map<String,Object> map = new HashMap<>();
                map.put("setmeal",findById(setmeal.getId()));
                this.generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
            }
        }
    }

    //生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String,Object> map = new HashMap<>();
        map.put("setmealList",setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    // 生成静态页面
    private void generateHtml(String templateName, String htmlPageName, Map<String, Object> dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        Writer writer =null;
        try {
            Template template = configuration.getTemplate(templateName);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputpath + "\\" + htmlPageName))));
            template.process(dataMap,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page = setmealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }


    // 根据ID查询套餐信息
//    @Override
//    public Setmeal findById(Integer id) {
//        Setmeal setmeal = setmealDao.findById(id);
//        List<CheckGroup> checkGroupList = checkGroupDao.findCheckGroupListBySetmealId(setmeal.getId());
//        if (checkGroupList != null && checkGroupList.size() > 0) {
//            for (CheckGroup checkGroup : checkGroupList) {
//                List<CheckItem> checkItemList = checkItemDao.findCheckItemListByCheckGroupId(checkGroup.getId());
//                checkGroup.setCheckItems(checkItemList);
//            }
//        }
//
//        setmeal.setCheckGroups(checkGroupList);
//        return setmeal;
//    }

    // 根据套餐id查询信息（resultMap方式）
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Integer[] findCheckGroupIdsBySetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    // 编辑套餐
    @Override
    public void edit(Integer[] checkgroupIds, Setmeal setmeal) {
        // 使用套餐id查询原img信息
        Setmeal setmeal_db = setmealDao.findById(setmeal.getId());
        String img = setmeal_db.getImg();
        if (setmeal.getImg() != null && !setmeal.getImg().equals(img)) {
            QiniuUtils.deleteFileFromQiniu(img);
        }

        // 保存编辑套餐信息
        setmealDao.edit(setmeal);
        // 删除中间表的数据
        setmealDao.deleteSetmealAndCheckGroupBySetmealId(setmeal.getId());
        // 新增中间表的数据
        this.setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();
    }

    // 删除套餐
    @Override
    public void delete(Integer id) {
        // 获取数据库原setmeal对象信息
        Setmeal setmeal_db = findById(id);

        // 清空中间表数据
        Integer count = setmealDao.deleteSetmealAndCheckGroupBySetmealId(id);
        if (count > 0) {
            throw new RuntimeException("当前套餐和检查组之间存在关联关系，不能删除");
        }
        // 删除套餐
        setmealDao.delete(id);

        // 删除七牛云的图片
        String img = setmeal_db.getImg();
        if (img != null && "".equals(img)) {
            QiniuUtils.deleteFileFromQiniu(img);
        }

        //新增套餐后需要重新生成静态页面
        generateMobileStaticHtml();

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
