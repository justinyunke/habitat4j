package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.compat.PropertyListXMLMap;
import org.productivity.java.habitat4j.compat.ServerIdentityXMLMap;

/**
 * Habitat4J Test Case for Application F on Dev Workstation #2.
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
 * @version $Id: ApplicationFonDev2TestCase.java,v 1.2 2004/09/04 05:26:15 cvs Exp $
 */
public class ApplicationFonDev2TestCase extends Habitat4JBaseTestCase  {
	private static final String APPLICATION_NAME			= "ApplicationF";
	private static final String SERVER_NAME					= "servers/development/workstation-2/";
	
	public static final String[] TEST_LIST = {
		"testAlternatePropertyFile"
	};
	
	private ServerIdentityXMLMap setupServerIdentityMap() {
		ServerIdentityXMLMap map = new ServerIdentityXMLMap();
		
		map.addElementMapping("server",ServerIdentityXMLMap.SERVER_IDENTITY_ELEMENT);
		
		map.addElementMapping("server/realm",ServerIdentityXMLMap.SERVER_IDENTITY_APPLICATION_ENVIRONMENT_ELEMENT);
		map.addElementMapping("server/serverName",ServerIdentityXMLMap.SERVER_IDENTITY_APPLICATION_INSTANCE_NAME_ELEMENT);
		map.addElementMapping("server/desc",ServerIdentityXMLMap.SERVER_IDENTITY_APPLICATION_INSTANCE_DESCRIPTION_ELEMENT);
		
		return map;
	}
	
	private PropertyListXMLMap setupPropertyListMap() {
		PropertyListXMLMap map = new PropertyListXMLMap();
		
		map.addElementMapping("MyProperties",PropertyListXMLMap.PROPERTY_LIST_ELEMENT);
		
		map.addElementMapping("MyProperties/MyProperty",PropertyListXMLMap.PROPERTY_LIST_PROPERTY_ELEMENT);
		map.addAttributeMapping("MyProperties/MyProperty","myName",PropertyListXMLMap.PROPERTY_LIST_PROPERTY_ELEMENT_NAME_ATTRIBUTE);
		
		return map;
	}
	
	protected void setUp() throws Exception {
		// NOTE: Bootstrap Code is in the BaseTestCase
		ServerIdentityXMLMap map = setupServerIdentityMap();
		super.setUp(SERVER_NAME,APPLICATION_NAME,SERVER_IDENTITY_MODE_FILE,false,map);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected void doLoadFromResource() throws PropertyListHandlerException {
		//
	}
	
	protected void doLoadFromResource(PropertyListXMLMap map) throws PropertyListHandlerException {
		String applicationPropertyPackage = DATA_EXAMPLES_RESOURCE_PATH + "apps/" + APPLICATION_NAME;
		
		PropertyListManager.loadPropertyListFromResource(applicationPropertyPackage + "/properties.xml",map);
		
		logger.info(PropertyListManager.getPropertyList().toString());
	}
	
	public void testAlternatePropertyFile() {
		try {
			PropertyListXMLMap map = setupPropertyListMap();
			doLoadFromResource(map);
			
			logger.info(PropertyListManager.getServerIdentity().toString());
			
			assertEquals(PropertyListManager.getProperty("myname"),"myvalue");
			
		} catch (Exception e) {
			logger.info(e);
			fail(e.getMessage());
		}		
	}

	public ApplicationFonDev2TestCase(String arg0) {
		super(arg0);
	}
}
