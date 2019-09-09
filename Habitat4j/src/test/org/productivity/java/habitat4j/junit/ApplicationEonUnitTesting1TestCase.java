package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF;

/**
 * This class consists of JUnit tests for deploying Application E on Unit Testing Server #1
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
 * @version $Id: ApplicationEonUnitTesting1TestCase.java,v 1.7 2007/02/25 18:52:04 cvs Exp $
 */
public class ApplicationEonUnitTesting1TestCase extends ApplicationEBaseTestCase implements Habitat4JConstants {
	private static final String SERVER_NAME					= "servers/unit/server-1/";
	
	public static final String[] TEST_LIST = {
		"testReloadOnUnitServer1"
	};

	private class ApplicationEReloadHandler implements ReloadEventHandlerIF {
		public void onReloadFailure(PropertyList oldPropertyList, PropertyListHandlerException exception) {
			//
		}

		public boolean postReload(PropertyList oldPropertyList, PropertyList newPropertyList) {
			//
			return true;
		}

		public boolean preReload(PropertyList oldPropertyList) {
			//
			return true;
		}
	};
	
	private ReloadEventHandlerIF reloadHandler = new ApplicationEReloadHandler();
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(SERVER_NAME,APPLICATION_NAME);
		
		try {
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD,true);
			
		} catch (PropertyListHandlerException plhe) {
			fail(plhe.toString());
		}
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		
		switch(currentVersion) {
			case 1: renamePropertyListToPropertyList1(); break;
			case 2: renamePropertyListToPropertyList2(); break;
			case 3: renamePropertyListToPropertyList3(); break;
		}
	}
	
	private void _testServerIdentity() {
		try {
			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			
			assertEquals("Unit",ServerIdentityManager.getEnvironment());
			assertEquals("server-unit-1",ServerIdentityManager.getInstanceName());
			assertEquals("Unit Testing Server #1",ServerIdentityManager.getInstanceDescription());
			assertEquals("1",ServerIdentityManager.getInstanceEnumeration());
			
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
			assertEquals("19",ServerIdentityManager.getRole("dbServer").getPatchLevel());

			assertEquals(0,ServerIdentityManager.getPragmaDefinitions().size());
			assertNull(ServerIdentityManager.getPragmaDefinition("bogusPragmaDefinition"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
		
		logger.info(ServerIdentityManager.toDisplayString());
	}

	public void testReloadOnUnitServer1() {
		try {
			renamePropertyList1ToPropertyList();
			logger.info("Step 1 - Read original property-list.xml.");			
			doLoadFromFile();
			_testServerIdentity();
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1");
			
			logger.info("Step 2 - Place a new version of property-list.xml.");			
			renamePropertyListToPropertyList1();
			renamePropertyList2ToPropertyList();

			logger.info("Step 3 - Pause for 1 second.");			
			pause(1);

			logger.info("Step 4 - Verify that old property value still exists (reload period not passed).");			
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1");

			logger.info("Step 5 - Execute reload.");
			PropertyListManager.reloadOnFileChange("reload");

			logger.info("Step 6 - Verify that new property value exists (reload period passed).");			
			assertNull(PropertyListManager.getProperty("reload","reloadName"));

			logger.info("Step 7 - Place old property file version back.");			
			renamePropertyListToPropertyList2();
			renamePropertyList1ToPropertyList();

			logger.info("Step 8 - Execute reload.");
			PropertyListManager.reloadOnFileChange("reload");

			logger.info("Step 9 - Verify that old property value is now back.");			
			assertEquals(PropertyListManager.getProperty("reload","reloadName"),"reloadValue-1");

			logger.info("Step 10 - Done.");			

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}	
	}
	
	public void testWithoutReloadInhibitor() {
		renamePropertyList1ToPropertyList();
		
		try {
			doLoadFromFile();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}
		
		String value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		String value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertEquals("reloadInhibitorValue2",value2);
		
		renamePropertyListToPropertyList1();
		renamePropertyList2ToPropertyList();
		
		value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		PropertyListManager.reloadOnFileChange("reload");
		
		value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertNull(value2);
	}

	public void testWithReloadInhibitor() {
		renamePropertyList1ToPropertyList();
		
		try {
			doLoadFromFile();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}
		
		String value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		String value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertEquals("reloadInhibitorValue2",value2);
		
		renamePropertyListToPropertyList1();
		renamePropertyList2ToPropertyList();
		
		PropertyListManager.inhibitReload();
		
		value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		PropertyListManager.reloadOnFileChange("reload");

		value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertEquals("reloadInhibitorValue2",value2);
		
		PropertyListManager.uninhibitReload();
	}

	public void testWithExpiringReloadInhibitor() {
		renamePropertyList1ToPropertyList();
		
		try {
			doLoadFromFile();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}
		
		String value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		String value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertEquals("reloadInhibitorValue2",value2);
		
		renamePropertyListToPropertyList1();
		renamePropertyList2ToPropertyList();
		
		PropertyListManager.inhibitReload(200);
		
		value1 = PropertyListManager.getProperty("reload","reloadInhibitorName1");
		assertEquals("reloadInhibitorValue1",value1);

		PropertyListManager.reloadOnFileChange("reload");

		try {
			Thread.sleep(100);
			
		} catch (InterruptedException ie) {
			//
		}

		PropertyListManager.reloadOnFileChange("reload");
		
		value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertEquals("reloadInhibitorValue2",value2);

		try {
			Thread.sleep(101);
			
		} catch (InterruptedException ie) {
			//
		}

		PropertyListManager.reloadOnFileChange("reload");
		
		value2 = PropertyListManager.getProperty("reload","reloadInhibitorName2");
		assertNull(value2);
	}
	
	public void testReloadTransfer() {
		renamePropertyList1ToPropertyList();
		
		try {
			doLoadFromFile();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}
		
		renamePropertyListToPropertyList1();
		renamePropertyList3ToPropertyList();
		
		PropertyList listBefore = PropertyListManager.getPropertyList("reload");
		listBefore.setReloadEventHandler(reloadHandler);

		PropertyListManager.reloadOnFileChange("reload");
		
		PropertyList listAfter = PropertyListManager.getPropertyList("reload");

		Object reloadHandlerBefore = listBefore.getReloadEventHandler();
		Object reloadHandlerAfter = listAfter.getReloadEventHandler();
		
		assertTrue(reloadHandlerBefore.equals(reloadHandlerAfter));
	}

	public void testReloadTransferDisabled() {
		renamePropertyList1ToPropertyList();
		
		try {
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_DISABLE_HANDLER_TRANSFER, "true");
			
			doLoadFromFile();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}
		
		renamePropertyListToPropertyList1();
		renamePropertyList3ToPropertyList();
		
		PropertyList listBefore = PropertyListManager.getPropertyList("reload");
		listBefore.setReloadEventHandler(reloadHandler);

		PropertyListManager.reloadOnFileChange("reload");
		
		PropertyList listAfter = PropertyListManager.getPropertyList("reload");

		Object reloadHandlerBefore = listBefore.getReloadEventHandler();
		
		assertTrue(reloadHandlerBefore != null);

		Object reloadHandlerAfter = listAfter.getReloadEventHandler();
		
		assertTrue(reloadHandlerAfter == null);
	}

	public ApplicationEonUnitTesting1TestCase(String arg0) {
		super(arg0);
	}
}
