package org.code.core.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Description: 配置管理组件  主要是为了获取配置文件的一些配置信息
 * Created on:  2016年8月9日 下午1:37:26 
 * @author bbaiggey
 * @github https://github.com/bbaiggey
 */
public class ConfigurationManager {
	//访问外部访问进行修改配置文件中的加载到内存中的数据
	private static Properties prop = new Properties();
	static Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);
	
	static{
		try {
			InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("config.properties");
			prop.load(in);
		} catch (IOException e) {
			logger.error(ConfigurationManager.class.getName()+"加载配置文件出错....");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Description: 根据key拿到value
	 * @author bbaiggey
	 * @param  @param key
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String getProperty(String key){
		
		return prop.getProperty(key);
	}
	/**
	 * 
	 * Description: 获取int类型的值
	 * @author bbaiggey
	 * @param  @param key
	 * @param  @return
	 * @return Integer
	 * @throws
	 */
	public static Integer getInteger(String key){
		String value = getProperty(key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 
	 * Description: 获取布尔类型的值
	 * @author bbaiggey
	 * @param  @param key
	 * @param  @return
	 * @return Boolean
	 * @throws
	 */
	public static Boolean getBoolean(String key) {
		String value = getProperty(key);
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 
	 * Description: 获取long类型的值
	 * @author bbaiggey
	 * @param  @param key
	 * @param  @return
	 * @return Long
	 * @throws
	 */
	public static Long getLong(String key) {
		String value = getProperty(key);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
	
	

}
