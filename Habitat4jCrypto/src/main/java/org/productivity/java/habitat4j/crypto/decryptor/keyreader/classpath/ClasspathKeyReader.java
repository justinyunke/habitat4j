package org.productivity.java.habitat4j.crypto.decryptor.keyreader.classpath;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.keyreader.KeyReader;

public class ClasspathKeyReader extends KeyReader {
	protected final static int BYTE_BUFFER_SIZE = 1024;

	protected static String CHAR_SET = "UTF-8";
	
	protected byte[] readInputStream(InputStream inputStream) throws DecryptorException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			byte[] buffer = new byte[BYTE_BUFFER_SIZE];
			int len = inputStream.read(buffer);
			while (len > -1) {
				baos.write(buffer,0,len);
				
				len = inputStream.read(buffer);
			}
			
		} catch (IOException ioe) {
			throw new DecryptorException(ioe);
		}
		
		byte[] outBytes = baos.toByteArray();
		
		return outBytes;
	}

	protected String readKeyFromClasspath(String keyLocation) throws DecryptorException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(keyLocation);
		
		if (inputStream == null) {
			throw new DecryptorException("No key found at \"" + keyLocation + "\"");
		}
		
		byte[] outBytes = readInputStream(inputStream);
		
		String out = createString(outBytes,CHAR_SET);
		
		return out;
	}

	public Serializable readProtectedKey() throws DecryptorException {
		String keyEncoded = readKeyFromClasspath(config.getKeyLocation());
		
		byte[] keyDecoded = decode(keyEncoded);
		
		return keyDecoded;
	}
}
