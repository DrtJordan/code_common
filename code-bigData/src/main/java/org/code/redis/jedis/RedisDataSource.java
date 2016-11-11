package org.code.redis.jedis;

import redis.clients.jedis.ShardedJedis;

public interface RedisDataSource {
	public ShardedJedis getRedisClient() ;
	public void returnResource(ShardedJedis shardedJedis);
	public void returnResource(ShardedJedis shardedJedis, boolean broken);

}
