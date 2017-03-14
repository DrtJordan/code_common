package com.bg.facade;

import java.util.Map;
import java.util.Properties;


public abstract class Facade {
	
	public abstract void process(Map<String, Object> request, Map<String, Object> response,
			Properties properties) throws Exception;

}
