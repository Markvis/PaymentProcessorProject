package com.mf.PaymentProcessor.businessLogic;

import java.util.UUID;

import com.google.gson.Gson;
import com.mf.PaymentProcessor.databean.CreditCardBean;
import com.mf.PaymentProcessor.databean.TransactionConfirmationBean;
import com.mf.PaymentProcessor.support.PropLoaderSupport;

public class ProcessPaymentBL {

	CreditCardBean creditCardDataBean;

	public ProcessPaymentBL(CreditCardBean creditCardDataBean) {
		this.creditCardDataBean = creditCardDataBean;
	}

	public TransactionConfirmationBean process() {

		String transactionID = UUID.randomUUID().toString();
		TransactionConfirmationBean transactionConfirmationBean = new TransactionConfirmationBean();
		transactionConfirmationBean.setTransactionID(transactionID);

		try {
			new KafkaProducerBL(new PropLoaderSupport().getProperty("kafka.topic.input"),
					new PropLoaderSupport().getProperty("mod1.producer.clientid")).runProducer(transactionID,
							new Gson().toJson(creditCardDataBean, CreditCardBean.class));
			transactionConfirmationBean.setSuccess(true);
		} catch (Exception e) {
			transactionConfirmationBean.setSuccess(false);
		}

		return transactionConfirmationBean;
	}

}
