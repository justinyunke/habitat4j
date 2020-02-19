package org.productivity.java.habitat4j.crypto.decryptor;

import java.io.Serializable;

public abstract class CryptoConfig implements Serializable {
	protected static final String DEFAULT_KEY_DELIMITER = "^";
	
	protected String keyReaderClassName = null;
	protected String keyLocation = null;
	protected String keyDelimiter = DEFAULT_KEY_DELIMITER;

	public String getKeyLocation() {
		return keyLocation;
	}

	public void setKeyLocation(String keyLocation) {
		this.keyLocation = keyLocation;
	}

	public String getKeyReaderClassName() {
		return keyReaderClassName;
	}

	public void setKeyReaderClassName(String keyReaderClassName) {
		this.keyReaderClassName = keyReaderClassName;
	}

	public String getKeyDelimiter() {
		return keyDelimiter;
	}

	public void setKeyDelimiter(String keyDelimiter) {
		this.keyDelimiter = keyDelimiter;
	}
}
