package org.code.hbase.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
/**
 * 
 * Description: 
 * Created on:  2016年9月20日 下午4:25:12 
 * @author bbaiggey
 * @github https://github.com/bbaiggey
 */
public interface HBaseDAO {

	public void save(Put put,String tableName) ;
	
	public void insert(String tableName,String rowKey,String family,String quailifer,String value) ;
	
	public void insert(String tableName,String rowKey,String family,String quailifer[],String value[]) ;
	
	public void save(List<Put>Put ,String tableName) ;
	
	public Result getOneRow(String tableName,String rowKey) ;
	
	public List<Result> getRows(String tableName,String rowKey_like) ;
	
	public List<Result> getRows(String tableName, String rowKeyLike, String cols[]) ;
	
	public List<Result> getRows(String tableName,String startRow,String stopRow) ;
}
