package org.code.redis.redisTemplate;

public interface UserDao {
	public void saveUser(User user);
	public User getUser(long id);

}
