package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.PropertyBeanHash;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.junit.examples.propertybean.EveryParameterTypeBean;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleCommProtocolBean;

/**
 * Application B Test Cases for Habitat4J.
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
 * @version $Id: ApplicationBTestCase.java,v 1.18 2005/09/14 04:30:50 cvs Exp $
 */
public class ApplicationBTestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	private static final String APPLICATION_NAME			= "ApplicationB";
	
	public static final String[] TEST_LIST = {
		"testServerIdentity",
		"testApplicationBPropertyAsInt",
		"testApplicationBPropertyBoolean",
		"testApplicationBPropertyListAttributes",
		"testApplicationBPropertyBean",
		"testApplicationBPropertyBeanCopy",
		"testApplicationBPropertyBeanArray",
		"testApplicationBPropertyBeanArrayCopy",
		"testApplicationBPropertyBeanPackage",
		"testApplicationBPropertyBeanArrayPackage",
		"testApplicationBPropertyBeanEveryParameterType",
		"testApplicationBPropertyBeanEveryParameterTypePackage"
	};
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		super.setUp(null,APPLICATION_NAME,SERVER_IDENTITY_MODE_JVM);
		
		PropertyListManager.addGlobalPropertyBeanDefinition("cpb", SampleCommProtocolBean.class);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void doLoadFromResource() throws PropertyListHandlerException {
		String applicationPropertyPackage = DATA_EXAMPLES_RESOURCE_PATH + "apps/" + APPLICATION_NAME;

		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties.xml");
		PropertyListManager.loadPropertyListFromResource("packagePrefixList",applicationPropertyPackage + "/properties-package.xml");
		
		PropertyList list = PropertyListManager.getPropertyList();
		logger.info(list.toString());
		logger.info(PropertyListManager.getPropertyList("packagePrefixList").toString());
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

	public void testApplicationBPropertyListAttributes() {
		try {
			doLoadFromResource();
			
			assertEquals(PropertyListManager.getPropertyListVersion(),"1.0a");
			assertEquals(PropertyListManager.getPropertyListModifiedBy(),"jpy");
			assertEquals(PropertyListManager.getPropertyListReloadSerial(),2004071801);
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationBPropertyAsInt() {
		try {
			doLoadFromResource();
			assertEquals(-1,PropertyListManager.getPropertyAsInt("int.value.-1"));
			assertEquals(0,PropertyListManager.getPropertyAsInt("int.value.0"));
			assertEquals(1,PropertyListManager.getPropertyAsInt("int.value.1"));
			
			assertEquals(-1,PropertyListManager.getPropertyAsInt("int.value.re.1"));
			assertEquals(-1,PropertyListManager.getPropertyAsInt("int.value.re.2"));
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}	
	}

	public void testApplicationBPropertyBoolean() {
		try {
			doLoadFromResource();
			assertTrue(PropertyListManager.isPropertyTrue("boolean.value.true"));
			assertTrue(PropertyListManager.isPropertyTrue("boolean.value.on"));
			assertTrue(PropertyListManager.isPropertyTrue("boolean.value.yes"));
			assertTrue(PropertyListManager.isPropertyTrue("boolean.value.1"));
			
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.false"));
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.off"));
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.no"));
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.0"));
			
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.re.1"));
			assertFalse(PropertyListManager.isPropertyTrue("boolean.value.re.2"));

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}	
	}

	public void testApplicationBPropertyBean() {
		try {
			doLoadFromResource();
			
			_testApplicationBPropertyBean();

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationBPropertyBeanCopy() {
		try {
			doLoadFromResource();
			
			SampleCommProtocolBean origBean1 = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("ldap.server.local");
			SampleCommProtocolBean origBean2 = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("ldap.server.local");
			SampleCommProtocolBean copyBean1 = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("ldap.server.local");
			SampleCommProtocolBean copyBean2 = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("ldap.server.local");
			
			assertEquals(origBean1.hashCode(),origBean2.hashCode());
			assertFalse(origBean1.hashCode() == copyBean1.hashCode());
			assertFalse(origBean1.hashCode() == copyBean2.hashCode());
			assertFalse(copyBean1.hashCode() == copyBean2.hashCode());

			origBean1.setDescription("Altered");

			SampleCommProtocolBean origBean1Altered = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("ldap.server.local");
			
			assertEquals("Altered",origBean1Altered.getDescription());
			assertNull(copyBean1.getDescription());
			
			origBean1Altered.setDescription(null);
			
			_testApplicationBPropertyBean();
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	protected void _testApplicationBPropertyBean() {
		SampleCommProtocolBean commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("ldap.server.local");
		assertEquals("127.0.0.1",commProtocolBean.getHost());
		assertEquals(389,commProtocolBean.getPort());
		assertEquals("ldap",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());

		commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("http.server.remote");
		assertEquals("10.0.0.1",commProtocolBean.getHost());
		assertEquals(80,commProtocolBean.getPort());
		assertEquals("http",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());

		String prefix = "secure.";
		commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy(prefix + "ldap.server.local");
		assertEquals("127.0.0.1",commProtocolBean.getHost());
		assertEquals(646,commProtocolBean.getPort());
		assertEquals("ldaps",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());

		commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy(prefix + "http.server.remote");
		assertEquals("10.0.0.1",commProtocolBean.getHost());
		assertEquals(443,commProtocolBean.getPort());
		assertEquals("https",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());

		commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("imap.server.local");
		assertEquals("10.0.0.2",commProtocolBean.getHost());
		assertEquals(-1,commProtocolBean.getPort());
		assertEquals("imaps",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());

		commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanCopy("imap.server.remote");
		assertEquals("10.1.0.2",commProtocolBean.getHost());
		assertEquals(143,commProtocolBean.getPort());
		assertEquals("imap",commProtocolBean.getService());
		assertNull(commProtocolBean.getDescription());
	}

	public void testApplicationBPropertyBeanArray() {
		try {
			doLoadFromResource();

			_testApplicationBPropertyBeanArray();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}
	
	public void testApplicationBPropertyBeanArrayCopy() {
		try {
			doLoadFromResource();

			SampleCommProtocolBean[] commProtocolBeanArray1 = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");			
			SampleCommProtocolBean[] commProtocolBeanArray2 = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");			

			SampleCommProtocolBean[] commProtocolBeanArrayCopy1 = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArrayCopy("internal.smtp.servers");			
			SampleCommProtocolBean[] commProtocolBeanArrayCopy2 = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArrayCopy("internal.smtp.servers");			

			assertFalse(commProtocolBeanArray1.hashCode() == commProtocolBeanArray2.hashCode());
			assertFalse(commProtocolBeanArray1.hashCode() == commProtocolBeanArrayCopy1.hashCode());
			assertFalse(commProtocolBeanArray1.hashCode() == commProtocolBeanArrayCopy2.hashCode());
			assertFalse(commProtocolBeanArrayCopy1.hashCode() == commProtocolBeanArrayCopy2.hashCode());

			SampleCommProtocolBean origBean1_0 = commProtocolBeanArray1[0];
			SampleCommProtocolBean origBean2_0 = commProtocolBeanArray2[0];

			SampleCommProtocolBean copyBean1_0 = commProtocolBeanArrayCopy1[0];			
			SampleCommProtocolBean copyBean2_0 = commProtocolBeanArrayCopy2[0];
			
			assertEquals(origBean1_0.hashCode(),origBean2_0.hashCode());
			assertFalse(origBean1_0.hashCode() == copyBean1_0.hashCode());
			assertFalse(origBean1_0.hashCode() == copyBean2_0.hashCode());
			assertFalse(copyBean1_0.hashCode() == copyBean2_0.hashCode());

			origBean1_0.setDescription("Altered");

			SampleCommProtocolBean[] commProtocolBeanArray1Altered = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");
			SampleCommProtocolBean origBean1_0Altered = commProtocolBeanArray1Altered[0];
			
			assertEquals(origBean1_0Altered.getDescription(),"Altered");
			assertNull(copyBean1_0.getDescription());
						
			_testApplicationBPropertyBeanArray();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	protected void _testApplicationBPropertyBeanHash() {
		try {
			doLoadFromResource();
			
			assertTrue(PropertyListManager.isPropertyABeanHash("external.imap.servers.hash.1"));			

			PropertyBeanHash beanHash1 = PropertyListManager.getPropertyBeanHash("external.imap.servers.hash.1");

			SampleCommProtocolBean bean1a = (SampleCommProtocolBean) beanHash1.get("a"); 
			
			assertEquals("10.6.0.1",bean1a.getHost());
			assertEquals(143,bean1a.getPort());
			assertEquals("imap",bean1a.getService());
			assertNull(bean1a.getDescription());
			
			SampleCommProtocolBean bean1b = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanHashValue("external.imap.servers.hash.1","b");
			
			assertEquals("10.7.0.2",bean1b.getHost());
			assertEquals(993,bean1b.getPort());
			assertEquals("imaps",bean1b.getService());
			assertNull(bean1b.getDescription());			

			PropertyBeanHash beanHash2 = PropertyListManager.getPropertyBeanHash("external.imap.servers.hash.2");
			
			SampleCommProtocolBean bean2a = (SampleCommProtocolBean) beanHash2.get("c"); 
			
			assertEquals("10.8.0.2",bean2a.getHost());
			assertEquals(143,bean1a.getPort());
			assertEquals("imap",bean1a.getService());
			assertNull(bean1a.getDescription());
			
			SampleCommProtocolBean bean2b = (SampleCommProtocolBean) PropertyListManager.getPropertyBeanHashValue("external.imap.servers.hash.2","d");
			
			assertEquals("10.9.0.2",bean2b.getHost());
			assertEquals(993,bean1b.getPort());
			assertEquals("imaps",bean1b.getService());
			assertNull(bean1b.getDescription());			
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationBPropertyBeanHash() {
		try {
			doLoadFromResource();

			_testApplicationBPropertyBeanHash();
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}
	
	protected void _testApplicationBPropertyBeanArray() {
		try {
			doLoadFromResource();

			assertTrue(PropertyListManager.isPropertyABeanArray("internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyABean("internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyAnArray("internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyAString("internal.smtp.servers"));			

			SampleCommProtocolBean[] commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");			
			SampleCommProtocolBean bean1 = commProtocolBeanArray[0];
			SampleCommProtocolBean bean2 = commProtocolBeanArray[1];
			
			assertEquals("10.0.0.1",bean1.getHost());
			assertEquals(25,bean1.getPort());
			assertEquals("smtp",bean1.getService());
			assertNull(bean1.getDescription());

			assertEquals("10.0.0.2",bean2.getHost());
			assertEquals(25,bean2.getPort());
			assertEquals("smtp",bean2.getService());
			assertNull(bean2.getDescription());

			commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("external.smtp.servers");			
			bean1 = commProtocolBeanArray[0];
			bean2 = commProtocolBeanArray[1];
			
			assertEquals("10.1.0.1",bean1.getHost());
			assertEquals(25,bean1.getPort());
			assertEquals("smtp",bean1.getService());
			assertNull(bean1.getDescription());

			assertEquals("10.1.0.2",bean2.getHost());
			assertEquals(25,bean2.getPort());
			assertEquals("smtp",bean2.getService());
			assertNull(bean2.getDescription());

			String prefix = "secure.";
			commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray(prefix + "internal.smtp.servers");			
			bean1 = commProtocolBeanArray[0];
			bean2 = commProtocolBeanArray[1];
			
			assertEquals("10.2.0.1",bean1.getHost());
			assertEquals(465,bean1.getPort());
			assertEquals("smtps",bean1.getService());
			assertNull(bean1.getDescription());

			assertEquals("10.2.0.2",bean2.getHost());
			assertEquals(465,bean2.getPort());
			assertEquals("smtps",bean2.getService());
			assertNull(bean2.getDescription());
			
			commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray(prefix + "external.smtp.servers");			
			bean1 = commProtocolBeanArray[0];
			bean2 = commProtocolBeanArray[1];
			
			assertEquals("10.3.0.1",bean1.getHost());
			assertEquals(465,bean1.getPort());
			assertEquals("smtps",bean1.getService());
			assertNull(bean1.getDescription());

			assertEquals("10.3.0.2",bean2.getHost());
			assertEquals(465,bean2.getPort());
			assertEquals("smtps",bean2.getService());
			assertNull(bean2.getDescription());

			commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.imap.servers");			
			assertEquals(commProtocolBeanArray.length,1);
			bean1 = commProtocolBeanArray[0];

			assertEquals("10.4.0.2",bean1.getHost());
			assertEquals(993,bean1.getPort());
			assertEquals("imaps",bean1.getService());
			assertNull(bean1.getDescription());

			commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("external.imap.servers");			
			assertEquals(commProtocolBeanArray.length,2);
			bean1 = commProtocolBeanArray[0];
			bean2 = commProtocolBeanArray[1];

			assertEquals("10.5.0.1",bean1.getHost());
			assertEquals(143,bean1.getPort());
			assertEquals("imap",bean1.getService());
			assertNull(bean1.getDescription());

			assertEquals("10.5.0.2",bean2.getHost());
			assertEquals(993,bean2.getPort());
			assertEquals("imaps",bean2.getService());
			assertNull(bean2.getDescription());

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationBPropertyBeanPackage() {
		try {
			doLoadFromResource();
			
			assertTrue(PropertyListManager.isPropertyABeanArray("packagePrefixList","internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyABean("packagePrefixList","internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyAnArray("packagePrefixList","internal.smtp.servers"));			
			assertFalse(PropertyListManager.isPropertyAString("packagePrefixList","internal.smtp.servers"));			

			SampleCommProtocolBean commProtocolBean = (SampleCommProtocolBean) PropertyListManager.getPropertyBean("packagePrefixList","ldap.server.local");
			assertEquals("127.0.0.1",commProtocolBean.getHost());
			assertEquals(389,commProtocolBean.getPort());
			assertEquals("ldap",commProtocolBean.getService());
			assertNull(commProtocolBean.getDescription());
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public void testApplicationBPropertyBeanArrayPackage() {
		try {
			doLoadFromResource();
			
			SampleCommProtocolBean[] commProtocolBeanArray = (SampleCommProtocolBean[]) PropertyListManager.getPropertyBeanArray("internal.smtp.servers");			
			SampleCommProtocolBean bean1 = commProtocolBeanArray[0];
			SampleCommProtocolBean bean2 = commProtocolBeanArray[1];
			
			assertNotNull(bean1);
			assertNotNull(bean2);
			
			assertEquals("10.0.0.1",bean1.getHost());
			assertEquals(25,bean1.getPort());
			assertEquals("smtp",bean1.getService());

			assertEquals("10.0.0.2",bean2.getHost());
			assertEquals(25,bean2.getPort());
			assertEquals("smtp",bean2.getService());
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}
	
	/**
	 * 
	 */
	public void testApplicationBPropertyBeanEveryParameterType() {
		try {
			doLoadFromResource();
			
			assertTrue(PropertyListManager.isPropertyABean("every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyABeanArray("every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyAnArray("every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyAString("every.parameter.1"));

			EveryParameterTypeBean ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("every.parameter.1");
			everyParameterSection1(ept);
			
			EveryParameterTypeBean eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBeanCopy("every.parameter.1");
			everyParameterSection1(eptCopy);
			
			ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("every.parameter.2");
			everyParameterSection2(ept);

			eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBeanCopy("every.parameter.2");
			everyParameterSection2(eptCopy);

			ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("every.parameter.null");
			everyParameterSectionNull(ept);

			eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBeanCopy("every.parameter.null");
			everyParameterSectionNull(eptCopy);

			assertFalse(ept.getCloneableBeanParameter().getBuffer().hashCode() == eptCopy.getCloneableBeanParameter().getBuffer().hashCode());
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}	
	}

	/**
	 * 
	 */
	public void testApplicationBPropertyBeanEveryParameterTypePackage() {
		try {
			doLoadFromResource();

			assertTrue(PropertyListManager.isPropertyABean("packagePrefixList","every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyABeanArray("packagePrefixList","every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyAnArray("packagePrefixList","every.parameter.1"));
			assertFalse(PropertyListManager.isPropertyAString("packagePrefixList","every.parameter.1"));

			EveryParameterTypeBean ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("packagePrefixList","every.parameter.1");
			everyParameterSection1(ept);

			EveryParameterTypeBean eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBeanCopy("packagePrefixList","every.parameter.1");
			everyParameterSection1(eptCopy);

			ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("packagePrefixList","every.parameter.2");
			everyParameterSection2(ept);

			eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("packagePrefixList","every.parameter.2");
			everyParameterSection2(eptCopy);
			
			ept = (EveryParameterTypeBean) PropertyListManager.getPropertyBean("packagePrefixList","every.parameter.null");
			everyParameterSectionNull(ept);

			eptCopy = (EveryParameterTypeBean) PropertyListManager.getPropertyBeanCopy("packagePrefixList","every.parameter.null");
			everyParameterSectionNull(eptCopy);
			
			assertFalse(ept.getCloneableBeanParameter().getBuffer().hashCode() == eptCopy.getCloneableBeanParameter().getBuffer().hashCode());

		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());			
		}	
	}

	private void everyParameterSection1(EveryParameterTypeBean ept) {
		assertNotNull(ept);
		
		assertEquals(ept.getShortValue(),1);
		assertEquals(ept.getIntValue(),2);
		assertEquals(ept.getLongValue(),3);
		assertTrue(ept.getDoubleValue() == 4.01d);
		assertTrue(ept.getFloatValue() == 5f);
		assertEquals(ept.getCharValue(),'a');
		assertEquals(ept.isBooleanValue(),true);
		assertEquals(ept.getShortObjectValue().shortValue(),6);
		assertEquals(ept.getIntObjectValue().intValue(),7);
		assertEquals(ept.getBigIntegerObjectValue().intValue(),8);
		assertEquals(ept.getLongObjectValue().longValue(),9);
		assertTrue(ept.getDoubleObjectValue().doubleValue() == 10.01d);
		assertTrue(ept.getFloatObjectValue().floatValue() == 11f);
		assertTrue(ept.getBigDecimalObjectValue().doubleValue() == 12.01d);
		assertEquals(ept.getCharObjectValue().charValue(),'b');
		assertEquals(ept.getBooleanObjectValue().booleanValue(),true);			
		assertEquals(ept.getStringValue(),"abc");					
	}

	private void everyParameterSection2(EveryParameterTypeBean ept) {
		assertNotNull(ept);

		assertEquals(ept.getShortValue(),-1);
		assertEquals(ept.getIntValue(),-2);
		assertEquals(ept.getLongValue(),-3);
		assertTrue(ept.getDoubleValue() == -4.01d);
		assertTrue(ept.getFloatValue() == -5f);
		assertEquals(ept.getCharValue(),'c');
		assertEquals(ept.isBooleanValue(),false);
		assertEquals(ept.getShortObjectValue().shortValue(),-6);
		assertEquals(ept.getIntObjectValue().intValue(),-7);
		assertEquals(ept.getBigIntegerObjectValue().intValue(),-8);
		assertEquals(ept.getLongObjectValue().longValue(),-9);
		assertTrue(ept.getDoubleObjectValue().doubleValue() == -10.01d);
		assertTrue(ept.getFloatObjectValue().floatValue() == -11f);
		assertTrue(ept.getBigDecimalObjectValue().doubleValue() == -12.01d);
		assertEquals(ept.getCharObjectValue().charValue(),'d');
		assertEquals(ept.getBooleanObjectValue().booleanValue(),false);			
		assertEquals(ept.getStringValue(),"cba");					
	}

	/**
	 * Note: This method will generate a lot of ERRORs in the logs, which is
	 * ok.  There is a failsafe that uses the default value of the
	 * bean if a NULL value is set in the Property-List file for a primitive
	 * value.
	 * 
	 * @param ept - the EveryParameterTypeBean
	 */
	private void everyParameterSectionNull(EveryParameterTypeBean ept) {
		assertNotNull(ept);

		assertEquals(ept.getShortValue(),0);
		assertEquals(ept.getIntValue(),0);
		assertEquals(ept.getLongValue(),0);
		assertTrue(ept.getDoubleValue() == 0d);
		assertTrue(ept.getFloatValue() == 0f);
		assertEquals(ept.getCharValue(),'x');
		assertEquals(ept.isBooleanValue(),false);
		assertNull(ept.getShortObjectValue());
		assertNull(ept.getIntObjectValue());
		assertNull(ept.getBigIntegerObjectValue());
		assertNull(ept.getLongObjectValue());
		assertNull(ept.getDoubleObjectValue());
		assertNull(ept.getFloatObjectValue());
		assertNull(ept.getBigDecimalObjectValue());
		assertNull(ept.getCharObjectValue());
		assertNull(ept.getBooleanObjectValue());			
		assertNull(ept.getStringValue());					
	}

	public ApplicationBTestCase(String arg0) {
		super(arg0);
	}
}
