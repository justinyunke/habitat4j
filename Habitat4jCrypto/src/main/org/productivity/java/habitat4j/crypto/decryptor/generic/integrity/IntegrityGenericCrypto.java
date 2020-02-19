package org.productivity.java.habitat4j.crypto.decryptor.generic.integrity;

import javax.crypto.Cipher;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;
import org.productivity.java.habitat4j.crypto.decryptor.key.generic.GenericKeyInfo;

public abstract class IntegrityGenericCrypto extends GenericCrypto {
	private static final long serialVersionUID = -415909784850521810L;

	protected String createPropertyPath(String propertyListName, String name, String value) {
		StringBuffer buffer = new StringBuffer();
		
		IntegrityGenericCryptoConfig integrityGenericCryptoConfig = (IntegrityGenericCryptoConfig) config;
		
		buffer.append(propertyListName);
		buffer.append(integrityGenericCryptoConfig.getDelimiter());
		buffer.append(name);
		buffer.append(integrityGenericCryptoConfig.getDelimiter());
		buffer.append(value);
		
		return buffer.toString();
	}
	
	protected byte[] createHash(String value) throws DecryptorException {
		IntegrityGenericCryptoConfig integrityGenericCryptoConfig = (IntegrityGenericCryptoConfig) config;
		
		byte[] digest = createHash(value,integrityGenericCryptoConfig.getHashAlgorithm());
		
		return digest;
	}
	
	protected String decryptPropertyValue(String propValueKey, String propValueEncrypted) throws DecryptorException {
		KeyInfoIF keyInfo = new GenericKeyInfo(config.getKeyAlgorithm(),propValueKey,config.getKeyDelimiter());

		byte[] propValueEncryptedBytes = decode(propValueEncrypted);
		
		byte[] propValueBytes = xcrypt(Cipher.DECRYPT_MODE,keyInfo,propValueEncryptedBytes,config.getKeyAlgorithm(),config.getCipherAlgorithm(),config.isUseDataPadding());
		
		String propValue = createString(propValueBytes);
		
		return propValue;
	}
	
	protected String validate(String propertyListName, String name, String value) throws DecryptorException {
		IntegrityGenericCryptoConfig integrityGenericCryptoConfig = (IntegrityGenericCryptoConfig) config;
		
		String[] values = value.split("\\" + integrityGenericCryptoConfig.getDelimiter());
		
		if (values.length != 3) {
			throw new DecryptorException("value");
		}
		
		String propValueKey = values[0];
		String propValueEncrypted = values[1];
		String propHash = values[2];
		
		String propValue = decryptPropertyValue(propValueKey,propValueEncrypted);
		
		String origPropertyPath = createPropertyPath(propertyListName,name,propValue);
		
		String hash = encode(createHash(origPropertyPath));
		
		if (!hash.equals(propHash)) {
			throw new DecryptorException("integrity failures - hash didn't match");
		}
			
		return propValue;
	}
	
	protected String decrypt(String listName, String propName, String propValue) throws DecryptorException {
		byte[] decodedPropValue = decode(propValue);
		
		String plain = decrypt(decodedPropValue);
		
		String propValuePlain = validate(listName,propName,plain);
		
		return propValuePlain;
	}
}
