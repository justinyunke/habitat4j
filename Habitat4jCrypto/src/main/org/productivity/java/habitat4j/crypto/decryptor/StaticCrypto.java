package org.productivity.java.habitat4j.crypto.decryptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.productivity.java.habitat4j.common.exception.DecryptorException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class StaticCrypto {
	public static byte[] getBytes(String in, String charSet) throws DecryptorException {
		byte[] out = null;
		
		try {
			out = in.getBytes(charSet);
			
		} catch (UnsupportedEncodingException uee) {
			throw new DecryptorException(uee);
		}
		
		return out;
	}

	public static String createString(byte[] in, String charSet) throws DecryptorException {
		String out = null;
		
		try {
			out = new String(in,charSet);
			
		} catch (UnsupportedEncodingException uee) {
			throw new DecryptorException(uee);
		}
		
		return out;
	}

	public static byte[] pad(byte[] data, int blockSize) {
		int len = data.length;
		int mod = len % blockSize;

		if (mod == 0) {
			return data;
		}
		
		int padCount = blockSize - mod;
		
		int newLen = len + padCount;
		
		byte[] newData = new byte[newLen];
		System.arraycopy(data, 0, newData, 0, len);
		
		for(int i=len; i<newLen; i++) {
			newData[i] = 0;
		}
		
		return newData;
	}
	
	public static int findPadEndIndex(byte[] data, int blockSize) {
		int s = data.length-1;
		int e = s - (blockSize - 1);
		
		for(int i=s; i>=e; i--) {
			if (data[i] != 0) {
				e = i + 1;
				
				break;
			}
		}
		
		return e;
	}
	
	public static byte[] removePad(byte[] in, int blockSize) {
		int len = findPadEndIndex(in,blockSize);
		
		byte[] out = new byte[len];
		
		System.arraycopy(in,0,out,0,len);
		
		return out;
	}

	public static String encode(byte[] in) {
		BASE64Encoder e = new BASE64Encoder();
		
		String out = e.encode(in);

		out = out.replaceAll("\n", "");
		out = out.replaceAll("\r", "");

		return out;
	}

	public static byte[] decode(String data) throws DecryptorException {
		BASE64Decoder d = new BASE64Decoder();
		
		byte[] out = null;
		
		try {
			out = d.decodeBuffer(data);
			
		} catch (IOException ioe) {
			throw new DecryptorException(ioe);
		}
			
		return out;
	}
}
