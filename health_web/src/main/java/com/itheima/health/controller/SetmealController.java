package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.util.QiniuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /*@RequestParam(value = "imgFile")的作用是
    形参名和浏览器提交的参数名不同时做指定用的，
    如果实参和形参一致可以不用这个注解*/
    @RequestMapping(value = "/upload")
    //public Result upload(@RequestParam(value = "imgFile") MultipartFile imgFile) {
    public Result upload( MultipartFile imgFile) {
        try {
            String originalFilename = imgFile.getOriginalFilename();
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);



        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }


    }

    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal,checkgroupIds);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);



        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }


    }
}
