package org.productivity.java.habitat4j.junit;

import java.security.Security;
import java.util.Properties;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.PropertyHash;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;

/**
 * Application A Test Cases for Habitat4J.
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
 * @version $Id: ApplicationATestCase.java,v 1.10 2005/09/03 22:45:22 cvs Exp $
 */
public class ApplicationATestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	private static final String APPLICATION_NAME			= "ApplicationA";
	
	public static final String[] TEST_LIST = {
		"testServerIdentity",
		"testApplicationALoadPropertiesFromFile",
		"testApplicationALoadPropertiesFromResource",
		"testApplicationAPropertyListProperties",
		"testApplicationAPropertyListPropertyArray",
		"testApplicationAPropertyListSystemProperties",
		"testApplicationAPropertyListSecurityProperties"
	};	
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(null,APPLICATION_NAME,SERVER_IDENTITY_MODE_NULL);

		// Turning on the override features for System and Security properties
		PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_SYSTEM_PROPERTY_OVERRIDE,"true");
		PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_SECURITY_PROPERTY_OVERRIDE,"true");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void doLoadFromResource() throws PropertyListHandlerException {
		PropertyListManager.loadPropertyList(DATA_EXAMPLES_RESOURCE_PATH + "apps/" + APPLICATION_NAME +"/properties.xml");
		logger.info(PropertyListManager.getPropertyList().toString());
	}
	
	public void testServerIdentity() {
		try {
			doLoadFromResource();

			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			assertNull(ServerIdentityManager.getEnvironment());
			assertNull(ServerIdentityManager.getInstanceName());
			assertNull(ServerIdentityManager.getInstanceDescription());
			assertNull(ServerIdentityManager.getInstanceEnumeration());
			assertEquals(0,ServerIdentityManager.getRoles().size());
			assertNull(ServerIdentityManager.getRole("bogusRole"));
			assertEquals(0,ServerIdentityManager.getPragmaDefinitions().size());
			assertNull(ServerIdentityManager.getPragmaDefinition("bogusPragmaDefinition"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
		
		logger.info(ServerIdentityManager.toDisplayString());
	}
	
	public void testApplicationALoadPropertiesFromFile() {
		try {
			PropertyListManager.loadPropertyList(DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME + "/properties.xml");
			logger.info(EOL_SEQUENCE + PropertyListManager.getPropertyList().toString());

			assertTrue("Successfully loaded properties for ApplicationA from file.",true);

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}
	
	public void testApplicationALoadPropertiesFromResource() {
		try {
			assertTrue("Successfully loaded properties for ApplicationA from classpath resource.",true);

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationAPropertyListProperties() {
		try {
			doLoadFromResource();
			
			assertTrue("name.noprefix.1","value.1".equals(PropertyListManager.getProperty("name.noprefix.1")));
			assertTrue("name.noprefix.1.1","value.1.1".equals(PropertyListManager.getProperty("name.noprefix.1.1")));
			assertTrue("name.noprefix.1.2","value.1.2".equals(PropertyListManager.getProperty("name.noprefix.1.2")));

			String prefix = "onelevel.";
			assertTrue(prefix + "name.prefix.2","value.2".equals(PropertyListManager.getProperty(prefix + "name.prefix.2")));
			assertTrue(prefix + "name.prefix.2.1","value.2.1".equals(PropertyListManager.getProperty(prefix + "name.prefix.2.1")));
			assertTrue(prefix + "name.prefix.2.2","value.2.2".equals(PropertyListManager.getProperty(prefix + "name.prefix.2.2")));

			prefix = "two.levels.";
			assertTrue(prefix + "name.prefix.3","value.3".equals(PropertyListManager.getProperty(prefix + "name.prefix.3")));
			assertTrue(prefix + "name.prefix.3.1","value.3.1".equals(PropertyListManager.getProperty(prefix + "name.prefix.3.1")));
			assertTrue(prefix + "name.prefix.3.2","value.3.2".equals(PropertyListManager.getProperty(prefix + "name.prefix.3.2")));

			assertTrue("name.noprefix.4","value.4".equals(PropertyListManager.getProperty("name.noprefix.4")));
			assertTrue("name.noprefix.4.1","value.4.1".equals(PropertyListManager.getProperty("name.noprefix.4.1")));
			assertTrue("name.noprefix.4.2","value.4.2".equals(PropertyListManager.getProperty("name.noprefix.4.2")));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationAPropertyListPropertyArray() {
		try {
			doLoadFromResource();

			assertEquals("name.noprefix.5","value.5",PropertyListManager.getProperty("name.noprefix.5"));
			assertTrue("name.noprefix.5 == String",PropertyListManager.isPropertyAString("name.noprefix.5"));
			assertFalse("name.noprefix.5 != Array",PropertyListManager.isPropertyAnArray("name.noprefix.5"));

			String[] array = PropertyListManager.getPropertyArray("name.noprefix.5.1");
			assertTrue("name.noprefix.5.1 == Array",PropertyListManager.isPropertyAnArray("name.noprefix.5.1"));
			assertFalse("name.noprefix.5.1 != String",PropertyListManager.isPropertyAString("name.noprefix.5.1"));

			assertTrue("Length",array.length == 2);
			assertEquals("One",array[0],"One");
			assertEquals("Two",array[1],"Two");
			assertFalse("Three",array[1].equals("Three"));

			String prefix = "onelevel.";
			assertEquals(prefix + "name.prefix.6","value.6",PropertyListManager.getProperty(prefix + "name.prefix.6"));
			assertTrue(prefix + "name.prefix.6 == String",PropertyListManager.isPropertyAString(prefix + "name.prefix.6"));
			assertFalse(prefix + "name.prefix.6 != Array",PropertyListManager.isPropertyAnArray(prefix + "name.prefix.6"));

			array = PropertyListManager.getPropertyArray(prefix + "name.prefix.6.1");
			assertTrue(prefix + "name.prefix.6.1 == Array",PropertyListManager.isPropertyAnArray(prefix + "name.prefix.6.1"));
			assertFalse(prefix + "name.prefix.6.1 != String",PropertyListManager.isPropertyAString(prefix + "name.prefix.6.1"));

			assertTrue("Length",array.length == 3);
			assertEquals("One",array[0],"One");
			assertNull("Two",array[1]);
			assertEquals("Three",array[2],"Three");			
			assertFalse("Four",array[2].equals("Four"));
			
			assertFalse("bogus string",PropertyListManager.isPropertyAString("bogus.string"));
			assertFalse("bogus array",PropertyListManager.isPropertyAnArray("bogus.array"));
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationAPropertyListPropertyHash() {
		try {
			doLoadFromResource();
			
			assertTrue("name.noprefix.7.1 == Hash",PropertyListManager.isPropertyAHash("name.noprefix.7.1"));
			assertFalse("name.noprefix.7.1 != Array",PropertyListManager.isPropertyAnArray("name.noprefix.7.1"));
			
			assertTrue("name.noprefix.7.1 has Hash entry A",PropertyListManager.isPropertyHashValueSet("name.noprefix.7.1","a"));
			assertTrue("name.noprefix.7.1 has Hash entry B",PropertyListManager.isPropertyHashValueSet("default","name.noprefix.7.1","b"));

			assertFalse("name.noprefix.7.1 does not have Hash entry C",PropertyListManager.isPropertyHashValueSet("default","name.noprefix.7.1","c"));
			assertFalse("name.noprefix.7.X does not have Hash entry C",PropertyListManager.isPropertyHashValueSet("default","name.noprefix.7.x","c"));

			PropertyHash hash = PropertyListManager.getPropertyHash("name.noprefix.7.1");
			String a7 = (String) hash.get("a");
			assertEquals(a7,"One");
			
			String b7 = (String) hash.get("b");
			assertEquals(b7,"Two");

			String c7 = (String) hash.get("c");			
			assertNull(c7);

			Properties propertiesD7 = PropertyListManager.getPropertyHashAsJavaProperties("name.noprefix.7.1");
			assertEquals(2,propertiesD7.size());
			assertEquals("One",propertiesD7.getProperty("a"));
			assertEquals("Two",propertiesD7.getProperty("b"));

			assertTrue("name.noprefix.8.1 has Hash entry A",PropertyListManager.isPropertyHashValueSet("onelevel.name.prefix.8.1","a"));
			assertTrue("name.noprefix.8.1 has Hash entry B",PropertyListManager.isPropertyHashValueSet("default","onelevel.name.prefix.8.1","b"));

			assertFalse("name.noprefix.8.1 does not have Hash entry D",PropertyListManager.isPropertyHashValueSet("default","onelevel.name.prefix.8.1","d"));
			assertFalse("name.noprefix.8.X does not have Hash entry D",PropertyListManager.isPropertyHashValueSet("default","onelevel.name.prefix.8.1","d"));

			String a8 = PropertyListManager.getPropertyHashValue("onelevel.name.prefix.8.1","a");
			assertEquals(a8,"One");
		
			String b8 = PropertyListManager.getPropertyHashValue("onelevel.name.prefix.8.1","b");
			assertEquals(b8,"");

			String c8 = PropertyListManager.getPropertyHashValue("default","onelevel.name.prefix.8.1","c");
			assertEquals(c8,"Three");

			String d8 = PropertyListManager.getPropertyHashValue("default","onelevel.name.prefix.8.1","d");
			assertNull(d8);
			
			Properties propertiesD8 = PropertyListManager.getPropertyHashAsJavaProperties("default","onelevel.name.prefix.8.1");
			assertEquals(3,propertiesD8.size());
			assertEquals("One",propertiesD8.getProperty("a"));
			assertEquals("",propertiesD8.getProperty("b"));
			assertEquals("Three",propertiesD8.getProperty("c"));
			assertNull(propertiesD8.getProperty("d"));

			Properties propertiesD9 = PropertyListManager.getPropertyHashAsJavaProperties("onelevel.name.prefix.8.X");
			assertNull(propertiesD9);

/*
			assertTrue("Length",array.length == 2);
			assertEquals("One",array[0],"One");
			assertEquals("Two",array[1],"Two");
			assertFalse("Three",array[1].equals("Three"));

			String prefix = "onelevel.";
			assertEquals(prefix + "name.prefix.6","value.6",PropertyListManager.getProperty(prefix + "name.prefix.6"));
			assertTrue(prefix + "name.prefix.6 == String",PropertyListManager.isPropertyAString(prefix + "name.prefix.6"));
			assertFalse(prefix + "name.prefix.6 != Array",PropertyListManager.isPropertyAnArray(prefix + "name.prefix.6"));

			array = PropertyListManager.getPropertyArray(prefix + "name.prefix.6.1");
			assertTrue(prefix + "name.prefix.6.1 == Array",PropertyListManager.isPropertyAnArray(prefix + "name.prefix.6.1"));
			assertFalse(prefix + "name.prefix.6.1 != String",PropertyListManager.isPropertyAString(prefix + "name.prefix.6.1"));

			assertTrue("Length",array.length == 3);
			assertEquals("One",array[0],"One");
			assertNull("Two",array[1]);
			assertEquals("Three",array[2],"Three");			
			assertFalse("Four",array[2].equals("Four"));
			
			assertFalse("bogus string",PropertyListManager.isPropertyAString("bogus.string"));
			assertFalse("bogus array",PropertyListManager.isPropertyAnArray("bogus.array"));
*/			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationAPropertyListSystemProperties() {
		try {
			doLoadFromResource();

			assertTrue("name.noprefix.9","value.9".equals(PropertyListManager.getProperty("name.noprefix.9")));
			
			String systemPropertyValue = System.getProperty("name.noprefix.9.1");
			assertTrue("name.noprefix.9.1","value.9.1".equals(systemPropertyValue));
			
			String prefix = "onelevel.";
			assertTrue(prefix + "name.prefix.9","value.9".equals(PropertyListManager.getProperty(prefix + "name.prefix.9")));
			
			systemPropertyValue = System.getProperty(prefix + "name.prefix.9.2");
			assertTrue("name.prefix.9.2","value.9.2".equals(systemPropertyValue));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationAPropertyListSecurityProperties() {
		try {
			doLoadFromResource();

			assertTrue("name.noprefix.10","value.10".equals(PropertyListManager.getProperty("name.noprefix.10")));
			
			String securityPropertyValue = Security.getProperty("name.noprefix.10.1");
			assertTrue("name.noprefix.10.1","value.10.1".equals(securityPropertyValue));
			
			String prefix = "onelevel.";
			assertTrue(prefix + "name.prefix.10","value.10".equals(PropertyListManager.getProperty(prefix + "name.prefix.10")));
			
			securityPropertyValue = Security.getProperty(prefix + "name.prefix.10.2");
			assertTrue("name.prefix.10.2","value.10.2".equals(securityPropertyValue));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}
	
	public ApplicationATestCase(String arg0) {
		super(arg0);
	}
}
