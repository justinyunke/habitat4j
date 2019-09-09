package org.productivity.java.habitat4j.common.exception;

import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JLogger;

/**
 * Exception used for Loader runtime problems.
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
 * @version $Id: ApplicationPropertyListLoaderException.java,v 1.2 2005/03/23 07:55:45 cvs Exp $
 */
public class ApplicationPropertyListLoaderException extends Exception implements Habitat4JConstants {
	private static final long serialVersionUID = 3833461795221418035L;
	
	protected Habitat4JLogger logger = null;

	/**
	 * 
	 */
	public ApplicationPropertyListLoaderException() {
		super();
		logger = Habitat4JLogger.getInstance();
	}

	/**
	 * @param message - a text message describing the exception
	 */
	public ApplicationPropertyListLoaderException(String message) {
		super(message);
		logger = Habitat4JLogger.getInstance();
		logger.error(message);
	}

	/**
	 * @param message - a text message describing the exception
	 * @param level - the Habitat4JLogger level
	 */
	public ApplicationPropertyListLoaderException(String message,byte level) {
		super(message);
		logger = Habitat4JLogger.getInstance();
		logger.log(level,message);
	}

	/**
	 * @param throwable - an object of type Throwable
	 */
	public ApplicationPropertyListLoaderException(Throwable throwable) {
		// super(throwable); -- J2SE 1.4+
		super(throwable.toString());
	}
}
