package org.code.flume.plugin.kafka.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import kafka.consumer.ConsumerIterator;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.avro.specific.SpecificDatumReader;
import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.channel.BasicChannelSemantics;
import org.apache.flume.channel.BasicTransactionSemantics;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.instrumentation.kafka.KafkaChannelCounter;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import static org.code.flume.plugin.kafka.channel.KafkaChannelConfiguration.*;
/**
 * 
 * Description: 
 * Created on:  2016年12月6日 下午2:47:23 
 * @author bbaiggey
 * @github https://github.com/bbaiggey
 */
public class MyKafkaChannel extends BasicChannelSemantics{
	 private final static Logger LOGGER =  LoggerFactory.getLogger(MyKafkaChannel.class);
	 
	 private int count=0;

	    private final Properties kafkaConf = new Properties();
	    private Producer<String, byte[]> producer;

	    private AtomicReference<String> topic = new AtomicReference<String>();
	    private boolean parseAsFlumeEvent = DEFAULT_PARSE_AS_FLUME_EVENT;
	    private final Map<String, Integer> topicCountMap =   Collections.synchronizedMap(new HashMap<String, Integer>());


	    private KafkaChannelCounter counter;
	    
	    @Override
		public void configure(Context ctx) {
	        String groupId = ctx.getString(GROUP_ID_FLUME);
	        if (groupId == null || groupId.isEmpty()) {
	            groupId = DEFAULT_GROUP_ID;
	            LOGGER.info("Group ID was not specified. Using " + groupId + " as the group id.");
	        }
	        String brokerList = ctx.getString(BROKER_LIST_FLUME_KEY);
	        if (brokerList == null || brokerList.isEmpty()) {
	        String bootStrapServers = ctx.getString(BOOTSTRAP_SERVERS_CONFIG);
	 	        if (bootStrapServers == null || bootStrapServers.isEmpty()) {
	 	          throw new ConfigurationException("Bootstrap Servers must be specified Or Broker List must be specified");
	 	    }
	        }
	       
	        String zkConnect = ctx.getString(ZOOKEEPER_CONNECT_FLUME_KEY);
	        if (zkConnect == null || zkConnect.isEmpty()) {
	            throw new ConfigurationException(
	                    "Zookeeper Connection must be specified");
	        }
	        
	        Long timeout = ctx.getLong(TIMEOUT, Long.valueOf(DEFAULT_TIMEOUT));
	        kafkaConf.putAll(ctx.getSubProperties(KAFKA_PREFIX));
	        kafkaConf.put(GROUP_ID, groupId);
	        kafkaConf.put(BROKER_LIST_KEY, brokerList);
	        kafkaConf.put(ZOOKEEPER_CONNECT, zkConnect);
	        kafkaConf.put(AUTO_COMMIT_ENABLED, String.valueOf(false));
	        kafkaConf.put(CONSUMER_TIMEOUT, String.valueOf(timeout));
	        kafkaConf.put(REQUIRED_ACKS_KEY, ctx.getString(REQUIRED_ACKS_KEY,"-1"));
	        LOGGER.info(kafkaConf.toString());
	        parseAsFlumeEvent = ctx.getBoolean(PARSE_AS_FLUME_EVENT, DEFAULT_PARSE_AS_FLUME_EVENT);

	        boolean readSmallest = ctx.getBoolean(READ_SMALLEST_OFFSET, DEFAULT_READ_SMALLEST_OFFSET);
	        // If the data is to be parsed as Flume events, we always read the smallest.
	        // Else, we read the configuration, which by default reads the largest.
	        if (parseAsFlumeEvent || readSmallest) {
	            // readSmallest is eval-ed only if parseAsFlumeEvent is false.
	            // The default is largest, so we don't need to set it explicitly.
	            kafkaConf.put("auto.offset.reset", "smallest");
	        }

	        if (counter == null) {
	            counter = new KafkaChannelCounter(getName());
	        }

	    }
	 
	 

	@Override
	public synchronized void start() {
        try {
            LOGGER.info("Starting Kafka Channel: " + getName());
            producer = new Producer<String, byte[]>(new ProducerConfig(kafkaConf));
            // We always have just one topic being read by one thread
            LOGGER.info("Topic = " + topic.get());
            topicCountMap.put(topic.get(), 1);
            counter.start();
            super.start();
        } catch (Exception e) {
            LOGGER.error("Could not start producer");
            throw new FlumeException("Unable to create Kafka Connections. " +
                    "Check whether Kafka Brokers are up and that the " +
                    "Flume agent can connect to it.", e);
        }
    }
	
	
	
	
	
	
	@Override
	public synchronized void stop() {

        producer.close();
        counter.stop();
        super.stop();
        LOGGER.info("Kafka channel {} stopped. Metrics: {}", getName(),
                counter);
    }
	



	@Override
	protected BasicTransactionSemantics createTransaction() {
		// TODO Auto-generated method stub
		return new KafkaTransaction();
	}
	
	
	private enum TransactionType {
		   PUT,
		   TAKE,
		   NONE
		 }
	public class KafkaTransaction extends BasicTransactionSemantics{
		
		private TransactionType type = TransactionType.NONE;

        // For put transactions, serialize the events and batch them and send it.
        private Optional<LinkedList<byte[]>> serializedEvents = Optional.absent();
        // For take transactions, deserialize and hold them till commit goes through
        private Optional<LinkedList<Event>> events = Optional.absent();
        private Optional<SpecificDatumReader<AvroFlumeEvent>> reader = Optional.absent();

        // Fine to use null for initial value, Avro will create new ones if this
        // is null
        private final String batchUUID = UUID.randomUUID().toString();

		@Override
		protected void doPut(Event event) throws InterruptedException {
            LOGGER.debug("event header {}",event.getHeaders());
            type = TransactionType.PUT;
            if (!events.isPresent()) {
                events = Optional.of(new LinkedList<Event>());
            }
            try {
                if(event.getHeaders().get(TOPIC)!=null){
                    events.get().add(event);
                }
            } catch (Exception e) {
                throw new ChannelException("Error while serializing event", e);
            }
        }

		@Override
		protected Event doTake() throws InterruptedException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void doCommit() throws InterruptedException {
            if (type.equals(TransactionType.NONE)) {
                return;
            }
            if (type.equals(TransactionType.PUT)) {
                try {
                    List<KeyedMessage<String, byte[]>> messages = new ArrayList<KeyedMessage<String, byte[]>>(events.get().size());
                    for (Event event : events.get()) {
                        messages.add(new KeyedMessage<String, byte[]>(event.getHeaders().get(TOPIC), null,batchUUID, event.getBody()));
                    }
                    long startTime = System.nanoTime();
                    producer.send(messages);
                    count+=messages.size();
                    long endTime = System.nanoTime();
                    counter.addToKafkaEventSendTimer((endTime - startTime) / (1000 * 1000));
                    counter.addToEventPutSuccessCount(Long.valueOf(messages.size()));
                    events.get().clear();
                    LOGGER.debug("send data to kafka {}",count);
                } catch (Exception ex) {
                    LOGGER.warn("Sending events to Kafka failed", ex);
                    throw new ChannelException("Commit failed as send to Kafka failed",ex);
                }
            }
        }

		@Override
		protected void doRollback() throws InterruptedException {
            if (type.equals(TransactionType.NONE)) {
                return;
            }
            if (type.equals(TransactionType.PUT)) {
                LOGGER.error("Sending events to Kafka failed, do roll back;count {} ",events.get().size());
                for(Event event:events.get()){
                    LOGGER.error("Event Topic is {},Body Content is {}",event.getHeaders().get(TOPIC),new String(event.getBody()));

                }
                events.get().clear();
            }
        }
		
	}
	


}
