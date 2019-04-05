package com.mf.PaymentProcessor.databean;

public class TokenKeyPairBean {

	private byte[] iv;
	private byte[] cipherText;
	
	public TokenKeyPairBean(byte[] iv, byte[] cipherText) {
		super();
		this.iv = iv;
		this.cipherText = cipherText;
	}
	public byte[] getIv() {
		return iv;
	}
	public byte[] getCipherText() {
		return cipherText;
	}

}
