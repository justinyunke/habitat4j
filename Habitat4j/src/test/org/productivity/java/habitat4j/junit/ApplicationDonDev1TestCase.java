package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleLDAPBean;

/**
 * This class consists of JUnit tests for deploying Application D on Development
 * Worstation #1 with Habitat4J.
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
 * @version $Id: ApplicationDonDev1TestCase.java,v 1.12 2006/08/16 13:32:21 cvs Exp $
 */
public class ApplicationDonDev1TestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	private static final String APPLICATION_NAME			= "ApplicationD";
	private static final String SERVER_NAME					= "servers/development/workstation-1/";
	
	public static final String[] TEST_LIST = {
		"testServerIdentity",
		"testLDAPonDevelopmentWorkstation1"
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
		
		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties-ldap.xml");
		
		logger.info(PropertyListManager.getPropertyList("ldap").toString());
	}
	
	public void testServerIdentity() {
		try {
			doLoadFromResource();

			assertEquals(APPLICATION_NAME,ServerIdentityManager.getApplicationName());
			assertEquals("Dev",ServerIdentityManager.getEnvironment());
			assertEquals("workstation-1",ServerIdentityManager.getInstanceName());
			assertEquals("Development Workstation #1",ServerIdentityManager.getInstanceDescription());
			assertEquals("1",ServerIdentityManager.getInstanceEnumeration());
			assertEquals(0,ServerIdentityManager.getRoles().size());
			assertNull(ServerIdentityManager.getRole("bogusRole"));
			assertEquals(3,ServerIdentityManager.getPragmaDefinitions().size());
			assertEquals("jxsmith",ServerIdentityManager.getPragmaDefinition("workstationUser"));
			assertEquals("Start: ",ServerIdentityManager.getPragmaDefinition("substitutionTestStart"));
			assertEquals("success.",ServerIdentityManager.getPragmaDefinition("substitutionTestEnd"));
			
			assertEquals(PropertyListManager.getProperty("ldap","detectedOS"),ServerIdentityManager.getInstanceOS());

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
		
		logger.info(ServerIdentityManager.toDisplayString());
	}
	
	public void testLDAPonDevelopmentWorkstation1() {
		try {
			doLoadFromResource();
			
			SampleLDAPBean ldap = (SampleLDAPBean) PropertyListManager.getPropertyBean("ldap","login.ldap.server");

			assertNotNull(ldap);
			assertEquals(ldap.getHost(),"ldap-dev-1.bogus.productivity.org");
			assertEquals(ldap.getPort(),389);
			assertEquals(ldap.getService(),"ldap");
			assertNull(ldap.getDescription());
			assertEquals(ldap.getBaseDN(),"o=habitat4j.org,ou=dev");
			assertEquals(ldap.getAuthUser(),"jxsmith");
			assertEquals(ldap.getAuthPass(),"testpassword");
			
			String testNISI = PropertyListManager.getProperty("ldap","test.context.not.in.server.identity");
			assertTrue("true".equals(testNISI));

			String testPS = PropertyListManager.getProperty("ldap","testOfPragmaSubstitution");
			assertEquals("Start: usertwice=jxsmithjxsmith test=success.",testPS);

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public ApplicationDonDev1TestCase(String arg0) {
		super(arg0);
	}
}
