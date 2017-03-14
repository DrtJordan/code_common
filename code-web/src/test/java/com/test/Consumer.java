package com.test;

import org.code.core.utils.SpringContextUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Consumer {

	
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring-context.xml" });
		context.start();
		System.out.println(SpringContextUtil.getBean(SpringContextUtil.class));
//		SysUserFacade sf = (SysUserFacade) context.getBean("sysUserService");
		
		
		
	}

}