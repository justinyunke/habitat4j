package org.productivity.java.habitat4j.web.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.productivity.java.habitat4j.Habitat4JBootstrap;
import org.productivity.java.habitat4j.common.exception.Habitat4JBootstrapException;

/**
 * Habitat4JBootstrapServlet provides an HttpServlet implementation for the use in
 * initializing, or "bootstrapping," Habitat4J.
 * 
 * <p>This class primarily wraps the Habitat4JBootstrap class.</p>
 * 
 * <p>For the <b>servlet-class</b> specification in the Serlvet Web Descriptor (web.xml):</p>
 * 
 * <ul>
 * <li>org.productivity.java.habitat4j.web.servlet.Habitat4JBootstrapServlet</li>
 * </ul>
 * 
 * <p>For the <p>init-param</p> specification in the Servlet Web Descriptor (web.xml):</p>
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
 * @version $Id: Habitat4JBootstrapServlet.java,v 1.1 2008/08/15 01:34:23 cvs Exp $
 */
public class Habitat4JBootstrapServlet extends HttpServlet {
	private static final long serialVersionUID = 4051324539675947826L;

	/**
	 * @return Returns all init-param name/value pairs in the form of a Map (HashMap).
	 */
	protected Map getInitParametersAsMap() {
		Enumeration enumeration = getInitParameterNames();
		HashMap map = new HashMap();
		
		while (enumeration.hasMoreElements()) {
			String initParamName = (String) enumeration.nextElement();
			String initParamValue = getInitParameter(initParamName);
			
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
	 * @throws ServletException
	 */
	protected void doBootstrapInitializeServerIdentity() throws ServletException {
		Map initParams = getInitParametersAsMap();

		try {
			Habitat4JBootstrap.initializeServerIdentity(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new ServletException(hbe);
		}		
	}

	/**
	 * doBootstrapInitializePropertyLists() is used when subclassing Habitat4JBootstrapServlet
	 * and overriding the init() method.  doBootstrapInitializeServerIdentity() should be
	 * called before this method.
	 * 
	 * @throws ServletException
	 */
	protected void doBootstrapInitializePropertyLists() throws ServletException {
		Map initParams = getInitParametersAsMap();

		try {
			Habitat4JBootstrap.initializePropertyLists(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new ServletException(hbe);
		}		
	}
	
	/**
	 * doBootstrapInitialize() is used by the default init() method to setup Habitat4J in one step.
	 * 
	 * @throws ServletException
	 */
	protected void doBootstrapInitialize() throws ServletException {
		Map initParams = getInitParametersAsMap();

		try {
			Habitat4JBootstrap.initialize(initParams);
			
		} catch (Habitat4JBootstrapException hbe) {
			throw new ServletException(hbe);
		}		
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException {
		super.init();
		
		doBootstrapInitialize();
	}
}
