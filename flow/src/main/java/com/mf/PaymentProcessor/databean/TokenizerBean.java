package com.mf.PaymentProcessor.databean;

public class TokenizerBean {

	String transactionId;
	byte[] token;
	
	public TokenizerBean(String transactionId, byte[] token) {
		super();
		this.transactionId = transactionId;
		this.token = token;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public byte[] getToken() {
		return token;
	}
	
}
