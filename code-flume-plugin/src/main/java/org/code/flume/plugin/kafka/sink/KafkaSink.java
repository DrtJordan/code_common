package org.code.flume.plugin.kafka.sink;

import static org.code.flume.plugin.kafka.channel.KafkaChannelConfiguration.BROKER_LIST_FLUME_KEY;
import static org.code.flume.plugin.kafka.sink.KafkaSinkConstants.*;

import java.util.Properties;

import kafka.producer.KeyedMessage;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.conf.LogPrivacyUtil;
import org.apache.flume.sink.AbstractSink;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//静态导入
/**
 * 
 * @author bbaiggey
 *
 */
public class KafkaSink extends AbstractSink implements Configurable{
	
	private final static Logger LOGGER =  LoggerFactory.getLogger(KafkaSink.class);
	Properties kafkaProps = new Properties();
	KafkaProducer<String, byte[]> producer;
	
	String topic;

	@Override
	public void configure(Context ctx) {
		//兼容旧的属性
		CompatibleOldProp(ctx);
		//topic
		String topicStr = ctx.getString(TOPIC_CONFIG);
	    if (topicStr == null || topicStr.isEmpty()) {
		      topicStr = DEFAULT_TOPIC;
		      LOGGER.warn("Topic was not specified. Using {} as the topic.", topicStr);
	    } else {
	    	LOGGER.info("Using the static topic {}. This may be overridden by event headers", topicStr);
	    }
	    topic = topicStr;
	    //batchSize
	    Integer batchSize = ctx.getInteger(BATCH_SIZE, DEFAULT_BATCH_SIZE);

	    if (LOGGER.isDebugEnabled()) {
	    	LOGGER.debug("Using batch size: {}", batchSize);
	    }
	    //kafka broker servers
	    String bootStrapServers = ctx.getString(BOOTSTRAP_SERVERS_CONFIG);
	    if (bootStrapServers == null || bootStrapServers.isEmpty()) {
	      throw new ConfigurationException("Bootstrap Servers must be specified");
	    }
	    //set kafka producer‘s properties
	    setProducerProps(ctx, bootStrapServers);
	    
	    if (LOGGER.isDebugEnabled() && LogPrivacyUtil.allowLogPrintConfig()) {
	    	LOGGER.debug("Kafka producer properties: {}", kafkaProps);
	      }

		
	}
	
	/**
	 * 设置kafka的配置信息
	 * @param context
	 * @param bootStrapServers
	 */
	private void setProducerProps(Context context, String bootStrapServers) {
	    kafkaProps.clear();
	    kafkaProps.put(ProducerConfig.ACKS_CONFIG, DEFAULT_ACKS);
	    //Defaults overridden based on config
	    kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, DEFAULT_KEY_SERIALIZER);
	    kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DEFAULT_VALUE_SERIAIZER);
	    kafkaProps.putAll(context.getSubProperties(KAFKA_PRODUCER_PREFIX));
	    kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
	  }
	/**
	 * 兼容旧的配置属性
	 * @param ctx
	 */
	private void CompatibleOldProp(Context ctx) {

	    if (!(ctx.containsKey(TOPIC_CONFIG))) {
	      ctx.put(TOPIC_CONFIG, ctx.getString("topic"));
	      LOGGER.warn("{} is deprecated. Please use the parameter {}", "topic", TOPIC_CONFIG);
	    }

	    //Broker List
	    // If there is no value we need to check and set the old param and log a warning message
	    if (!(ctx.containsKey(BOOTSTRAP_SERVERS_CONFIG))) {
	      String brokerList = ctx.getString(BROKER_LIST_FLUME_KEY);
	      if (brokerList == null || brokerList.isEmpty()) {
	        throw new ConfigurationException("Bootstrap Servers must be specified");
	      } else {
	        ctx.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
	        LOGGER.warn("{} is deprecated. Please use the parameter {}",
	                    BROKER_LIST_FLUME_KEY, BOOTSTRAP_SERVERS_CONFIG);
	      }
	    }

	    //batch Size
	    if (!(ctx.containsKey(BATCH_SIZE))) {
	      String oldBatchSize = ctx.getString(OLD_BATCH_SIZE);
	      if ( oldBatchSize != null  && !oldBatchSize.isEmpty())  {
	        ctx.put(BATCH_SIZE, oldBatchSize);
	        LOGGER.warn("{} is deprecated. Please use the parameter {}", OLD_BATCH_SIZE, BATCH_SIZE);
	      }
	    }

	    // Acks
	    if (!(ctx.containsKey(KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG))) {
	      String requiredKey = ctx.getString(
	              REQUIRED_ACKS_FLUME_KEY);
	      if (!(requiredKey == null) && !(requiredKey.isEmpty())) {
	        ctx.put(KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG, requiredKey);
	        LOGGER.warn("{} is deprecated. Please use the parameter {}", REQUIRED_ACKS_FLUME_KEY,
	                KAFKA_PRODUCER_PREFIX + ProducerConfig.ACKS_CONFIG);
	      }
	    }

	    if (ctx.containsKey(KEY_SERIALIZER_KEY )) {
	      LOGGER.warn("{} is deprecated. Flume now uses the latest Kafka producer which implements " +
	          "a different interface for serializers. Please use the parameter {}",
	          KEY_SERIALIZER_KEY,KAFKA_PRODUCER_PREFIX + ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG);
	    }

	    if (ctx.containsKey(MESSAGE_SERIALIZER_KEY)) {
	      LOGGER.warn("{} is deprecated. Flume now uses the latest Kafka producer which implements " +
	                  "a different interface for serializers. Please use the parameter {}",
	                  MESSAGE_SERIALIZER_KEY,
	                  KAFKA_PRODUCER_PREFIX + ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);
	    }
	  }

	@Override
	public synchronized void start() {
		// instantiate the producer
	    producer = new KafkaProducer<String,byte[]>(kafkaProps);
		super.start();
	}
	//简单事务  单条事务 效率不够高   没有官方的 bath事务效率高
	@Override
	public Status process() throws EventDeliveryException {
		Status state = null;
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		transaction.begin();
		
		try{
			Event event = channel.take();
			if(event==null){
				transaction.rollback();
				state = Status.BACKOFF;
				return state;
			}
			String eventTopic = event.getHeaders().get(TOPIC_HEADER);
			
			if (eventTopic==null) {
				eventTopic = topic;
			}
			
			ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, event.getBody());
			producer.send(record);
		
			transaction.commit();
			state = Status.READY;
		}catch(Exception E){
			transaction.rollback();
			state = Status.BACKOFF;
		}finally{
			transaction.close();
		}
		
		return state;
	}


	@Override
	public synchronized void stop() {
		  producer.close();
		  LOGGER.info("Kafka Sink {} stopped. Metrics: {}", getName());
		super.stop();
	}


	

	

}
