package org.productivity.java.habitat4j.junit.examples.decryptor;
import org.productivity.java.habitat4j.common.interfaces.DecryptorIF;

/**
 * This sample encryptor/decryptor class implements a simple Caesar cipher, where
 * the letters of the alphabet are mapped 1 character over.  For instance, to
 * "encrypt," a becomes b, b becomes c, ..., and z becomes a.  In order to 
 * "decrypt," z becomes y, y becomes x, ..., and a becomes z.
 * 
 * <p>## LICENSE INFORMATION ##</p>
 * 
 * <p>This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.</p>
 *
 * <p>This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 
 * USA</p>
 * 
 * @author Justin Yunke &lt;habitat4j@productivity.org&gt;
 * @version $Id: SampleCaesarEncryptorDecryptor.java,v 1.3 2005/03/30 04:08:41 cvs Exp $
 */
public class SampleCaesarEncryptorDecryptor implements DecryptorIF {
	public void initialize(String appName, String propertyListName) {
		//
	}
	
	// NOTE: This is the only method required by the DecryptorIF interface.
	public String decrypt(String propertyListName, String name, String value) {
		return doCaesar(value,false);
	}
	
	/**
	 * @param value - the data to encrypt/decrypt
	 * @param encrypt - whether to encrypt (true) or decrypt (false)
	 * @return Returns the result of a Caesar cipher.
	 */
	public static String doCaesar(String value, boolean encrypt) {
		byte[] byteArrayValue = value.getBytes();
		StringBuffer bufferValue = new StringBuffer(); 
		
		for (int i=0; i<byteArrayValue.length; i++) {
			byte byteValue = byteArrayValue[i];
			char charValue = (char) byteValue;
			
			if ((charValue >= 'a' && charValue <='z') || (charValue >= '0' && charValue <='9')) {
				if (encrypt) {
					if (charValue == 'z') {
						charValue = 'a';
					} else if (charValue == '9') {
						charValue = '0';
					} else {
						charValue = (char) (byteValue + 1);						
					}
					
				} else {
					if (charValue == 'a') {
						charValue = 'z';
					} else if (charValue == '0') {
						charValue = '9';
					} else {
						charValue = (char) (byteValue - 1);						
					}
					
				}
			}
			
			bufferValue.append(charValue);
		}
		
		return bufferValue.toString(); 
	}
}
