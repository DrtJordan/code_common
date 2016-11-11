package org.code.bigData;

import javax.annotation.Resource;

import org.code.kafka.service.KafkaProducerService;
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
public class kafkaTest {
	
	private static final Logger logger = LoggerFactory.getLogger(kafkaTest.class);
	@Resource
	private KafkaProducerService kafkaService;
	@org.junit.Test
	public void testSpringWithKafka() throws Exception {
		
		
			logger.info("-------KafkaController--------start-----");
			System.err.println("---------KafkaController--------start---------");
			kafkaService.sendDefaultInfo("kafka sendMessage test with spring!");
			System.in.read();
		

	}
	

}
