package org.productivity.java.habitat4j.web.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.productivity.java.habitat4j.Habitat4JBootstrap;
import org.productivity.java.habitat4j.common.exception.Habitat4JBootstrapException;

/**
 * Habitat4JBootstrapServletContextListener provides a ServletContextListener implementation for the use in
 * initializing, or "bootstrapping," Habitat4J.
 * 
 * <p>This class primarily wraps the Habitat4JBootstrap class.</p>
 * 
 * <p>For the <b>listener</b> specification in the Serlvet Web Descriptor (web.xml), use the following <b>listener-class</b>:</p>
 * 
 * <ul>
 * <li>org.productivity.java.habitat4j.web.servlet.Habitat4JBootstrapServletContextListener</li>
 * </ul>
 * 
 * <p>For the <b>context-param</b> configuration in the Web Descriptor (web.xml), use the following parameters:</p>
 * 
 * <ul>
 * <li>applicationName - the name of your application</li>
 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
 * <li>logLevel - a String containing one of the following log levels: debug, info, warn, error, or fatal mainly used for setting Habitat4J's native logging level (non-log4j)</li>
 * <li>serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths pointing to a server-identity.xml file or files</li>
 * <li>serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
 * <li>appPropertyListLoaderClassName - a String containing the fully qualified class for the customized implementation of ApplicationPropertyListLoaderIF</li>
 * </ul>
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
 * @version $Id: Habitat4JBootstrapServletContextListener.java,v 1.2 2006/05/27 21:47:29 cvs Exp $
 */
public class Habitat4JBootstrapServletContextListener implements ServletContextListener  {
	/**
	 * @param context - the ServletContext instance
	 * @return Returns all init-param name/value pairs in the form of a Map (HashMap).
	 */
	protected static Map getInitParametersAsMap(ServletContext context) {
		Enumeration enumeration = context.getInitParameterNames();
		HashMap map = new HashMap();
		
		while (enumeration.hasMoreElements()) {
			String initParamName = (String) enumeration.nextElement();
			String initParamValue = context.getInitParameter(initParamName);
			
			if (initParamValue != null) {
				map.put(initParamName,initParamValue);
			}
		}
		
		return map;
	}

	/**
	 * doBootstrapInitializeServerIdentity() is used when subclassing Habitat4JBootstrapServlet
	 * and overriding the init() method.  It allows one to initialize the ServerIdentity as
	 * a separate step in order to do things like tweak pragma values before doBootstrapInitializePropertyLists()
	 * method is called.
	 * 
	 * @param context - the ServletContext instance
	 * @throws RuntimeException
	 */
	protected void doBootstrapInitializeServerIdentity(ServletContext context) throws RuntimeException {
		Map initParams = getInitParametersAsMap(context);

		try {
			Habitat4JBootstrap.initializeServerIdentity(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new RuntimeException(hbe.toString());
		}		
	}

	/**
	 * doBootstrapInitializePropertyLists() is used when subclassing Habitat4JBootstrapServlet
	 * and overriding the init() method.  doBootstrapInitializeServerIdentity() should be
	 * called before this method.
	 * 
	 * @param context - the ServletContext instance
	 * @throws RuntimeException
	 */
	protected void doBootstrapInitializePropertyLists(ServletContext context) throws RuntimeException {
		Map initParams = getInitParametersAsMap(context);

		try {
			Habitat4JBootstrap.initializePropertyLists(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new RuntimeException(hbe.toString());
		}		
	}
	
	/**
	 * doBootstrapInitialize() is used by the default init() method to setup Habitat4J in one step.
	 * 
	 * @param context - the ServletContext instance
	 * @throws RuntimeException
	 */
	protected void doBootstrapInitialize(ServletContext context) throws RuntimeException {
		Map initParams = getInitParametersAsMap(context);

		try {
			Habitat4JBootstrap.initialize(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new RuntimeException(hbe.toString());
		}		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
		// Nothing to do for contextDestroyed
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		
		if (context != null) {
			doBootstrapInitialize(context);
		}
	}
}
