import com.itheima.health.constant.RedisConstant;
import com.itheima.health.util.QiniuUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@ContextConfiguration(locations = "classpath:spring-redis.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class TestDeletePic {


    @Autowired
    JedisPool jedisPool;

    @Test
    public void test(){
//        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
//        for (String img : sdiff) {
//            System.out.println("删除的图片名称是：" + img);
//            QiniuUtils.deleteFileFromQiniu(img);
//            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
//        }

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,"11");

    }
}
