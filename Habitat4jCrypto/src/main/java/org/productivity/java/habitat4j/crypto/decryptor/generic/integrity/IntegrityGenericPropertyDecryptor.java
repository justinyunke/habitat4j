package org.productivity.java.habitat4j.crypto.decryptor.generic.integrity;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.common.interfaces.DecryptorIF;
import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCryptoConfig;
import org.productivity.java.habitat4j.crypto.decryptor.keyreader.KeyReader;

public class IntegrityGenericPropertyDecryptor extends IntegrityGenericCrypto implements DecryptorIF {
	private static final long serialVersionUID = 4130715925114474776L;
	
	public void initialize(String appName, String propertyListName) throws DecryptorException {
		GenericCryptoConfig config = GenericCryptoConfig.getConfig(this.getClass());

		super.initialize(config);
		
		KeyReader keyReader = getKeyReader();
		
		keyInfo = keyReader.readKey();
	}

	public String decrypt(String propertyListName, String name, String value) throws DecryptorException {
		String plain = super.decrypt(propertyListName,name,value);
		
		return plain;
	}
}
