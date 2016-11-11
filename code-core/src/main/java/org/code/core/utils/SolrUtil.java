package org.code.core.utils;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * solr工具类
 *
 */
@Component
public class SolrUtil {
	static final Logger logger = LoggerFactory.getLogger(SolrUtil.class);


	private static final String SOLR_URL = "http://192.168.120.199:8983/solr/collection1";

	
	private static HttpSolrClient httpSolrClient ;

	static {
		try {
			httpSolrClient = new HttpSolrClient(SOLR_URL);
			httpSolrClient.setAllowCompression(true);
			httpSolrClient.setConnectionTimeout(10000);
			httpSolrClient.setDefaultMaxConnectionsPerHost(100);
			httpSolrClient.setMaxTotalConnections(100);
			
			 /* Article ac = new Article(); ac.setAuthor("ttt");
			  ac.setContent("ssss"); ac.setDescribe("ssss"); ac.setId("5555");
			  ac.setTitle("556666"); addIndex(ac);*/
			 

			/**
			 * 连接solrCloud
			 */
			 /*
			
			 * String zkHost = "192.168.1.170:2181,192.168.1.171:2181";
			 * 
			 * CloudSolrServer httpSolrServer = new CloudSolrServer(zkHost );
			 * 
			 * server.setDefaultCollection("collection1");
			 */

		} catch (Exception e) {
			logger.error("请检查tomcat服务器或端口是否开启!{}", e);
			e.printStackTrace();
		}

	}

	/**
	 * 建立索引
	 * 
	 * @throws Exception
	 */
	public  void addIndex(Object obj) {
		try {
			httpSolrClient.addBean(obj);
			httpSolrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Id进行删除
	 */
	public  void DelById() {
		try {
			httpSolrClient.deleteById("IW-02");
			httpSolrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过查询条件进行删除
	 */
	private  void DelByQuery() {
		try {
			httpSolrClient.deleteByQuery("id:F*");
			httpSolrClient.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
