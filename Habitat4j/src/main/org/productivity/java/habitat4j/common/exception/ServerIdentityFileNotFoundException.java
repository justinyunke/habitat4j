package org.productivity.java.habitat4j.common.exception;


/**
 * Exception used for a missing ServerIdentity XML file.
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
 * @version $Id: ServerIdentityFileNotFoundException.java,v 1.2 2005/03/23 07:55:45 cvs Exp $
 */
public class ServerIdentityFileNotFoundException extends ServerIdentityHandlerException {
	private static final long serialVersionUID = 3834869191646721077L;

	/**
	 * 
	 */
	public ServerIdentityFileNotFoundException() {
		super();
	}

	/**
	 * @param message - a text message describing the exception
	 */
	public ServerIdentityFileNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message - a text message describing the exception
	 * @param level - the Habitat4JLogger level
	 */
	public ServerIdentityFileNotFoundException(String message,byte level) {
		super(message,level);
	}

	/**
	 * @param throwable - an object of type Throwable
	 */
	public ServerIdentityFileNotFoundException(Throwable throwable) {
		super(throwable);
	}
}
