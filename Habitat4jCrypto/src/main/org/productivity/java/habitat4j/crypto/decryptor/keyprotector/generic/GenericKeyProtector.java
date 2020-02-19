package org.productivity.java.habitat4j.crypto.decryptor.keyprotector.generic;

import java.io.Serializable;

import javax.crypto.Cipher;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCryptoConfig;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;
import org.productivity.java.habitat4j.crypto.decryptor.key.generic.GenericKeyInfo;
import org.productivity.java.habitat4j.crypto.decryptor.keyprotector.KeyProtectorIF;

public abstract class GenericKeyProtector extends GenericCrypto implements KeyProtectorIF {
	protected KeyInfoIF keyInfo = null;
	
	public abstract void initialize() throws DecryptorException;
	
	public void configure(GenericCryptoConfig config, KeyInfoIF keyInfo) throws DecryptorException {
		this.config = config;
		this.keyInfo = keyInfo;
	}
	
	public KeyInfoIF decrypt(Serializable in) throws DecryptorException {
		byte[] inBytes = (byte[]) in;
		
		byte[] outBytes = xcrypt(Cipher.DECRYPT_MODE,keyInfo,inBytes,config.getKeyAlgorithm(),config.getCipherAlgorithm(),config.isUseDataPadding());
		
		String out = createString(outBytes);
		
		KeyInfoIF keyInfo = new GenericKeyInfo(config.getKeyAlgorithm(),out,config.getKeyDelimiter());
		
		return keyInfo;
	}
}
