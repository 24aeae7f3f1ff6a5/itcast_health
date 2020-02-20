package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    // 查询所有检查项
    @RequestMapping(value = "/findAll")
    public Result findCheckItemList() {
        List<CheckItem> list = checkItemService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } else {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    ;

    // 新增
    @RequestMapping(value = "/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    ;

    //分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = checkItemService.pageQuery(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());


        return pageResult;
    }

    // 根据主键id删除
    @RequestMapping(value = "/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

}
