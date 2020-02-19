package org.productivity.java.habitat4j.crypto.decryptor.generic;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.CryptoConfig;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;
import org.productivity.java.habitat4j.crypto.decryptor.key.generic.GenericKeyInfo;

public class GenericCryptoConfig extends CryptoConfig {
	private static final long serialVersionUID = 8926355767632434370L;
	
	protected static final String DEFAULT_DATA_CHAR_SET = "UTF-8";
	protected static final boolean DEFAULT_DATA_PADDING = false;
	
	protected String keyAlgorithm = null;
	protected String cipherAlgorithm = null;
	
	protected boolean useDataPadding = DEFAULT_DATA_PADDING;
	protected String dataCharSet = DEFAULT_DATA_CHAR_SET;

	protected static String configPropertyListName = Habitat4JConstants.HABITAT4J_PROPERTY_LIST_NAME_DEFAULT;
	
	public static String getConfigPropertyListName() {
		return configPropertyListName;
	}

	public static void setConfigPropertyListName(String configPropertyListName) {
		GenericCryptoConfig.configPropertyListName = configPropertyListName;
	}
	
	public static GenericCryptoConfig getConfig(Class clazz) throws DecryptorException {
		GenericCryptoConfig config = (GenericCryptoConfig) PropertyListManager.getPropertyBean(getConfigPropertyListName(),clazz.getName());

		if (config == null) {
			throw new DecryptorException("GenericCryptoConfig bean was not found in list \"" + getConfigPropertyListName() +"\" and name \"" + clazz.getName() + "\"");
		}
		
		return config;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}
	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}
	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}
	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}
	public String getDataCharSet() {
		return dataCharSet;
	}
	public void setDataCharSet(String dataCharSet) {
		this.dataCharSet = dataCharSet;
	}
	public boolean isUseDataPadding() {
		return useDataPadding;
	}
	public void setUseDataPadding(boolean useDataPadding) {
		this.useDataPadding = useDataPadding;
	}
	
	public KeyInfoIF getKeyInfo(String in) throws DecryptorException {
		KeyInfoIF keyInfo = new GenericKeyInfo(getKeyAlgorithm(),in,getKeyDelimiter());
		
		return keyInfo;
	}
}
