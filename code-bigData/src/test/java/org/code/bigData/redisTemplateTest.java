package org.code.bigData;

import org.code.redis.redisTemplate.User;
import org.code.redis.redisTemplate.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class redisTemplateTest {
	@Autowired
	UserDao userDao;
	@org.junit.Test
	 public  void test() {
		
	        ApplicationContext ac =  new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
	        User user1 = new User();
	        user1.setId(1);
	        user1.setName("obama");
	        userDao.saveUser(user1);
	        User user2 = userDao.getUser(1);
	        System.out.println(user2.getName());
	    }
}

