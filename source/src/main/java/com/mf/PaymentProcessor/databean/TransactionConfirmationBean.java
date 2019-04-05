package com.mf.PaymentProcessor.databean;

public class TransactionConfirmationBean {
	
	private Boolean success;
	private String transactionID;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

}
