package org.productivity.java.habitat4j.crypto.decryptor.keyprotector;

import java.io.Serializable;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;

public interface KeyProtectorIF extends Serializable {
	public void initialize() throws DecryptorException;
	
	public KeyInfoIF decrypt(Serializable in) throws DecryptorException;
}
