package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.util.QiniuUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /*@RequestParam(value = "imgFile")的作用是
    形参名和浏览器提交的参数名不同时做指定用的，
    如果实参和形参一致可以不用这个注解*/
    @RequestMapping(value = "/upload")
    //public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile) {
    public Result upload(MultipartFile imgFile) {
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);


        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }


    }

    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean param) {
        PageResult pageResult = setmealService.pageQuery(param.getCurrentPage(), param.getPageSize(), param.getQueryString());
        return pageResult;
    }

    // 根据主键id查询套餐
    @RequestMapping(value = "/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    // 查套餐和检查项的关联关系
    @RequestMapping(value = "/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(Integer id) {
        try {
            Integer[] ids = setmealService.findCheckGroupIdsBySetmealId(id);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, ids);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    // 编辑套餐
    @RequestMapping(value = "/edit")
    public Result edit(Integer[] checkgroupIds, @RequestBody Setmeal setmeal) {
        try {
            setmealService.edit(checkgroupIds, setmeal);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }


    // 删除套餐
    @RequestMapping(value = "/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);

        } catch (RuntimeException r) {
            return new Result(false, r.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }


}
