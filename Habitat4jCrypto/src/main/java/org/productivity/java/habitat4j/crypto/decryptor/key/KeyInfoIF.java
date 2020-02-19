package org.productivity.java.habitat4j.crypto.decryptor.key;

import java.io.Serializable;

import org.productivity.java.habitat4j.common.exception.DecryptorException;

public interface KeyInfoIF extends Serializable {
	public Serializable getKey() throws DecryptorException;
	public Serializable getIv() throws DecryptorException;
}
