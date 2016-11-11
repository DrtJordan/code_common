/**
 * File Name：CityService.java
 *
 * Copyright Defonds Corporation 2015 
 * All Rights Reserved
 *
 */
package org.code.redisCache.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.code.redisCache.bean.City;
import org.code.redisCache.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Project Name：bdp
 * Type Name：CityService
 * Type Description：
 * Author：Defonds
 * Create Date：2015-08-31
 * @version 
 * 
 */
@Service
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class CityService { 
	private final Log logger = LogFactory.getLog(this.getClass());
	@Autowired
	private CityMapper cityMapper;
	
	// C
	@CacheEvict(value = { "provinceCities"}, allEntries = true)
	public void insertCity(String city_code, String city_jb, 
			String province_code, String city_name,
			String city, String province) {
		City cityBean = new City();
		cityBean.setCityCode(city_code);
		cityBean.setCityJb(city_jb);
		cityBean.setProvinceCode(province_code);
		cityBean.setCityName(city_name);
		cityBean.setCity(city);
		cityBean.setProvince(province);
		this.cityMapper.insertCity(cityBean);
	}
	
	// R
	@Cacheable("provinceCities")
	public List<City> provinceCities(String province) {
		logger.debug("province=" + province);
		return this.cityMapper.provinceCities(province);
	}
	
	// R
	@Cacheable("searchCity")
	public City searchCity(String city_code){
		logger.debug("city_code=" + city_code);
		return this.cityMapper.searchCity(city_code);	
	}
	
	// U
	@CacheEvict(value = { "provinceCities", "searchCity" }, allEntries = true)
	public int renameCity(String city_code, String city_name) {
		City city = new City();
		city.setCityCode(city_code);
		city.setCityName(city_name);
		this.cityMapper.renameCity(city);
		return 1;
	}
	
	// D
	@CacheEvict(value = { "provinceCities", "searchCity" }, allEntries = true)
	public int deleteCity(String city_code) {
		this.cityMapper.deleteCity(city_code);
		return 1;
	}
}