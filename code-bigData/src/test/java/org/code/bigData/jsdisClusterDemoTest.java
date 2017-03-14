package org.code.bigData;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.JedisCluster;
  
public class jsdisClusterDemoTest {  
    private static ApplicationContext apx;  
    static{  
        apx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");  
    }  
      
      
    @Test  
    public void test1(){  
        JedisCluster jedisCluster = (JedisCluster) apx.getBean("jedisCluster");  
        String key1="myname1";  
        String key2="myname2";  
        String key3="myname3";  
        System.out.println(jedisCluster.get(key1));  
        System.out.println(jedisCluster.get(key2));  
        System.out.println(jedisCluster.get(key3));  
          
    }  
      
    @Test  
    public void test2(){  
        JedisCluster jedisCluster = (JedisCluster) apx.getBean("jedisCluster");  
        String key1="mystring1"; String val1 = "myval1";  
        String key2="mystring2"; String val2 = "myval2";  
        String key3="mystring3"; String val3 = "myval3";  
        System.out.println(jedisCluster.set(key1,val1));  
        System.out.println(jedisCluster.set(key2,val2));  
        System.out.println(jedisCluster.set(key3,val3));  
        System.out.println("----------------------");  
        System.out.println(jedisCluster.get(key1));  
        System.out.println(jedisCluster.get(key2));  
        System.out.println(jedisCluster.get(key3));  
    }  
}  