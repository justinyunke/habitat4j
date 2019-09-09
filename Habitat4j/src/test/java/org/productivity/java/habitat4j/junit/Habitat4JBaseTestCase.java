package org.productivity.java.habitat4j.junit;

import junit.framework.TestCase;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.Habitat4JLogger;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.compat.ServerIdentityXMLMap;

/**
 * Base Test Case for Habitat4J.
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
 * @version $Id: Habitat4JBaseTestCase.java,v 1.7 2004/10/03 21:10:26 cvs Exp $
 */
public abstract class Habitat4JBaseTestCase extends TestCase implements Habitat4JConstants {
	protected static final String DATA_EXAMPLES_FILE_PATH		= "doc/Habitat4J/examples/junit/Habitat4JTestSuite/";
	protected static final String DATA_EXAMPLES_RESOURCE_PATH	= "org/productivity/java/habitat4j/junit/examples/resource/Habitat4JTestSuite/";
	protected String SERVER_IDENTITY_FILENAME = "server-identity.xml";
	
	protected Habitat4JLogger logger = null;
	
	/**
	 * 
	 */
	public Habitat4JBaseTestCase() {
		super();
	}
	
	protected abstract void doLoadFromResource() throws PropertyListHandlerException;

	protected void setUp(String serverName, String applicationName, String mode, boolean xsdValidation, ServerIdentityXMLMap map) throws Exception {
		super.setUp();
		
		System.setProperty(HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME,DATA_EXAMPLES_FILE_PATH + serverName + SERVER_IDENTITY_FILENAME);

		// NOTE: This is only needed for the JUnit tests, since we need to start with a clean slate to start the tests.
		PropertyListManager.reset();
		
		// The following constitutes the "Bootstrap code" for Habitat4J
		// NOTE: If you want to use the default mode of SERVER_IDENTITY_MODE_FILE, just
		//       call PropertyListManager.initialize(applicationName)
		if (map == null) {
			PropertyListManager.initialize(applicationName,mode);
		} else {
			PropertyListManager.initialize(applicationName,mode,map);
		}
		
		if (xsdValidation) {
			// The following code is optional if you wish to set features.  We're going to
			// be strict for the purposes of the JUnit tests and force XSD validation.
			try {
				PropertyListManager.setFeature(Habitat4JFeatures.SERVER_IDENTITY_FEATURE_XSD_VALIDATION,"true");
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_XSD_VALIDATION,"true");
				
			} catch (PropertyListHandlerException plhe) {
				fail("The setUp() method had a problem setting up the Habitat4J features.");
			}
		}
	}

	protected void setUp(String serverName, String applicationName, String mode) throws Exception {
		setUp(serverName,applicationName,mode,true,null);
	}

	protected void setUp(String serverName, String applicationName, boolean xsdValidation) throws Exception {
		setUp(serverName,applicationName,SERVER_IDENTITY_MODE_FILE,xsdValidation,null);
	}

	protected void setUp(String serverName, String applicationName) throws Exception {
		setUp(serverName,applicationName,SERVER_IDENTITY_MODE_FILE,true,null);
	}

	/**
	 * @param arg0
	 */
	public Habitat4JBaseTestCase(String arg0) {
		super(arg0);
		
		logger = Habitat4JLogger.getInstance();
	}
}
