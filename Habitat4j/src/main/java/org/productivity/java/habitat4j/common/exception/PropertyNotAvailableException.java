package org.productivity.java.habitat4j.common.exception;

/**
 * Exception used for Property retrieval problems.
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
 * @version $Id: PropertyNotAvailableException.java,v 1.3 2005/03/26 07:52:08 cvs Exp $
 */
public class PropertyNotAvailableException extends Exception {
	private static final long serialVersionUID = 3688508813644674101L;

	/**
	 * 
	 */
	public PropertyNotAvailableException() {
		super();
	}

	/**
	 * @param message - a text message describing the exception
	 */
	public PropertyNotAvailableException(String message) {
		super(message);
	}

	/**
	 * @param message - a text message describing the exception
	 * @param level - the Habitat4JLogger level
	 */
	public PropertyNotAvailableException(String message,byte level) {
		super(message + " level: " + level);
	}

	/**
	 * @param throwable - an object of type Throwable
	 */
	public PropertyNotAvailableException(Throwable throwable) {
		// super(throwable); -- J2SE 1.4+
		super(throwable.toString());
	}
}
