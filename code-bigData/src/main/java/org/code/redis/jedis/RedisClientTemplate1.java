package org.code.redis.jedis;  
  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Repository;  
  
import redis.clients.jedis.ShardedJedis;  
  
@Repository("redisClientTemplate1")  
public class RedisClientTemplate1 {  
    @Autowired  
    private RedisDataSource redisDataSource;  
      
    public void disconnect(){  
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();  
        shardedJedis.disconnect();  
    }  
      
    public String set(String key,String val){  
        String result = null ;  
        ShardedJedis jedis = redisDataSource.getRedisClient();  
        if(jedis ==null)return null;  
        boolean broken = false;  
        try{  
            result = jedis.set(key, val);  
        }catch(Exception e){  
            e.printStackTrace();  
            broken = true ;  
        }finally{  
            redisDataSource.returnResource(jedis,broken);  
        }  
        return result;  
    }  
      
    public String get(String key){  
        String result = null;  
        ShardedJedis jedis = redisDataSource.getRedisClient();  
        if(jedis==null)return result;  
        boolean broken = false;  
        try{  
            result = jedis.get(key);  
        }catch(Exception e){  
            e.printStackTrace();  
            broken= true;  
        }finally{  
            redisDataSource.returnResource(jedis,broken);  
        }  
        return result;  
    }  
}  