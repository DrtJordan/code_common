package org.code.solr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

/**
 * solr工具类
 *
 */
@Component
public class Solr {
	
	static final Logger logger = LoggerFactory.getLogger(Solr.class);

	@Autowired
	private  SolrTemplate solrTemplate ;

	

	/**
	 * 建立索引
	 * 
	 * @throws Exception
	 */
	public  void addIndex(Object obj) {
		try {
			solrTemplate.saveBean(obj);
			solrTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据Id进行删除
	 */
	public  void DelById() {
		try {
			solrTemplate.deleteById("IW-02");
			solrTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过查询条件进行删除
	 */
	private  void DelByQuery() {
		try {
//			solrTemplate.deleteByQuery("id:F*");
			solrTemplate.deleteById("id:F*");
			solrTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
