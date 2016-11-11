package org.code.redis.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	private static final int port = 6379;
	
	 static JedisPoolConfig poolConfig =null;
	 static Set<HostAndPort> nodes =null;
	 static JedisCluster jedisCluster = null;
	 
	 
	 static{
			poolConfig = new JedisPoolConfig();
			nodes  = new HashSet<HostAndPort>();
			nodes.add(new HostAndPort("suixingpay192", port));
			nodes.add(new HostAndPort("suixingpay193", port));
			nodes.add(new HostAndPort("suixingpay194", port));
			nodes.add(new HostAndPort("suixingpay195", port));
			nodes.add(new HostAndPort("suixingpay196", port));
			nodes.add(new HostAndPort("suixingpay197", port));
			jedisCluster=  new JedisCluster(nodes, poolConfig);
	 }

	 /**
	  * 
	  * @param inMno
	  * @param rowkey
	  */
	 public static void AddRowKey(String inMno,String rowkey) {
		 
		 jedisCluster.lpush(inMno,rowkey);
		 jedisCluster.close();
		
	}
	 public static List<String> GetRowKey(String inMno) {
		 
		 
		 List<String> list = jedisCluster.lrange(inMno, 0, -1);
		 
		 jedisCluster.close();
		 
		return list;
		 
	 }

	



}
