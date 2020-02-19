package org.productivity.java.habitat4j.crypto.decryptor.generic;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.crypto.decryptor.StaticCrypto;
import org.productivity.java.habitat4j.crypto.decryptor.key.KeyInfoIF;
import org.productivity.java.habitat4j.crypto.decryptor.key.generic.GenericKeyInfo;
import org.productivity.java.habitat4j.crypto.decryptor.keyreader.KeyReader;

public abstract class GenericCrypto extends StaticCrypto {
	private static final long serialVersionUID = -4722341793205486969L;
	
	protected GenericCryptoConfig config = null;
	protected KeyInfoIF keyInfo = null;
	
	protected void initialize(GenericCryptoConfig config) throws DecryptorException {
		this.config = config;
	}

	protected byte[] getBytes(String in) throws DecryptorException {
		byte[] out = getBytes(in,config.getDataCharSet());
		
		return out;
	}
	
	protected String createString(byte[] in) throws DecryptorException {
		String out = createString(in,config.getDataCharSet());
		
		return out;
	}

	protected static byte[] xcrypt(int mode, KeyInfoIF keyInfo, byte[] in, String keyAlgorithm, String cipherAlgorithm, boolean pad) throws DecryptorException {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(cipherAlgorithm);
			
		} catch (NoSuchAlgorithmException nsae) {
			throw new DecryptorException(nsae);
			
		} catch (NoSuchPaddingException nspe) {
			throw new DecryptorException(nspe);
		}
		
		GenericKeyInfo genericKeyInfo = (GenericKeyInfo) keyInfo;
		
		Key key = genericKeyInfo.getKeySpec();
		
		try {
			byte[] ivBytes = (byte[]) keyInfo.getIv();
			
			if (ivBytes == null) {
				cipher.init(mode,key);
				
			} else {
				AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
				
				cipher.init(mode,key,ivSpec);
			}
			
		} catch (InvalidAlgorithmParameterException iape) {
			throw new DecryptorException(iape);

		} catch (InvalidKeyException ike) {
			throw new DecryptorException(ike);
		}
		
		byte[] out = null;

		if ((mode == Cipher.ENCRYPT_MODE) && pad) {
			in = pad(in,cipher.getBlockSize());
		}

		try {
			out = cipher.doFinal(in);
			
		} catch (IllegalBlockSizeException ibse) {
			throw new DecryptorException(ibse);

		} catch (BadPaddingException bpe) {
			throw new DecryptorException(bpe);
		}
		
		if (out.length < 1) {
			throw new DecryptorException("Cipher data empty");
		}
		
		if (mode == Cipher.DECRYPT_MODE && pad) {
			out = removePad(out,cipher.getBlockSize());
		}

		return out;
	}

	protected String decrypt(byte[] value) throws DecryptorException {
		byte[] outBytes = xcrypt(Cipher.DECRYPT_MODE,keyInfo,value,config.getKeyAlgorithm(),config.getCipherAlgorithm(),config.isUseDataPadding());
		
		String out = createString(outBytes);
		
		return out;
	}

	protected KeyReader getKeyReader() throws DecryptorException {
		KeyReader keyReader = KeyReader.createKeyReader(config);
		
		return keyReader;
	}
	
	protected byte[] createHash(String value, String hashAlgorithm) throws DecryptorException {
		MessageDigest messageDigest = null;
		
		try {
			messageDigest = MessageDigest.getInstance(hashAlgorithm);
			
		} catch (NoSuchAlgorithmException nsae) {
			throw new DecryptorException(nsae); 
		}
		
		byte[] digest = messageDigest.digest(getBytes(value));
		
		return digest;
	}
}
