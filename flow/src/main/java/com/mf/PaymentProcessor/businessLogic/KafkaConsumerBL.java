package com.mf.PaymentProcessor.businessLogic;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.Gson;
import com.mf.PaymentProcessor.databean.TokenKeyPairBean;
import com.mf.PaymentProcessor.databean.TokenizerBean;
import com.mf.PaymentProcessor.support.PropLoaderSupport;
import com.mf.PaymentProcessor.support.SecureDataSupportAES256;

import redis.clients.jedis.Jedis;

public class KafkaConsumerBL {

	private String TOPIC;
	private String BOOTSTRAP_SERVERS;
	private String GROUP_ID_CONFIG;
	private String CLIENT_ID_CONFIG;

	public KafkaConsumerBL(String topic, String groupId, String clientId) {
		TOPIC = topic;
		BOOTSTRAP_SERVERS = new PropLoaderSupport().getProperty("kafka.baseurl") + ":"
				+ new PropLoaderSupport().getProperty("kafka.port");
		GROUP_ID_CONFIG = groupId;
		CLIENT_ID_CONFIG = clientId;
	}

	private KafkaConsumer<String, String> createConsumer() {

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG);

		return new KafkaConsumer<String, String>(props);
	}

	public void runConsumerMod2() throws Exception {

		final KafkaConsumer<String, String> kafkaConsumer = createConsumer();
		kafkaConsumer.subscribe(Arrays.asList(TOPIC));
		try {
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10000));
				for (ConsumerRecord record : records) {
					TokenKeyPairBean tokenKeyPair = new SecureDataSupportAES256().encrypt((String) record.value());

					// Produce json to kafka output topic
					TokenizerBean mod2Tokenizer = new TokenizerBean((String) record.key(),
							tokenKeyPair.getCipherText());
					new KafkaProducerBL(new PropLoaderSupport().getProperty("kafka.topic.output"),
							new PropLoaderSupport().getProperty("mod2.producer.clientid"))
									.runProducer((String) record.key(), new Gson().toJson(mod2Tokenizer, TokenizerBean.class));

					// store decryption key to redis
					Jedis jedis = new Jedis(new PropLoaderSupport().getProperty("redis.baseurl"));
					byte[] keyStringToByte = ((String) record.key()).getBytes();
					jedis.set(keyStringToByte, tokenKeyPair.getIv());
					jedis.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			kafkaConsumer.close();
		}
	}
}
