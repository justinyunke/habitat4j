package org.productivity.java.habitat4j.crypto.decryptor.key.generic;

import java.io.Serializable;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.StaticCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;

public class GenericKeyInfo extends StaticCrypto implements KeyInfoIF {
	private static final long serialVersionUID = -5227497064499249401L;
	
	protected byte[] key = null;
	protected byte[] iv = null;
	
	protected Key keySpec = null;
	
	protected String algorithm = null;
	
	public GenericKeyInfo(String algorithm) {
		this.algorithm = algorithm;
	}

	public GenericKeyInfo(String algorithm, String in, String delimiter) throws DecryptorException {
		initialize(algorithm,in,delimiter);
	}
	
	protected void initialize(String algorithm, String in, String delimiter) throws DecryptorException {
		this.algorithm = algorithm;

		String[] values = in.split("\\" + delimiter);
		
		if (values.length < 1 || values.length > 2) {
			throw new DecryptorException("Bad KeyInfo String");
		}
		
		if (values.length >= 1) {
			setKey(decode(values[0]));
		}
		
		if (values.length == 2) {
			iv = decode(values[1]);
		}
	}

	public Serializable getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public Serializable getKey() throws DecryptorException {
		if (key == null) {
			throw new DecryptorException("No key available");
		}
		
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;

		if (key != null) {
			keySpec = new SecretKeySpec(key,algorithm);
		}
	}
	
	public Key getKeySpec() throws DecryptorException {
		if (keySpec == null) {
			throw new DecryptorException("No key (spec) available");
		}

		return keySpec;
	}

	public String toString() {
		if (iv == null) {
			return encode(key);
			
		} else {
			return encode(key) + "^" + encode(iv);
		}
	}
}
