package org.code.core.utils;

import java.math.BigDecimal;

/**
 * 
 * Description: 数字格式化类
 * Created on:  2016年8月10日 下午3:00:47 
 * @author bbaiggey
 * @github https://github.com/bbaiggey
 */
public class NumberUtils {

	/**
	 * 格式化小数
	 * @param str 字符串
	 * @param scale 四舍五入的位数
	 * @return 格式化小数
	 */
	public static double formatDouble(double num, int scale) {
		BigDecimal bd = new BigDecimal(num);  
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
}
