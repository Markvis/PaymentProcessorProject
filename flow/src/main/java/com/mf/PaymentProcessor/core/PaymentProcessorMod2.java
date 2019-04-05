package com.mf.PaymentProcessor.core;

import com.mf.PaymentProcessor.businessLogic.KafkaConsumerBL;
import com.mf.PaymentProcessor.support.PropLoaderSupport;

public class PaymentProcessorMod2 {

	public static void main(String args[]) {

		try {
			// Data Flow (Tokenizer, module #2):
			KafkaConsumerBL kafkaConsumerBL = new KafkaConsumerBL(
					new PropLoaderSupport().getProperty("kafka.topic.input"),
					new PropLoaderSupport().getProperty("mod2.consumer.groupid"),
					new PropLoaderSupport().getProperty("mod2.consumer.clientid"));

			kafkaConsumerBL.runConsumerMod2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
