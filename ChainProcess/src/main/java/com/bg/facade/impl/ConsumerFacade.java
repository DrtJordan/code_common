package com.bg.facade.impl;

import java.util.Map;
import java.util.Properties;

import com.bg.facade.Facade;
import com.bg.filter.impl.FilterChain;
/**
 * 这里实现自己的接口 进行定义责任链中的成员并且调用责任链中的处理方法进行处理操作
 * @author bbaiggey
 *
 */
public class ConsumerFacade  extends Facade{

	@Override
	public void process(Map<String, Object> request,
			Map<String, Object> response, Properties properties)
			throws Exception {

		FilterChain chain = new FilterChain();
//		chain.addFilter(MerchantInfoFilter.class);
//		chain.addFilter(FactoryCheckAllowNullFilter.class);
//		chain.addFilter(MacCheckFilter.class);
//		chain.addFilter(CardholderInfoFilter.class);
//		chain.addFilter(QuickPayFilter.class);
//		chain.addFilter(CreateTradeFlowingFilter.class);
//		chain.addFilter(MerchantInfoCheckFilter.class);
//		chain.addFilter(PauseTradeCheckFilter.class);
//		chain.addFilter(ConsumeAuthorCheckFilter.class);
//		chain.addFilter(CardCheckFilter.class);
//		chain.addFilter(BatCheckFilter.class);
//		chain.addFilter(RiskControlCheckFilter.class);
//		chain.addFilter(MerchantRateInfoFilter.class);
//		chain.addFilter(MerchantFeeFilter.class);
//		chain.addFilter(RoutingFilter.class);
//		chain.addFilter(ConsumeTradeFilter.class);
//		chain.addFilter(ChangeTicketInfoFilter.class);
		
		chain.process(request, response, properties, chain);
	
		
	}

}
