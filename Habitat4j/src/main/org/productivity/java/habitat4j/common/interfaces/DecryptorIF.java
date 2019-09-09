package org.productivity.java.habitat4j.common.interfaces;

import org.productivity.java.habitat4j.common.exception.DecryptorException;

/**
 * Interface used to implement a property value decryptor.
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
 * @version $Id: DecryptorIF.java,v 1.4 2005/03/30 04:04:34 cvs Exp $
 */
public interface DecryptorIF {
	/**
	 * @param appName - the name of the calling application (use is optional)
	 * @param propertyListName - the name of the PropertyList that's calling this method (use is optional)
	 * @throws DecryptorException
	 */
	public void initialize(String appName, String propertyListName) throws DecryptorException;
	
	/**
	 * @param propertyListName - the name of the PropertyList that's calling this method (use is optional)
	 * @param name - the Property name (use is optional)
	 * @param value - the String value to decrypt
	 * @return Returns a decrypted String from parameter "value"
	 * @throws DecryptorException
	 */
	public String decrypt(String propertyListName, String name, String value) throws DecryptorException;
}
