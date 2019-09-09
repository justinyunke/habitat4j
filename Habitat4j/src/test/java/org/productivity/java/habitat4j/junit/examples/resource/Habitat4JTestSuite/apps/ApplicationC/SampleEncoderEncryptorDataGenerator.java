/*
 * Created on Jun 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.productivity.java.habitat4j.junit.examples.resource.Habitat4JTestSuite.apps.ApplicationC;

import org.productivity.java.habitat4j.junit.examples.decoder.SampleBase64EncoderDecoder;
import org.productivity.java.habitat4j.junit.examples.decryptor.SampleCaesarEncryptorDecryptor;

/**
 * This class generates sample encoded/encrypted values for the JUnit TestCase classes.
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
 * @version $Id: SampleEncoderEncryptorDataGenerator.java,v 1.2 2004/07/05 18:52:05 cvs Exp $
 */
public class SampleEncoderEncryptorDataGenerator {
	public static void generatePropertyData() {
		System.out.println("encoded.1=" + SampleBase64EncoderDecoder.doBase64("encoded.value.1",true));
		
		System.out.println("encrypted.2=" + SampleCaesarEncryptorDecryptor.doCaesar("encrypted.value.2",true));
		
		String encoded = SampleBase64EncoderDecoder.doBase64("encoded.encrypted.value.3",true);
		System.out.println("encoded.encrypted.3=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));
		
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("encrypted.encoded.value.4",true);
		System.out.println("encrypted.encoded.4=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println();
	}

	public static void generateSystemPropertyData() {
		System.out.println("system.encoded.1=" + SampleBase64EncoderDecoder.doBase64("system.encoded.value.1",true));
		
		System.out.println("system.encrypted.2=" + SampleCaesarEncryptorDecryptor.doCaesar("system.encrypted.value.2",true));
		
		String encoded = SampleBase64EncoderDecoder.doBase64("system.encoded.encrypted.value.3",true);
		System.out.println("system.encoded.encrypted.3=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));
		
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("system.encrypted.encoded.value.4",true);
		System.out.println("system.encrypted.encoded.4=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println();
	}

	public static void generateSecurityPropertyData() {
		System.out.println("security.encoded.1=" + SampleBase64EncoderDecoder.doBase64("security.encoded.value.1",true));
		
		System.out.println("security.encrypted.2=" + SampleCaesarEncryptorDecryptor.doCaesar("security.encrypted.value.2",true));
		
		String encoded = SampleBase64EncoderDecoder.doBase64("security.encoded.encrypted.value.3",true);
		System.out.println("security.encoded.encrypted.3=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));
		
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("security.encrypted.encoded.value.4",true);
		System.out.println("security.encrypted.encoded.4=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println();
	}

	public static void generatePropertyDataPackage() {
		System.out.println("encoded.5=" + SampleBase64EncoderDecoder.doBase64("encoded.value.5",true));

		System.out.println("encrypted.6=" + SampleCaesarEncryptorDecryptor.doCaesar("encrypted.value.6",true));
	
		String encoded = SampleBase64EncoderDecoder.doBase64("encoded.encrypted.value.7",true);
		System.out.println("encoded.encrypted.7=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));

		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("encrypted.encoded.value.8",true);
		System.out.println("encrypted.encoded.8=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));
		
		System.out.println();
	}

	public static void generatePropertyArrayData() {
		System.out.println("encoded.array.1.1=" + SampleBase64EncoderDecoder.doBase64("encoded.value.1.1",true));
		
		System.out.println("encrypted.array.1.2=" + SampleCaesarEncryptorDecryptor.doCaesar("encrypted.value.1.2",true));

		String encoded = SampleBase64EncoderDecoder.doBase64("encoded.encrypted.value.1.3",true);
		System.out.println("encoded.encrypted.array.1.3=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));		

		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("encrypted.encoded.value.1.4",true);
		System.out.println("encrypted.encoded.array.1.4=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));
		
		System.out.println();
	}

	public static void generatePropertyArrayDataPackage() {
		System.out.println("encoded.array.2.1=" + SampleBase64EncoderDecoder.doBase64("encoded.value.2.1",true));
		
		System.out.println("encrypted.array.2.2=" + SampleCaesarEncryptorDecryptor.doCaesar("encrypted.value.2.2",true));
		
		String encoded = SampleBase64EncoderDecoder.doBase64("encoded.encrypted.value.2.3",true);
		System.out.println("encoded.encrypted.array.2.3=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));		

		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("encoded.encrypted.value.2.4",true);
		System.out.println("encrypted.encoded.array.2.4=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));	
		
		System.out.println();
	}

	public static void generatePropertyBeanData() {
		System.out.println("127.0.0.1=" + SampleBase64EncoderDecoder.doBase64("127.0.0.1",true));
	
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("LDAP (Directory) Server",true);
		System.out.println("LDAP (Directory) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("389=" + SampleCaesarEncryptorDecryptor.doCaesar("389",true));
	
		String encoded = SampleBase64EncoderDecoder.doBase64("ldap",true);
		System.out.println("ldap=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));		
		
		System.out.println();
	}

	public static void generatePropertyBeanDataPackage() {
		System.out.println("127.0.0.1=" + SampleBase64EncoderDecoder.doBase64("127.0.0.1",true));
	
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("LDAPS (Directory) Server",true);
		System.out.println("LDAPS (Directory) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("646=" + SampleCaesarEncryptorDecryptor.doCaesar("646",true));
	
		String encoded = SampleBase64EncoderDecoder.doBase64("ldaps",true);
		System.out.println("ldaps=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));		
		
		System.out.println();
	}

	public static void generatePropertyBeanArrayData() {
		System.out.println("10.0.0.1=" + SampleBase64EncoderDecoder.doBase64("10.0.0.1",true));
	
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("SMTP (Mail) Server",true);
		System.out.println("SMTP (Mail) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("25=" + SampleCaesarEncryptorDecryptor.doCaesar("25",true));
	
		String encoded = SampleBase64EncoderDecoder.doBase64("smtp",true);
		System.out.println("smtp=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));

		System.out.println();

		System.out.println("10.0.0.2=" + SampleBase64EncoderDecoder.doBase64("10.0.0.2",true));
	
		encrypted = SampleCaesarEncryptorDecryptor.doCaesar("SMTPS (Mail) Server",true);
		System.out.println("SMTPS (Mail) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("465=" + SampleCaesarEncryptorDecryptor.doCaesar("465",true));
		
		encoded = SampleBase64EncoderDecoder.doBase64("smtps",true);
		System.out.println("smtps=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));
		
		System.out.println();
	}

	public static void generatePropertyBeanArrayDataPackage() {
		System.out.println("10.1.0.1=" + SampleBase64EncoderDecoder.doBase64("10.1.0.1",true));
	
		String encrypted = SampleCaesarEncryptorDecryptor.doCaesar("SMTP (Mail) Server",true);
		System.out.println("SMTP (Mail) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("25=" + SampleCaesarEncryptorDecryptor.doCaesar("25",true));

		String encoded = SampleBase64EncoderDecoder.doBase64("smtp",true);
		System.out.println("smtp=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));

		System.out.println();

		System.out.println("10.1.0.2=" + SampleBase64EncoderDecoder.doBase64("10.1.0.2",true));

		encrypted = SampleCaesarEncryptorDecryptor.doCaesar("SMTPS (Mail) Server",true);
		System.out.println("SMTPS (Mail) Server=" + SampleBase64EncoderDecoder.doBase64(encrypted,true));		

		System.out.println("465=" + SampleCaesarEncryptorDecryptor.doCaesar("465",true));
		
		encoded = SampleBase64EncoderDecoder.doBase64("smtps",true);
		System.out.println("smtps=" + SampleCaesarEncryptorDecryptor.doCaesar(encoded,true));
		
		System.out.println();
	}

	public static void main(String[] args) {
		generatePropertyData();
		generateSystemPropertyData();
		generateSecurityPropertyData();
		generatePropertyDataPackage();
		generatePropertyArrayData();
		generatePropertyArrayDataPackage();
		generatePropertyBeanData();
		generatePropertyBeanArrayData();
		generatePropertyBeanDataPackage();
		generatePropertyBeanArrayDataPackage();
	}
}
