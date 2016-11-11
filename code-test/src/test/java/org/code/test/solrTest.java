package org.code.test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
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
public class solrTest {
	
	private static final Logger logger = LoggerFactory.getLogger(solrTest.class);

	
	@Autowired
	private SolrTemplate solrTemplate;
	@org.junit.Test
	public void testSpringWithSlor() throws Exception {
		
		System.out.println(solrTemplate);
		
		
		
		
	}

}
