package org.code.bigData;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Hello world!
 *
 */
@ContextConfiguration(locations = { "classpath:servlet-context.xml" })
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional(rollbackFor = Exception.class)
public class elasticsearchTest {
	
	private static final Logger logger = LoggerFactory.getLogger(elasticsearchTest.class);

	

	@org.junit.Test
	public void testSpringWithES() throws Exception {
		
		
		
		
		
	}

}
