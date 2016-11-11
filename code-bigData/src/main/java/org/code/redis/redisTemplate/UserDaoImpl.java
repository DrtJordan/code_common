package org.code.redis.redisTemplate;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
  
/**
 * 
 * @DESC: 使用Spring RedisTemplate操作redis
 * @author bbaiggey
 * @Date 2016年10月26日
 *
 */
@Repository  
public class UserDaoImpl extends RedisGeneratorDao<Serializable, Serializable> implements UserDao {  //key和value必须是序列化的
      
   
  
    @Override
    public void saveUser(final User user) {  
        redisTemplate.execute(new RedisCallback<Object>(){  
            @Override  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                String str= "user.uid."+user.getId();  
                byte[] key = redisTemplate.getStringSerializer().serialize(str);  
                //这个有返回值 只有key不存在的时候设置Set value for key, only if key does not exist.
                //所谓 SETNX,是「SET if Not eXists」的缩写,也就是只有不存在的时候才设置
                //connection.setNX(key,redisTemplate.getStringSerializer().serialize(user.getName())); 
                connection.set(key,redisTemplate.getStringSerializer().serialize(user.getName()));  
                return null;  
            }  
        });  
    }  
  
    @Override
    public User getUser(final long id) {  
        return redisTemplate.execute(new RedisCallback<User>(){  
            @Override  
            public User doInRedis(RedisConnection connection) throws DataAccessException {  
                byte[] key = redisTemplate.getStringSerializer().serialize("user.uid." + id);  
                if(connection.exists(key)) {  
                    byte[] value = connection.get(key);  
                    String name = redisTemplate.getStringSerializer().deserialize(value);  
                    User user = new User();  
                    user.setName(name);  
                    user.setId(id);  
                    return user;  
                }  
                return null;  
            }  
        });  
    }  
  
}