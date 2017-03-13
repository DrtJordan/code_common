package com.bg.filter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bg.filter.Filter;
/**
 * 责任链的重要轮训方法 process
 * @author bbaiggey
 *
 */
public class FilterChain extends Filter{
	private List<Filter> filters = new ArrayList<Filter>();
	private int index;

	public FilterChain addFilter(Class<? extends Filter> clazz) {
//		this.filters.add(SpringContextUtil.getBean(clazz));
		return this;
	}

	@Override
	public void process(Map<String, Object> request,
			Map<String, Object> response, Properties properties,FilterChain chain) throws Exception {
		if (filters.size() == index)
			return;
		Filter handler = filters.get(index++);
		handler.process(request, response, properties, chain);
	}



}
