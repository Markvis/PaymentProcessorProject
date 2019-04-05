package com.mf.PaymentProcessor.businessLogic;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.mf.PaymentProcessor.support.PropLoaderSupport;

public class KafkaProducerBL {

	private String TOPIC;
	private String BOOTSTRAP_SERVERS;
	private String CLIENT_ID_CONFIG;

	KafkaProducerBL(String topic, String clientID) {
		TOPIC = topic;
		BOOTSTRAP_SERVERS = new PropLoaderSupport().getProperty("kafka.baseurl") + ":"
				+ new PropLoaderSupport().getProperty("kafka.port");
		CLIENT_ID_CONFIG = clientID;
	}

	private Producer<String, String> createProducer() {

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return new KafkaProducer<>(props);
	}

	void runProducer(final String transactionId, final String data) throws Exception {

		final Producer<String, String> producer = createProducer();

		try {
			final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, transactionId, data);
			RecordMetadata metadata = producer.send(record).get();
			System.out.println(metadata);

		} finally {
			producer.flush();
			producer.close();
		}
	}

}
