package org.code.es.util;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.code.core.utils.FastJsonConvert;
import org.code.core.utils.UUID;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestEs {
	
	TransportClient client;
	String index = "bg";
	String type = "emp";
	Student student = new Student(UUID.getNextVal(), "update大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
	@Before
	public void bef() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff",true).build();
		this.client = new TransportClient(settings);
		TransportAddress transportAddress = new InetSocketTransportAddress("192.168.127.6", 9300);
		client.addTransportAddress(transportAddress);
		
	}
	
	
	//构建客户端
	public  void  bg_bef() {
		 // 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
        // 这样做的好处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器。
		Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.sniff",true).build();
		this.client = new TransportClient(settings);
		TransportAddress transportAddress = new InetSocketTransportAddress("192.168.127.6", 9300);
		client.addTransportAddresses(transportAddress );
	}
	
	
	@Test
	public void test1() throws Exception {
		
		
		GetResponse response = client.prepareGet(index, type, "1").execute().actionGet();
		
		System.out.println(response.getSourceAsString());
		
		ImmutableList<DiscoveryNode> connectedNodes = client.connectedNodes();
		for (DiscoveryNode discoveryNode : connectedNodes) {
			System.out.println(discoveryNode.getHostAddress());
			
		}
		
	}
	//获取当前集群的所有节点
	@Test
	public void bg_test1(){
		
		
		GetResponse response = client.prepareGet().setIndex(index).setType(type).setId("1").execute().actionGet();
//		GetResponse resp = client.prepareGet("bg", "emp", "1").execute().actionGet();
		System.out.println("id为1的数据是--->"+response.getSourceAsString());
		
		ImmutableList<DiscoveryNode> connectedNodes = client.connectedNodes();
		for (DiscoveryNode discoveryNode : connectedNodes) {
			System.out.println("节点--->"+discoveryNode.getAddress());
		}
		;
	}
	

	
	/**
	 * index -- jsons
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		String jsonStr = "{\"name\":\"新闻\",\"context\":\"新时代有新任务，新形势有新要求。11月２日，党的十八届六中全会审议通过的《关于新形势下党内政治生活的若干准则》《中国共产党党内监督条例》正式发布。综观《准则》和《条例》，有五个方面鲜明特点。把握这些特点，有助于深入领会六中全会精神，有助于把《准则》和《条例》更好地贯彻执行到位。 \"}";
		IndexResponse response = client.prepareIndex(index, type, "7")
				.setSource(jsonStr)
				.execute()
				.actionGet();
		System.out.println(response.getVersion());
	}
	/**
	 * 索引json
	 */
	@Test
	public void bg_index_json(){
		
		for (int i = 0; i < 5; i++) {
			Student student = new Student(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
			String json = FastJsonConvert.convertObjectToJSON(student);
			IndexResponse resp = client.prepareIndex(index, type, UUID.getNextVal())
			.setSource(json)
			.execute()
			.actionGet();
			System.out.println("索引json字符串---->" +resp.getVersion());
		}
	}
	
	
	/**
	 * index -- map
	 * @throws Exception
	 */
	@Test
	public void test4() throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("name", "杨利伟");
		hashMap.put("age", 22);
		hashMap.put("addr", "北京");
		IndexResponse response = client.prepareIndex(index, type,"6")
				.setSource(hashMap)
				.execute()
				.actionGet();
		System.out.println(response.getVersion());
	}
	
	/**
	 * 索引map
	 */
	@Test
	public void bg_index_Map(){
		
		
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal());
			hashMap.put(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal());
			hashMap.put(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal());
		/*Student student1 = new Student(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		Student student2 = new Student(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		Student student3 = new Student(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();	
		hashMap.put("1",student1);
		hashMap.put("2",student2);
		hashMap.put("3",student3);*///会自动调用对象的toString方法
		
			IndexResponse resp = client.prepareIndex(index, type, UUID.getNextVal())
					.setSource(hashMap)
					.execute()
					.actionGet();
			System.out.println("索引json字符串---->" +resp.getVersion());
		}
	
	
	
	
	
	/**
	 * index -- bean
	 * @throws Exception
	 */
	@Test
	public void test5() throws Exception {
		
		Student stu = new Student();
		stu.setId(UUID.getNextVal());
		stu.setName("小米");
		stu.setAge(3);
		IndexResponse response = client.prepareIndex(index, type, "6")
				.setSource(new ObjectMapper().writeValueAsString(stu))
				.execute()
				.get();
		System.out.println(response.getVersion());
	}
	
	/**
	 * 索引bean对象  
	 * tips:要将bean进行一次映射
	 * @throws JsonProcessingException 
	 * @throws ElasticsearchException 
	 */
	@Test
	public void bg_index_bean() throws ElasticsearchException, JsonProcessingException{
		
		Student student1 = new Student(UUID.getNextVal(), "大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		
		IndexResponse resp = client.prepareIndex(index, type, UUID.getNextVal())
				.setSource(new ObjectMapper().writeValueAsString(student1))
				.execute()
				.actionGet();
		System.out.println("索引json字符串---->" +resp.getVersion());
	}
	
	
	
	
	/**
	 * index -- es helper
	 * @throws Exception
	 */
	@Test
	public void test6() throws Exception {
		IndexResponse response = client.prepareIndex(index, type, "8")
				.setSource(XContentFactory.jsonBuilder().startObject().field("name", "名字").field("age", 24).endObject())
				.execute()
				.actionGet();
		System.out.println(response.getVersion());
	}
	
	/**
	 * 通过ES提供的工厂类构建对象
	 * @throws ElasticsearchException
	 * @throws IOException
	 */
	@Test
	public void bg_index_esHelper() throws ElasticsearchException, IOException{
		
		client.prepareIndex(index, type,UUID.getNextVal())
		.setSource(XContentFactory.jsonBuilder()
				.startObject()//通过函数构造对象
				.field("id", UUID.getNextVal()).field("name","小米").field("age", 10)
				.endObject())
		.execute()
		.actionGet();
		
	}
	
	
	/**
	 * index
	 * 
	 * @throws Exception
	 */
	@Test
	public void test7() throws Exception {
		IndexRequest request = new IndexRequest();
		request.index(index);
		request.type(type);
		request.id("9");
		request.source("{\"name\":\"crxy8\",\"age\":10}");
		IndexResponse response = client.index(request).get();
		System.out.println(response.getVersion());
	}
	/**
	 * IndexRequest
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void bg_index_request() throws InterruptedException, ExecutionException{
		
		Student student = new Student(UUID.getNextVal(), "update大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		
		IndexRequest reqs = new IndexRequest();
		reqs.index(index);
		reqs.type(type);
		reqs.id("1");
		reqs.source(FastJsonConvert.convertObjectToJSON(student));
		IndexResponse indexResponse = client.index(reqs).get();
		
		System.out.println(indexResponse.getVersion());
		
	}
	
	
	
	/**
	 * get 通过id查询
	 * 
	 * @throws Exception
	 */
	@Test
	public void test8() throws Exception {
		GetResponse response = client.prepareGet(index, type, "1").execute().actionGet();
		System.out.println(response.getSourceAsString());
	}
	
	
	
	/**
	 * update
	 * @throws Exception
	 */
	@Test
	public void test9() throws Exception {
		UpdateResponse response = client.prepareUpdate(index, type, "1")
		.setDoc(XContentFactory.jsonBuilder().startObject().field("name", "crxy1").endObject())
		.execute()
		.actionGet();
		
		System.out.println(response.getVersion());
		
	}
	/**
	 * 更新
	 * @throws ElasticsearchException
	 * @throws IOException
	 */
	@Test
	public void bg_updateIndex() throws ElasticsearchException, IOException{
		
		UpdateResponse actionGet = client.prepareUpdate(index, type, "1")
		.setDoc(XContentFactory.jsonBuilder().startObject().field("name","你是谁？").endObject())
		.execute()
		.actionGet();
		System.out.println(actionGet.getVersion());
		
	}
	
	/**
	 * 更新或者插入
	 * @throws Exception
	 */
	@Test
	public void test10() throws Exception {
		UpdateRequest request = new UpdateRequest();
		request.index(index);
		request.type(type);
		request.id("11");
		request.doc("{\"age\":21}");
		request.upsert("{\"name\":\"crxy10\",\"age\":10}");
		
		UpdateResponse response = client.update(request ).get();
		System.out.println(response.getVersion());
		
	}
	
	/**
	 * 插入或更新   如果存在执行更新 对应的doc中的数据  如果不存在就插入 upsert()中的数据
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Test
	public void bg_index_upsert() throws InterruptedException, ExecutionException{
		Student student = new Student(UUID.getNextVal(), "update大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		UpdateRequest upsert = new UpdateRequest(index, type, "2")
		.doc(FastJsonConvert.convertObjectToJSON(student))
		.doc("age", 30)
		.upsert(FastJsonConvert.convertObjectToJSON(student));
		UpdateResponse reps = client.update(upsert).get();
		System.out.println(reps.getVersion());
		
	}
	
	
	/**
	 * delete
	 * @throws Exception
	 */
	@Test
	public void test11() throws Exception {
		DeleteResponse response = client.prepareDelete(index, type, "2").execute().actionGet();
		System.out.println(response.getVersion());
	}
	/**
	 * 根据id【主键】删除数据
	 */
	@Test
	public void bg_index_del(){
		DeleteResponse actionGet = client.prepareDelete(index, type, "1").execute().actionGet();
		System.out.println(actionGet.getVersion());
	}
	
	/**
	 * count 
	 * select count(1) from table
	 * @throws Exception
	 */
	@Test
	public void test12() throws Exception {
		CountResponse response = client.prepareCount(index).execute().actionGet();
		System.out.println(response.getCount());
	}
	
	/**
	 * 统计总条数
	 */
	@Test
	public void bg_index_Count(){
		CountResponse actionGet = client.prepareCount(index).execute().actionGet();
		System.out.println(actionGet.getCount());
	}
	
	
	/**
	 * bulk 批量执行
	 * @throws Exception
	 */
	@Test
	public void test13() throws Exception {
		Student student = new Student(UUID.getNextVal(), "update大雨将至新华社课工厂北京11月2日电　题：牵住改革“牛鼻子”　落实改革硬任务——学习贯彻习近平总书记在中央深改组第二十九次会议重要讲话"+UUID.getNextVal(), 10);
		BulkRequest bulkRequest = new BulkRequest();
		
		IndexRequest indexRequest = new IndexRequest(index,type,"6");
		indexRequest.source("{\"name\":\"crxy6\",\"age\":16}");
		
		DeleteRequest deleteRequest = new DeleteRequest(index, type, "22");
		
		UpdateRequest updateRequest = new UpdateRequest(index, type, "23");
		updateRequest
		.doc("{\"name\":\"crxy23\",\"age\":23}")
		//.upsert(FastJsonConvert.convertObjectToJSON(student))
		;
		//将预要执行的操作进行封装到bulkRequest中
		bulkRequest.add(indexRequest);
		bulkRequest.add(deleteRequest);
		bulkRequest.add(updateRequest);
		//一次提交批量执行
		BulkResponse resonse = client.bulk(bulkRequest).get();
		
		if(resonse.hasFailures()){
			System.out.println("部分执行失败");
			BulkItemResponse[] items = resonse.getItems();
			for (BulkItemResponse bulkItemResponse : items) {
				System.out.println(bulkItemResponse.getFailureMessage());
			}
		}else{
			System.out.println("全部执行成功！");
		}
	}
	
	
	/**
	 * 批量执行
	 */
	@Test
	public void bg_index_bulk(){
		
		BulkRequest bulkRequest = new BulkRequest();
		IndexRequest indexReq = new IndexRequest(index, type, "11").source(FastJsonConvert.convertObjectToJSON(student));
		DeleteRequest deleteReq = new DeleteRequest(index, type, "2");
		UpdateRequest doc = new UpdateRequest(index, type, "2")
		.doc("name", "ceshi")
		.upsert(FastJsonConvert.convertObjectToJSON(student));
		
		bulkRequest.add(indexReq);
		bulkRequest.add(deleteReq);
		bulkRequest.add(doc);
		BulkResponse actionGet = client.bulk(bulkRequest).actionGet();
		if (actionGet.hasFailures()) {
			System.out.println("部分执行失败");
			BulkItemResponse[] items = actionGet.getItems();
			for (BulkItemResponse bulkItemResponse : items) {
				if (bulkItemResponse!=null) {
					System.out.println(bulkItemResponse);
				}
			}
			
		}else {
			System.out.println("-----success over !!!");
		}
		
		
	}
	
	
	
	
	/**
	 * 查询方式
	 * @throws Exception
	 */
	@Test
	public void test14() throws Exception {
		SearchResponse response = client.prepareSearch(index)
		.setTypes(type)
		.setSearchType(SearchType.QUERY_THEN_FETCH)//这个是最精确的  精确度会影响查询效率
		.setQuery(QueryBuilders.queryString("白鸽"))
		.setFrom(1)//分页
		.get();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	
	/**
	 * from size
	 * @throws Exception
	 */
	@Test
	public void test15() throws Exception {
		SearchResponse response = client.prepareSearch(index)
		.setTypes(type)
		.setSearchType(SearchType.QUERY_THEN_FETCH)
		.setFrom(0)
		.setSize(3)
		.get();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	
	/**
	 * sort
	 * @throws Exception
	 */
	@Test
	public void test16() throws Exception {
		SearchResponse response = client.prepareSearch(index)
		.setTypes(type)
		.setSearchType(SearchType.QUERY_THEN_FETCH)
		.addSort("age", SortOrder.DESC)
		.get();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	
	
	
	
	/**
	 * filter
	 * @throws Exception
	 */
	@Test
	public void test17() throws Exception {
		SearchResponse response = client.prepareSearch(index)
		.setTypes(type)
		.setSearchType(SearchType.QUERY_THEN_FETCH)
		.setPostFilter(FilterBuilders.rangeFilter("age").from(0).to(25))
		.get();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		System.out.println("总数："+totalHits);
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.out.println(searchHit.getSourceAsString());
		}
	}
	/**
	 * filter的使用
	 */
	@Test
	public void bg_index_Filter(){
		SearchResponse res = client.prepareSearch(index)
		.setTypes(type)
		.setSearchType(SearchType.QUERY_THEN_FETCH)
		.setPostFilter(FilterBuilders.rangeFilter("age").from(10).to(30))
		.get();
		SearchHits hits = res.getHits();
		long total = hits.getTotalHits();
		System.out.println("命中总条数--->"+total);
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			System.out.println(searchHit.getSourceAsString());
			
		}
		
		
	}
	
	
	/**
	 * 高亮
	 * @throws Exception
	 */
	@Test
	public void test18() throws Exception {
		SearchResponse response = client.prepareSearch(index)
				.setTypes(type)
				.setQuery(QueryBuilders.matchQuery("name", "伟"))
				//设置高亮字段
				.addHighlightedField("name")
				//设置高亮前缀后缀
				.setHighlighterPreTags("<font color='red'>")
				.setHighlighterPostTags("</font>")
				.get();
				
				SearchHits hits = response.getHits();
				long totalHits = hits.getTotalHits();
				System.out.println("总数："+totalHits);
				SearchHit[] hits2 = hits.getHits();
				for (SearchHit searchHit : hits2) {
					Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
					HighlightField highlightField = highlightFields.get("name");
					Text[] fragments = highlightField.getFragments();
					for (Text text : fragments) {
						System.out.println(text);
					}
					System.out.println(searchHit.getSourceAsString());
				}
	}
	

	
	/**
	 * 分组统计
	 * @throws Exception
	 */
	@Test
	public void test19() throws Exception {
		//TODO-- size默认条数的问题
		SearchResponse response = client.prepareSearch(index).setTypes(type)
		.addAggregation(AggregationBuilders.terms("terms_1").field("name"))
		.execute().actionGet();
		
		Terms terms = response.getAggregations().get("terms_1");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getKey()+"---"+bucket.getDocCount());
		}
	}
	
	/**
	 * 简单分组 分桶
	 * @throws Exception
	 */
	@Test
	public void bg_index_agg() throws Exception {
		SearchResponse actionGet = client.prepareSearch(index).setTypes(type)
		.addAggregation(AggregationBuilders.terms("terms").field("age"))
		.execute()
		.actionGet();
		Terms terms = actionGet.getAggregations().get("terms");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			
			System.out.println(bucket.getKey()+"\t"+bucket.getDocCount());
			
		}
		
	}
	
	
	
	/**
	 * 分组聚合
	 * @throws Exception
	 */
	@Test
	public void test20() throws Exception {
		SearchResponse response = client.prepareSearch(index).setTypes(type)
		.addAggregation(AggregationBuilders.terms("terms_1").field("name")
		.subAggregation(AggregationBuilders.sum("sum_1").field("age")))
		.execute().actionGet();
		
		Terms terms = response.getAggregations().get("terms_1");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Sum sum = bucket.getAggregations().get("sum_1");
			System.out.println(bucket.getKey()+"---"+sum.getValue());
		}
	}
	
	/**
	 * 组内求和		select name terms,sum(age) sum_1 from tab group by name
	 * @throws Exception
	 */
	@Test
	public void bg_index_agg2() throws Exception {
		SearchResponse actionGet = client.prepareSearch(index).setTypes(type)
				.addAggregation(AggregationBuilders.terms("terms").field("name"))
				.addAggregation(AggregationBuilders.sum("sum_1").field("age"))
				.execute()
				.actionGet();
		Terms terms = actionGet.getAggregations().get("terms");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			Sum sum = bucket.getAggregations().get("sum_1");
			System.out.println(bucket.getKey()+"\t"+sum.getValue());
			
		}
		
	}
	
	
	
	/**
	 * 简单查询
	 */
	@Test
	public void bg_search(){
		
			 SearchResponse resp = client.prepareSearch(index).setTypes(type)
					.setQuery(QueryBuilders.matchQuery("name", "课工厂"))
					//.setQuery(QueryBuilders.prefixQuery("name", "大雨"))
					//.setQuery(QueryBuilders.matchPhraseQuery("name", "课工场"))
					//.setQuery(QueryBuilders.matchPhrasePrefixQuery("name", "大"))
					.addSort("age", SortOrder.DESC)//按照age进行降序排序
					.addHighlightedField("name")
					.setExplain(true) // 设置是否按查询匹配度排序
					.setSearchType(SearchType.DEFAULT)//  默认是QUERY_THEN_FETCH
					.setHighlighterPreTags("<tag>")//设置高亮
					.setHighlighterPostTags("</tag>")
					.setFrom(0)//分页
					.setSize(3)
					.execute()
					.actionGet();
			SearchHits hits = resp.getHits();
//			System.err.println("命中总数--->   "+hits.getTotalHits());
//			Iterator<SearchHit> it = hits.iterator();
//			while (it.hasNext()) {
//				System.out.println(it.next().getSourceAsString());
//				
//			}
			System.err.println("命中总数--->   "+hits.getTotalHits());
			for (SearchHit searchHit : hits) {
				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
				HighlightField highlightField = highlightFields.get("name");
				Text[] fragments = highlightField.getFragments();
				for (Text text : fragments) {
					System.err.println(text);
				}
				System.out.println("obj-->"+searchHit.getSourceAsString());
			}
			
	}
	
	
	/**
	 * 批量插入
	 */
	@Test
	public void bg_bulk_index(){
		
		BulkRequestBuilder prepareBulk = client.prepareBulk();
			for (int i = 0; i < 5; i++) {
				Student student = new Student(UUID.getNextVal(), "学生"+UUID.getNextVal(), 10);
				String json = FastJsonConvert.convertObjectToJSON(student);
				
				IndexRequest request = client.prepareIndex(index, type, UUID.getNextVal()).setSource(json).request();
				
				prepareBulk.add(request);
			}
			//单条插入
//			IndexResponse resp = client.prepareIndex(index, type, UUID.getNextVal())
//					.setSource(json)
//					.execute()
//					.actionGet();
			 try {
				prepareBulk.execute().get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			System.out.println("索引json字符串---->" +resp.getVersion());
		}
		
		
	/**
	 * 删除所有
	 */
	@Test
    public void deleteByTerm(){  
        BulkRequestBuilder bulkRequest = client.prepareBulk();  
        SearchResponse response = client.prepareSearch(index).setTypes(type)  
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)  
                .setQuery(QueryBuilders.matchAllQuery())  
                .setFrom(0).setSize(20).setExplain(true).execute().actionGet();  
        SearchHits hits = response.getHits();
        System.out.println("命中总条数------->"+hits.getTotalHits());
		for(SearchHit hit : hits){  
            String id = hit.getId();  
            bulkRequest.add(client.prepareDelete(index, type, id).request());  
        }  
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();  
        if (bulkResponse.hasFailures()) {  
            for(BulkItemResponse item : bulkResponse.getItems()){  
                System.out.println(item.getFailureMessage());  
            }  
        }else {  
            System.out.println("delete ok");  
        }  
          
    } 
	
	
	
	
	
	
	
	
	
	
	
	
	

}
