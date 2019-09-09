package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;

/**
 * This class consists of JUnit tests for deploying Application E on Unit Testing Server #2
 * with Habitat4J.
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
 * @version $Id: ApplicationEonUnitTesting2TestCase.java,v 1.5 2004/08/30 00:27:28 cvs Exp $
 */
public class ApplicationEonUnitTesting2TestCase extends ApplicationEBaseTestCase implements Habitat4JConstants {
	private static final String SERVER_NAME					= "servers/unit/server-2/";

	public static final String[] TEST_LIST = {
		"testReloadOnUnitServer2"
	};
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(SERVER_NAME,APPLICATION_NAME);
		
		try {
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD,true);
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL,"1");
			
		} catch (PropertyListHandlerException plhe) {
			fail(plhe.toString());
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		
		switch(currentVersion) {
			case 1: renamePropertyListToPropertyList1(); break;
			case 2: renamePropertyListToPropertyList2(); break;
		}
	}
	
	private void _testServerIdentity() {
		try {
			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			
			assertEquals("Dev",ServerIdentityManager.getEnvironment());
			assertEquals("server-unit-2",ServerIdentityManager.getInstanceName());
			assertEquals("Unit Testing Server #2",ServerIdentityManager.getInstanceDescription());
			assertEquals("2",ServerIdentityManager.getInstanceEnumeration());
			
			assertEquals(2,ServerIdentityManager.getRoles().size());
			
			assertNotNull(ServerIdentityManager.getRole("appServer"));		
			assertEquals("J2EE",ServerIdentityManager.getRole("appServer").getType());
			assertEquals("JBOSS",ServerIdentityManager.getRole("appServer").getVendor());
			assertEquals("JBOSS J2EE Server",ServerIdentityManager.getRole("appServer").getProduct());
			assertEquals("3.2",ServerIdentityManager.getRole("appServer").getVersion());
			assertEquals("3",ServerIdentityManager.getRole("appServer").getPatchLevel());

			assertNotNull(ServerIdentityManager.getRole("dbServer"));		
			assertEquals("MySQL",ServerIdentityManager.getRole("dbServer").getType());
			assertEquals("MySQL AB",ServerIdentityManager.getRole("dbServer").getVendor());
			assertEquals("MySQL SQL Server",ServerIdentityManager.getRole("dbServer").getProduct());
			assertEquals("4.0",ServerIdentityManager.getRole("dbServer").getVersion());
			assertEquals("20",ServerIdentityManager.getRole("dbServer").getPatchLevel());

			assertEquals(0,ServerIdentityManager.getPragmaDefinitions().size());
			assertNull(ServerIdentityManager.getPragmaDefinition("bogusPragmaDefinition"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
		
		logger.info(ServerIdentityManager.toDisplayString());
	}

	public void testReloadOnUnitServer2() {
		try {
			renamePropertyList1ToPropertyList();
			logger.info("Step 1 - Read original property-list.xml.");			
			doLoadFromFile();
			_testServerIdentity();
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1b");
			
			logger.info("Step 2 - Pause for 30 seconds.");			
			pause(30);
			
			logger.info("Step 3 - Place a new version of property-list.xml.");			
			renamePropertyListToPropertyList1();
			renamePropertyList2ToPropertyList();

			logger.info("Step 4 - Pause for 15 seconds.");			
			pause(15);

			logger.info("Step 5 - Verify that old property value still exists (reload period not passed).");			
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1b");
			
			logger.info("Step 6 - Pause for 16 seconds.");			
			pause(16);

			logger.info("Step 7 - Verify that new property value exists (reload period passed).");
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-2");

			logger.info("Step 8 - Place old property file version back.");			
			renamePropertyListToPropertyList2();
			renamePropertyList1ToPropertyList();

			logger.info("Step 9 - Pause for 61 seconds.");			
			pause(61);

			logger.info("Step 10 - Verify that old property value is now back.");			
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1b");

			logger.info("Step 11 - Done.");			

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}	
	}

	public ApplicationEonUnitTesting2TestCase(String arg0) {
		super(arg0);
	}
}
