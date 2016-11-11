package org.code.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量接口
 * Description: 配置程序中的用到的一些常量
 * Created on:  2016年8月10日 下午1:37:56 
 * @author bbaiggey
 * @github https://github.com/bbaiggey
 */
public interface Constants {

	/**
	 * 数据库相关配置信息
	 */
	public static String JDBC_DRIVER="jdbc.driver";
	public static String JDBC_DATASOURCE_SIZE="jdbc.datasource.size";
	public static String JDBC_URL="jdbc.url";
	public static String JDBC_USER="jdbc.user";
	public static String JDBC_PASSWORD="jdbc.password";
	public static String JDBC_URL_PROD = "jdbc.url.prod";
	public static String JDBC_USER_PROD = "jdbc.user.prod";
	public static String JDBC_PASSWORD_PROD = "jdbc.password.prod";
	
//zk hbase kafka	
	public static String ZOOKEEPER_CONNECT="zookeeper.connect";
	public static String GROUP_ID="group.id";
	public static String SERIALIZER_CLASS="serializer.class";
	public static String METADATA_BROKER_LIST="metadata.broker.list";
	public static String TOPIC="topic";
	public static String HBASE_ZK_LIST="hbase.zookeeper.quorum";
	public static String IS_LOCAL = "is_local";
	

    /** 判断代码：是 */
    public static final String TRUE = "1";

    /** 判断代码：否 */
    public static final String FALSE = "0";

    /** 通用字符集编码 */
    public static final String CHARSET_UTF8 = "UTF-8";

    /** 中文字符集编码 */
    public static final String CHARSET_CHINESE = "GBK";

    /** 英文字符集编码 */
    public static final String CHARSET_LATIN = "ISO-8859-1";

    /** 根节点ID */
    public static final String ROOT_ID = "root";

    /** NULL字符串 */
    public static final String NULL = "null";

    /** 日期格式 */
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    /** 日期时间格式 */
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 时间戳格式 */
    public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    
    /** JSON成功标记 */
    public static final String JSON_SUCCESS = "success";

    /** JSON数据 */
    public static final String JSON_DATA = "data";

    /** JSON数据列表 */
    public static final String JSON_ROWS = "rows";
    
    /** JSON总数 */
    public static final String JSON_TOTAL = "total";

    /** JSON消息文本 */
    public static final String JSON_MESSAGE = "message";
    
    public static final String TAG_SYS = "sys";
    
    public static final String TAG_MST = "mst";
    
    public static final String TAG_MQ = "mq";
    
    public static final String TAG_DAT = "dat";
    
    public static final String TAG_STA = "sta";
    
    public static final String TAG_INT = "int";  
    
    public static final String[] TAGS = {TAG_SYS, TAG_MST, TAG_MQ, TAG_DAT, TAG_STA, TAG_INT};
    
    /** Cookie键值：验证键值 */
    public static final String COOKIE_VALIDATE_KEY = "VALIDATE_KEY";

    /** Cookie键值：验证键值分割符 */
    public static final String COOKIE_VALIDATE_KEY_SPLIT = "$_";

    /** 请求属性键值：当前项目标识 */
    public static final String REQ_CUR_TAG = "REQ_CUR_TAG";
    
    /** 请求属性键值：当前用户标识 */
    public static final String REQ_CUR_USER_ID = "CUR_USER_ID";

    /** 请求属性键值：当前用户名称 */
    public static final String REQ_CUR_USER_NAME = "CUR_USER_NAME";

    /** 请求属性键值：当前机构标识 */
    public static final String REQ_CUR_ORG_ID = "CUR_ORG_ID";

    /** 请求属性键值：当前角色名称 */
    public static final String REQ_CUR_ROLE_CODE = "CUR_ROLE_CODE";
	
	/** es:常量*/
    public static final Map<String, String> map = new HashMap<String, String>();
    public static final String CLOSED_MSG = "#################closed####################";
    public static final long DELIVERIED_TAG = -1;
  
    class ESProp {  
        public static final String INDEX_NAME = "heros";
        public static final String DAIDONGXI_INDEX_NAME = "daidongxi";
        public static final String TYPE_NEWS_INFO = "news_info";
        public static final String TYPE_PRODUCT_INFO = "product_info";
        public static final String TYPE_STORY_INFO = "story_info";
        public static final String TYPE_TASK_INFO = "task_info";
        
  
        public static final String TYPE_PERSON_INFO = "person_info";
        
        
        public static final String TYPE_USER_INFO = "user_info";
        public static final String TYPE_BRANDCASE_INFO = "brandcase_info";
        public static final String INDEX_STORE_TYPE = "memory";
        public static final int SHARDS = 2;
        public static final int REPLICAS = 1;
        public static final String REFRESH_INTERVAL = "-1";
    }  
	
	
}
