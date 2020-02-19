package org.productivity.java.habitat4j.crypto.decryptor.keyreader;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.CryptoConfig;
import org.productivity.java.habitat4j.crypto.decryptor.StaticCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;
import org.productivity.java.habitat4j.crypto.decryptor.keyprotector.KeyProtectorIF;

public abstract class KeyReader extends StaticCrypto {
	protected static String KEY_PROTECTOR_CLASS = "KeyProtector";

	protected final static String[] DEFAULT_KEYREADER_ALIASES = {
		"ServerIdentityKeyReader", "org.productivity.java.habitat4j.crypto.decryptor.keyreader.serveridentity.ServerIdentityKeyReader",
		"FileKeyReader", "org.productivity.java.habitat4j.crypto.decryptor.keyreader.file.FileKeyReader",
		"ClasspathKeyReader", "org.productivity.java.habitat4j.crypto.decryptor.keyreader.classpath.ClasspathKeyReader"
	};
	
	protected static Map KEYREADER_ALIAS_MAP = null;
	
	static {
		KEYREADER_ALIAS_MAP = new HashMap();
		
		for(int i=0; i<DEFAULT_KEYREADER_ALIASES.length; i+=2) {
			KEYREADER_ALIAS_MAP.put(DEFAULT_KEYREADER_ALIASES[i],DEFAULT_KEYREADER_ALIASES[i+1]);
		}
	}

	protected CryptoConfig config = null;
	
	public void initialize(CryptoConfig config) {
		this.config = config;
	}
	
	public abstract Serializable readProtectedKey() throws DecryptorException;
	
	public KeyInfoIF readKey() throws DecryptorException {
		Serializable protectedKey = readProtectedKey();
		
		KeyProtectorIF keyProtector = createKeyProtector(KEY_PROTECTOR_CLASS);
		
		KeyInfoIF unprotectedKey = keyProtector.decrypt(protectedKey);
		
		return unprotectedKey;
	}
	
	protected static Map aliasMap = null;
	
	protected static String lookupAlias(String alias) {
		String className = (String) KEYREADER_ALIAS_MAP.get(alias);
		
		return className;
	}
	
	protected static KeyProtectorIF createKeyProtector(String className) throws DecryptorException {
		Class clazz = null;
		
		try {
			clazz = Class.forName(className);

		} catch (ClassNotFoundException cnfe) {
			throw new DecryptorException("KeyProtector class \"" + className + "\" not found");
		}
		
		KeyProtectorIF keyProtector = null;
		
		try {
			keyProtector = (KeyProtectorIF) clazz.newInstance();
			keyProtector.initialize();
			
		} catch (ClassCastException cce) {
			throw new DecryptorException(cce);
			
		} catch (InstantiationException ie) {
			throw new DecryptorException(ie);
			
		} catch (IllegalAccessException iae) {
			throw new DecryptorException(iae);
		}
		
		return keyProtector; 
	}

	
	public static KeyReader createKeyReader(CryptoConfig config) throws DecryptorException {
		String keyReaderClassName = config.getKeyReaderClassName();
		
		if (keyReaderClassName == null || "".equals(keyReaderClassName.trim())) {
			throw new DecryptorException("Invalid keyReaderClassName");
		}
		
		String classNameByAlias = lookupAlias(keyReaderClassName);
		
		if (classNameByAlias != null) {
			keyReaderClassName = classNameByAlias;
		}
		
		KeyReader keyReader = null;
		
		try {
			Class clazz = Class.forName(keyReaderClassName);
			
			keyReader = (KeyReader) clazz.newInstance();
			keyReader.initialize(config);
			
		} catch (ClassNotFoundException cnfe) {
			throw new DecryptorException(cnfe);
			
		} catch (InstantiationException ie) {
			throw new DecryptorException(ie);

		} catch (IllegalAccessException iae) {
			throw new DecryptorException(iae);
		}
		
		return keyReader;
	}
	
	public static void setKeyProtector(String clazz) throws DecryptorException {
		if (clazz != null) {
			KEY_PROTECTOR_CLASS = clazz;
			
		} else {
			throw new DecryptorException("Class parameter cannot be null");
		}
	}

	public static void addKeyReaderAlias(String alias, Class clazz) throws DecryptorException {
		if (alias == null || "".equals(alias.trim())) {
			throw new DecryptorException("Alias parameter cannot be null");
		}

		if (clazz == null) {
			throw new DecryptorException("Class parameter cannot be null");
		}

		KEYREADER_ALIAS_MAP.put(alias,clazz.getName());
	}
}
