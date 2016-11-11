package org.code.bigData;
import org.code.redis.jedis.RedisClientTemplate1;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
  
public class jedisDemoTest {  
    private static ApplicationContext apx;  
    static{  
        apx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");  
    }  
      
    @Test  
    public void test1(){  
        String key1 = "mykey1"; String val1 = "myvalue1";  
        RedisClientTemplate1 template = (RedisClientTemplate1) apx.getBean("redisClientTemplate");  
        String result = template.set(key1, val1);  
        System.out.println("result="+result);  
    }  
      
    @Test  
    public void test2(){  
        String key1 = "mykey1";  
        RedisClientTemplate1 template = (RedisClientTemplate1) apx.getBean("redisClientTemplate");  
        String val1 = template.get(key1);  
        System.out.println(val1);  
    }  
}  