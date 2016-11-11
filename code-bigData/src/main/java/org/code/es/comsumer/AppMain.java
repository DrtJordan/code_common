package org.code.es.comsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.code.core.constant.Constants;
import org.code.es.comsumer.service.ElasticsearchService;
import org.code.es.comsumer.vo.Person;
import org.code.es.comsumer.vo.TaskInfo;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppMain {

	private static final Logger logger = Logger.getLogger(AppMain.class);
	ClassPathXmlApplicationContext context =null ; 
	@Before
	public void before() {
		 context = new ClassPathXmlApplicationContext("classpath:es/es.xml");
	}

	@Test
	public void start() {
		ClassPathXmlApplicationContext context = null;
		try {
			context = new ClassPathXmlApplicationContext("classpath:es/es.xml");
		} catch (Exception e) {
			logger.error("An error occurred, applicationContext will close.", e);
			if (context != null) {
				context.close();
			}
			context = null;
			logger.error(Constants.CLOSED_MSG);
		}
	}

	/**
	 * 插入
	* @author 高国藩
	* @date 2015年6月16日 上午10:14:21
	 */
	@Test
	public  void insertNo() {
		
		ElasticsearchService service = context.getBean(ElasticsearchService.class);
//		List<Person> taskInfoList = new ArrayList<Person>();
//		for (int i = 0; i < 20; i++) {
//			taskInfoList.add(new Person((i + 5), i + 5, "大雨将至新华社课工场北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"
//					));
//		}
		List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
		for (int i = 0; i < 20; i++) {
			taskInfoList.add(new TaskInfo(String.valueOf((i + 5)), i + 5, "大雨将至新华社课工场北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"
					+ i, "taskArea你好", "taskTags", i + 5, "1996-02-03", "bbaiggey"));
		}
		System.out.println(service.insertOrUpdateTaskInfo(taskInfoList));
	}
	


	@Test
	public void serchNo() {
		org.code.es.service.Web_ElasticsearchService service = (org.code.es.service.Web_ElasticsearchService) context
				.getBean("es");
		List<Map<String, Object>> al = service.queryForObject(Constants.ESProp.TYPE_TASK_INFO,
				new String[] { "taskContent", "taskArea" }, "你好", "taskArea", SortOrder.DESC,
				0, 20);

		for (int i = 0; i < al.size(); i++) {
			System.out.println(al.get(i));
		}
		
	}
	@Test
	public void serchPNo() {
		org.code.es.service.Web_ElasticsearchService service = (org.code.es.service.Web_ElasticsearchService) context
				.getBean("es");
		List<Map<String, Object>> al = service.queryForObject(Constants.ESProp.TYPE_PERSON_INFO,
				new String[] { "name" }, "北京", "age", SortOrder.DESC,
				0, 20);
		
		for (int i = 0; i < al.size(); i++) {
			System.out.println(al.get(i));
		}
		
	}
	
	
	@Test
	public void serchFilter() {
		
		org.code.es.service.Web_ElasticsearchService service = (org.code.es.service.Web_ElasticsearchService) context
				.getBean("es");
		List<Map<String, Object>> al = service.queryForObjectForElasticSerch("task_info", "taskContent", "北京",0,20);

		for (int i = 0; i < al.size(); i++) {
			System.out.println(al.get(i));
		}
		
	}
}
