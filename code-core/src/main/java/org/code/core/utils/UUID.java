package org.code.core.utils;

import org.code.core.id.SimpleUuidGenerator;

/**
 * 
 * @DESC: UUID工具类
 * @author bbaiggey
 * @Date 2016年10月21日
 *
 */
public class UUID {
	static SimpleUuidGenerator idGenerator = new SimpleUuidGenerator();
	public static String getNextVal(){
		
		return idGenerator.getNextId();
		
	}

}
