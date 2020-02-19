package org.productivity.java.habitat4j.crypto.decryptor.generic.integrity;

import org.productivity.java.habitat4j.crypto.decryptor.generic.GenericCryptoConfig;


public class IntegrityGenericCryptoConfig extends GenericCryptoConfig {
	private static final long serialVersionUID = -7131922946078859391L;
	
	protected static final String DEFAULT_DELIMITER = "|";

	protected String hashAlgorithm = null;
	protected String delimiter = DEFAULT_DELIMITER;
	
	public String getHashAlgorithm() {
		return hashAlgorithm;
	}
	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}
