package com.test;

import org.code.core.utils.SpringContextUtil;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hello world!
 *
 */
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(rollbackFor = Exception.class)
public class Test {
	
	@org.junit.Test
	public void testSpringContextUtil() throws Exception {
		
		System.err.println(SpringContextUtil.getBean(SpringContextUtil.class));

	}

}
