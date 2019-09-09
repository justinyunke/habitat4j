package org.productivity.java.habitat4j.junit;

import java.security.Security;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleCommProtocolBean;

/**
 * Application C Test Cases for Habitat4J.
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
 * @version $Id: ApplicationCTestCase.java,v 1.14 2006/02/12 16:29:26 cvs Exp $
 */
public class ApplicationCTestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	private static final String APPLICATION_NAME			= "ApplicationC";
	private static final String SERVER_NAME					= "servers/development/workstation-1/";
	
	public static final String[] TEST_LIST = {
		"testServerIdentity",
		"testApplicationCEncodedAndEncryptedProperties",
		"testApplicationCEncodedAndEncryptedSystemProperties",
		"testApplicationCEncodedAndEncryptedSecurityProperties",
		"testApplicationCEncodedAndEncryptedPropertiesPackage"
	};
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(SERVER_NAME,APPLICATION_NAME);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void doLoadFromResource() throws PropertyListHandlerException {
		String applicationPropertyPackage = DATA_EXAMPLES_RESOURCE_PATH + "apps/" + APPLICATION_NAME;
		
		PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_SYSTEM_PROPERTY_OVERRIDE,"true");
		PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_SECURITY_PROPERTY_OVERRIDE,"true");
		PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_SERVER_IDENTITY_PRAGMA_OVERRIDE,"true");
		
		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties.xml");
		PropertyListManager.loadPropertyListFromResource("packagePrefixList",applicationPropertyPackage + "/properties-package.xml");

		logger.info(PropertyListManager.getPropertyList().toString());
		logger.info(PropertyListManager.getPropertyList("packagePrefixList").toString());
	}

	public void testServerIdentity() {
		try {
			doLoadFromResource();

			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			assertEquals("v10a",ServerIdentityManager.getVersion());
			assertEquals("jxsmith",ServerIdentityManager.getModifiedBy());
			assertEquals("Dev",ServerIdentityManager.getEnvironment());
			assertEquals("workstation-1",ServerIdentityManager.getInstanceName());
			assertEquals("Development Workstation #1",ServerIdentityManager.getInstanceDescription());
			assertEquals("1",ServerIdentityManager.getInstanceEnumeration());
			assertEquals(0,ServerIdentityManager.getRoles().size());
			assertNull(ServerIdentityManager.getRole("bogusRole"));
			assertEquals(3,ServerIdentityManager.getPragmaDefinitions().size());
			assertEquals("jxsmith",ServerIdentityManager.getPragmaDefinition("workstationUser"));

			assertNull(PropertyListManager.getProperty("prop7"));
			assertEquals("testE",PropertyListManager.getProperty("prop8"));		

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
		
		logger.info(ServerIdentityManager.toDisplayString());
	}

	public void testPragmaDefinitions() {
		try {
			doLoadFromResource();
			
			PropertyList list = PropertyListManager.getPropertyList();
		
			assertEquals(3,list.getPragmaDefinitions().size());
			
			assertEquals("testA",PropertyListManager.getProperty("prop1"));
			assertEquals("testB",PropertyListManager.getProperty("prop2"));
			assertNull(PropertyListManager.getProperty("prop3"));

			assertEquals("John Z Smith",PropertyListManager.getProperty("user"));
			
			assertEquals("testC",PropertyListManager.getProperty("prop4"));

			assertNull(PropertyListManager.getProperty("prop5"));

			assertEquals("testD",PropertyListManager.getProperty("prop6"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}
	}

	public void testApplicationCEncodedAndEncryptedProperties() {
		try {
			doLoadFromResource();
			
			assertEquals(PropertyListManager.getProperty("encoded.1"),"encoded.value.1");
			assertEquals(PropertyListManager.getProperty("encrypted.2"),"encrypted.value.2");
			assertEquals(PropertyListManager.getProperty("encoded.encrypted.3"),"encoded.encrypted.value.3");
			assertEquals(PropertyListManager.getProperty("encrypted.encoded.4"),"encrypted.encoded.value.4");

			String[] array = PropertyListManager.getPropertyArray("encoded.encrypted.array.1");
			assertEquals(array[0],"encoded.value.1.1");
			assertEquals(array[1],"encrypted.value.1.2");
			assertEquals(array[2],"encoded.encrypted.value.1.3");
			assertEquals(array[3],"encrypted.encoded.value.1.4");

			SampleCommProtocolBean commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("ldap.server.local");
			assertEquals("127.0.0.1",commProtocolBean.getHost());
			assertEquals(389,commProtocolBean.getPort());
			assertEquals("ldap",commProtocolBean.getService());
			assertEquals("LDAP (Directory) Server",commProtocolBean.getDescription());

			SampleCommProtocolBean[] commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");			
			SampleCommProtocolBean bean1 = commProtocolBeanArray[0];
			SampleCommProtocolBean bean2 = commProtocolBeanArray[1];
			
			assertEquals("10.0.0.1",bean1.getHost());
			assertEquals(25,bean1.getPort());
			assertEquals("smtp",bean1.getService());
			assertEquals("SMTP (Mail) Server",bean1.getDescription());

			assertEquals("10.0.0.2",bean2.getHost());
			assertEquals(465,bean2.getPort());
			assertEquals("smtps",bean2.getService());
			assertEquals("SMTPS (Mail) Server",bean2.getDescription());
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationCEncodedAndEncryptedSystemProperties() {
		try {
			doLoadFromResource();
			
			assertEquals(System.getProperty("system.encoded.1"),"system.encoded.value.1");
			assertEquals(System.getProperty("system.encrypted.2"),"system.encrypted.value.2");
			assertEquals(System.getProperty("system.encoded.encrypted.3"),"system.encoded.encrypted.value.3");
			assertEquals(System.getProperty("system.encrypted.encoded.4"),"system.encrypted.encoded.value.4");
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}			

	public void testApplicationCEncodedAndEncryptedSecurityProperties() {
		try {
			doLoadFromResource();
			
			assertEquals(Security.getProperty("security.encoded.1"),"security.encoded.value.1");
			assertEquals(Security.getProperty("security.encrypted.2"),"security.encrypted.value.2");
			assertEquals(Security.getProperty("security.encoded.encrypted.3"),"security.encoded.encrypted.value.3");
			assertEquals(Security.getProperty("security.encrypted.encoded.4"),"security.encrypted.encoded.value.4");

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}			

	public void testApplicationCEncodedAndEncryptedPropertiesPackage() {
		try {
			doLoadFromResource();
			
			assertEquals(PropertyListManager.getProperty("packagePrefixList","encoded.5"),"encoded.value.5");
			assertEquals(PropertyListManager.getProperty("packagePrefixList","encrypted.6"),"encrypted.value.6");
			assertEquals(PropertyListManager.getProperty("packagePrefixList","encoded.encrypted.7"),"encoded.encrypted.value.7");
			assertEquals(PropertyListManager.getProperty("packagePrefixList","encrypted.encoded.8"),"encrypted.encoded.value.8");

			String[] array = PropertyListManager.getPropertyArray("packagePrefixList","encoded.encrypted.array.2");
			assertEquals(array[0],"encoded.value.2.1");
			assertEquals(array[1],"encrypted.value.2.2");
			assertEquals(array[2],"encoded.encrypted.value.2.3");
			assertEquals(array[3],"encrypted.encoded.value.2.4");

			SampleCommProtocolBean commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("packagePrefixList","ldap.server.local");
			assertEquals("127.0.0.1",commProtocolBean.getHost());
			assertEquals(646,commProtocolBean.getPort());
			assertEquals("ldaps",commProtocolBean.getService());
			assertEquals("LDAPS (Directory) Server",commProtocolBean.getDescription());

			Object[] commProtocolBeanArray = PropertyListManager.getPropertyBeanArray("packagePrefixList","internal.smtp.servers");			
			SampleCommProtocolBean bean1 = (SampleCommProtocolBean) commProtocolBeanArray[0];
			SampleCommProtocolBean bean2 = (SampleCommProtocolBean) commProtocolBeanArray[1];
			
			assertEquals("10.1.0.1",bean1.getHost());
			assertEquals(25,bean1.getPort());
			assertEquals("smtp",bean1.getService());
			assertEquals("SMTP (Mail) Server",bean1.getDescription());

			assertEquals("10.1.0.2",bean2.getHost());
			assertEquals(465,bean2.getPort());
			assertEquals("smtps",bean2.getService());
			assertEquals("SMTPS (Mail) Server",bean2.getDescription());
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public ApplicationCTestCase(String arg0) {
		super(arg0);
	}
}
