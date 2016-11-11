package com.threadpool.RejectedExecutionHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 
 *
 */
public class TestThreadPoolMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//设置配置
		ThreadPoolConfig config = ThreadPoolConfig.getInstance();
		config.setCorePoolSize(2);
		config.setMaximumPoolSize(3);
		config.setKeepAliveTime(3);
		config.setUnit(TimeUnit.SECONDS);
		//将队列设小，会抛异常
		config.setWorkQueue(new ArrayBlockingQueue<Runnable>(10));
		config.setHandler(new MyRejectedExecutionHandler());
		//线程池工厂
		ThreadPoolFactory factory = ThreadPoolFactory.getInstance(config);
		
		for(int i = 0;i<13;i++){
			factory.addTask(new ThreadTask(i+"-i"));
		}
		System.out.println("i add is over!-------------------");
	}
}