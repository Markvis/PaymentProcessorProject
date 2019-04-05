package com.mf.PaymentProcessor.support;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.mf.PaymentProcessor.databean.TokenKeyPairBean;

public class SecureDataSupportAES256 {

	private Cipher cipher = null;
	private char[] password = null;
	private byte[] salt = null;
	private SecretKey secret = null;
	private int iteration = 256;
	private int keySize = 256;

	public SecureDataSupportAES256() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		password = new PropLoaderSupport().getProperty("secret.password").toCharArray();
		salt = new PropLoaderSupport().getProperty("secret.salt").getBytes();
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password, salt, iteration, keySize);
		SecretKey tmp = factory.generateSecret(spec);
		secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	}

	public TokenKeyPairBean encrypt(String plainTextData)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] ciphertext = cipher.doFinal(plainTextData.getBytes("UTF-8"));

		return new TokenKeyPairBean(iv, ciphertext);
	}

	public String decrypt(TokenKeyPairBean tokenKeyPair) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(tokenKeyPair.getIv()));
		return new String(cipher.doFinal(tokenKeyPair.getCipherText()), "UTF-8");
	}
}
