package org.productivity.java.habitat4j.crypto.decryptor.generic.simple;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCryptoConfig;

public abstract class SimpleGenericDecryptor extends GenericCrypto {
	public abstract GenericCryptoConfig getConfig();
	public abstract String getKey();
	
	public void initialize(String appName, String propertyListName) throws DecryptorException {
		GenericCryptoConfig config = getConfig();
			
		super.initialize(config);
		
		String key = getKey();
		
		keyInfo = config.getKeyInfo(key);
	}
	
	public String decrypt(String propertyListName, String name, String value) throws DecryptorException {
		byte[] valueBytes = decode(value);
		
		String plain = decrypt(valueBytes);
		
		return plain;
	}
}
