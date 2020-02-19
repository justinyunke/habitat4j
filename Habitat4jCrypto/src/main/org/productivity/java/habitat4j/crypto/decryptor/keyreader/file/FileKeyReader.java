package org.productivity.java.habitat4j.crypto.decryptor.keyreader.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.keyreader.KeyReader;

public class FileKeyReader extends KeyReader {
	private static final long serialVersionUID = -1525784794567750431L;
	
	protected static String CHAR_SET = "UTF-8";
	
	protected static int BUFFER_SIZE = 1024;
	
	protected String readKeyFromFile(String keyLocation) throws DecryptorException {
		if (keyLocation == null) {
			throw new DecryptorException("keyLocation cannot be null");
		}
		
		File file = new File(keyLocation);

		if (!file.exists()) {
			throw new DecryptorException("file \"" + keyLocation + "\" does not exist");
		}

		ByteArrayOutputStream baos = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer = new byte[BUFFER_SIZE];
			
			baos = new ByteArrayOutputStream();
			
			int i = fis.read(buffer);
			
			while (i > 0) {
				baos.write(buffer,0,i);
	
				i = fis.read(buffer);
			}
			
			fis.close();
			
		} catch (FileNotFoundException fnfe) {
			throw new DecryptorException(fnfe);
			
		} catch (IOException ioe) {
			throw new DecryptorException(ioe);
		}
		
		byte[] keyEncodedBytes = baos.toByteArray();
		
		String keyEncoded = createString(keyEncodedBytes,CHAR_SET);
		
		return keyEncoded;
	}

	public Serializable readProtectedKey() throws DecryptorException {
		String keyEncoded = readKeyFromFile(config.getKeyLocation());

		byte[] keyDecoded = decode(keyEncoded);
		
		return keyDecoded;
	}
}
