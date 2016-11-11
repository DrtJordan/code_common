package org.code.redis.redisTemplate;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * @DESC: 公共模板抽取
 * @author bbaiggey
 * @Date 2016年10月26日
 *
 * @param <K>
 * @param <V>
 *///key和value必须是序列化的
public abstract class RedisGeneratorDao<K extends Serializable, V extends Serializable> {

	@Autowired
	protected RedisTemplate<Serializable, Serializable> redisTemplate;

}
