package org.code.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 
 * @ClassName: RedisDao 
 * @Description: TODO： 对redis数据库的查询和添加操作
 * @author ：xuwei
 * @date 2015年9月28日 上午10:22:03 
 *
 */
public class RedisDao {
	private JedisPool jedisPool = null;
	private Pipeline   pipeline = null;
	public  static int IN = 1000;
	public  static int GN = 1000;
	private Jedis      jedis = null;
	
	public RedisDao(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		jedisPool = new JedisPool(poolConfig, "192.168.1.184");
		poolConfig.setMaxIdle(1000);
		poolConfig.setMaxTotal(1000);
		poolConfig.setMaxWaitMillis(10000);
	}
	
		
	public void closeJedis(Jedis jedis){
		if(jedis != null){
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void  setString(String key,String value){
		jedis=jedisPool.getResource();
		jedis.set(key, value);
		closeJedis(jedis);
	}
	
	
	public void setString_batch(Map<String, String> keyValues){
		jedis=jedisPool.getResource();
		pipeline=jedis.pipelined();
		Set<Entry<String, String>> entrySet = keyValues.entrySet();
		int inr=0;
		for (Entry<String, String> entry : entrySet) {
			inr++;
			String key = entry.getKey();
			String value = entry.getValue();
			pipeline.set(key, value);
			if(inr%IN==0){
				pipeline.sync();
			}
		}
		pipeline.sync();
		
	}
	
	public void setHash(String key,Map<String, String> value){
		jedis=jedisPool.getResource();
		jedis.hmset(key, value);
		closeJedis(jedis);
	}
	
	public void setHash_bathch(Map<String, Map<String, String>> keyValues){
		jedis=jedisPool.getResource();
		pipeline=jedis.pipelined();
		Set<Entry<String, Map<String, String>>> keyValue_set= keyValues.entrySet();
		int inr=0;
		for (Entry<String, Map<String, String>> entry : keyValue_set) {
			inr++;
			String key = entry.getKey();
			Map<String, String> value = entry.getValue();
			pipeline.hmset(key, value);
			if(inr%IN==0){
				pipeline.sync();
			}
		}
		pipeline.sync();
		closeJedis(jedis);
	}
	
	
	public List<String> getString(String ...keys){
		jedis=jedisPool.getResource();
		List<String> values = jedis.mget(keys);
		closeJedis(jedis);
		return values;
	}
	
	public Map<String, String> getString_bathch(List<String> keys){
		jedis=jedisPool.getResource();
		pipeline=jedis.pipelined();
		int inr=0;
		Map<String, String > keyValus=new HashMap<String, String>();
		Response<String> response;
		for(String key:keys){
			inr++;
			response = pipeline.get(key);
			if(inr%GN==0){
				pipeline.sync();
			}
			String value = response.get();
			keyValus.put(key, value);
		}
		pipeline.sync();
		closeJedis(jedis);
		return keyValus;
	}
	
	public String  getHash(String key,String field){
		jedis=jedisPool.getResource();
		String value = jedis.hget(key, field);
		closeJedis(jedis);
		return value;
	}
	
	public List<String> getHash_fields(String key,String...fileds){
		jedis=jedisPool.getResource();
		List<String> values = jedis.hmget(key,fileds);
		closeJedis(jedis);
		return values;
	}
	
	public Map<String, List<String>> getHash_batch_filds(List<String> keys,String ...fileds){
		jedis=jedisPool.getResource();
		Map<String, List<String>> keyValue=new HashMap<String,List<String>>();
		for(String key:keys){
			Response<List<String>> hmget = pipeline.hmget(key, fileds);
			pipeline.sync();
			keyValue.put(key, hmget.get());
		}
		closeJedis(jedis);
		return keyValue;
	}
	
	public void deleteString(String...keys){
		jedis=jedisPool.getResource();
		jedis.del(keys);
		closeJedis(jedis);
	}
	
	public void deleteHash(List<String> keys,String...fileds){
		jedis=jedisPool.getResource();
		pipeline = jedis.pipelined();
		int inr=0;
		for(String key:keys){
			inr++;
			pipeline.hdel(key, fileds);
			if(inr%1000==0){
				pipeline.sync();
			}
		}
		pipeline.sync();
		closeJedis(jedis);
	}
	
	
	public void setList(String key,String ...value){
		jedis=jedisPool.getResource();
		jedis.lpush(key, value);
		closeJedis(jedis);
	}
	
	
	public List<String> getList(String key){
		jedis=jedisPool.getResource();
		List<String> value = jedis.lrange(key, 0, -1);
		closeJedis(jedis);
		return value;
	}
	
	
	public Map<String,String> getHash_all(String key){
		jedis=jedisPool.getResource();
		Map<String, String> values = jedis.hgetAll(key);
		closeJedis(jedis);
		return values;
	}
	
	
	/**
	 * 
	 * @param key
	 * @param value
	 * 添加字节类型的key,value
	 */
	public void setBytes(byte[] key,byte[] value){
		jedis=jedisPool.getResource();
		jedis.set(key, value);
		closeJedis(jedis);
	}
}
