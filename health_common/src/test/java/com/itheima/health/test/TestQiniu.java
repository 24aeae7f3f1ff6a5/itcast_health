package com.itheima.health.test;

import com.google.gson.Gson;
import com.itheima.health.util.QiniuUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class TestQiniu {

    @Test
    public void upload(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "GGoQEJMIl6VDP3ae1PAzb91Lmtt_mU5RWTir7We7";
        String secretKey = "rPV_y6yjBJzIf6H_Y7L5afgqmurJkBd5RtIquL-A";
        String bucket = "itcast-health-85";
//        String accessKey = "liyKTcQC5TP1LrhgZH6Xem8zqMXbEtVgfAINP53v";
//        String secretKey = "f5zpuzKAPceEMG77-EK3XbwqgOBRDXDawG4UHRta";
//        String bucket = "itcast-health85";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Administrator\\Desktop\\20191203161251h.jpg";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    @Test
    public void upload2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "GGoQEJMIl6VDP3ae1PAzb91Lmtt_mU5RWTir7We7";
        String secretKey = "rPV_y6yjBJzIf6H_Y7L5afgqmurJkBd5RtIquL-A";
        String bucket = "itcast-health-85";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }

    }

    @Test
    public void delete(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释

        String accessKey = "GGoQEJMIl6VDP3ae1PAzb91Lmtt_mU5RWTir7We7";
        String secretKey = "rPV_y6yjBJzIf6H_Y7L5afgqmurJkBd5RtIquL-A";
        String bucket = "itcast-health-85";
        String key = "Fu3P9AgpOo_0q_4KnJdn9ZXVimfD";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }

    }

    @Test
    public void testUtil(){
        QiniuUtils.upload2Qiniu("C:\\Users\\Administrator\\Desktop\\20191202094221x.jpg","20191202094221x.jpg");
    }
}
