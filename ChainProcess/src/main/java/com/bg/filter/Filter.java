package com.bg.filter;

import java.util.Map;
import java.util.Properties;

import com.bg.filter.impl.FilterChain;


public abstract class Filter {
	
	public abstract void process(Map<String, Object> request, Map<String, Object> response,
			Properties properties, FilterChain chain) throws Exception;

}
