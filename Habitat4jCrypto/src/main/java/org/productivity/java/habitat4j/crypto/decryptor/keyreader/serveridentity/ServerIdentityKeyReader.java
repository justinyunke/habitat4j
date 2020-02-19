package org.productivity.java.habitat4j.crypto.decryptor.keyreader.serveridentity;

import java.io.Serializable;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.ServerIdentity;
import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.keyreader.KeyReader;

public class ServerIdentityKeyReader extends KeyReader {
	private static final long serialVersionUID = -1525784794567750431L;
	
	protected String readKeyFromServerIdentity(String keyLocation) throws DecryptorException {
		ServerIdentity serverIdentity = PropertyListManager.getServerIdentity();
		
		if (keyLocation == null || "".equals(keyLocation.trim())) {
			throw new DecryptorException("No keyLocation specified");
		}
		
		String keyEncoded = serverIdentity.getPragmaDefinition(keyLocation);

		if (keyEncoded == null || "".equals(keyEncoded.trim())) {
			throw new DecryptorException("No key found in \"" + keyLocation + "\"");
		}

		return keyEncoded;
	}
	
	public Serializable readProtectedKey() throws DecryptorException {
		String keyEncoded = readKeyFromServerIdentity(config.getKeyLocation());
		
		byte[] keyDecoded = decode(keyEncoded);
		
		return keyDecoded;
	}
}
