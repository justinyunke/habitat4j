package org.productivity.java.habitat4j.common.interfaces;

import org.productivity.java.habitat4j.common.exception.ApplicationPropertyListLoaderException;

/**
 * This interface is used by the Habitat4JBootstrapServlet to
 * provide a way for an Application to load its properties via a custom
 * class.
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
 * @version $Id: ApplicationPropertyListLoaderIF.java,v 1.1 2004/11/10 05:25:06 cvs Exp $
 */
public interface ApplicationPropertyListLoaderIF {
	public void load(String applicationName) throws ApplicationPropertyListLoaderException;
}
