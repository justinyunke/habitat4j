package org.productivity.java.habitat4j.junit.examples.decoder;
import java.io.IOException;

import org.productivity.java.habitat4j.common.interfaces.DecoderIF;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This sample BASE64 encoder/decoder class implements a simple
 * interface to the sun.misc.BASE64Decoder & sun.misc.BASE64Encoder.
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
 * @version $Id: SampleBase64EncoderDecoder.java,v 1.4 2005/03/30 04:08:29 cvs Exp $
 */
public class SampleBase64EncoderDecoder implements DecoderIF {

	public void initialize(String appName, String propertyListName) {
		//
	}
	
	// NOTE: This is the only method required by the DecoderIF interface.
	public String decode(String propertyListName, String name, String value) {
		return doBase64(value,false);
	}

	public static String doBase64(String value, boolean encode) {
		if (encode) {
			byte[] byteArrayValue = new String(value).getBytes();
			BASE64Encoder base64 = new BASE64Encoder();
			
			return base64.encode(byteArrayValue);		
		}

		BASE64Decoder base64 = new BASE64Decoder();
		try {
			byte[] byteArrayValue = base64.decodeBuffer(value);
			
			StringBuffer result = new StringBuffer();
			for(int i=0; i<byteArrayValue.length; i++) {
				result.append((char) byteArrayValue[i]);
			}
			return result.toString();
			
		} catch (IOException ioe) {
			return null;
		}	
	}
}
