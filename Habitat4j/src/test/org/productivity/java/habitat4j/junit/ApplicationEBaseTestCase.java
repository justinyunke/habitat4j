package org.productivity.java.habitat4j.junit;

import java.io.File;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;

/**
 * This class consists of JUnit tests for deploying Application E on Unit Testing Servers
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
 * @version $Id: ApplicationEBaseTestCase.java,v 1.3 2007/02/25 18:52:04 cvs Exp $
 */
public abstract class ApplicationEBaseTestCase extends Habitat4JBaseTestCase implements Habitat4JConstants {
	protected static final String APPLICATION_NAME			= "ApplicationE";
	protected static final String FILE_VERSION_1			= "properties-1.xml";
	protected static final String FILE_VERSION_2			= "properties-2.xml";
	protected static final String FILE_VERSION_3			= "properties-3.xml";
	protected static final String FILE_MAIN					= "properties.xml";
	protected static final String FILE_MAIN_LDAP			= "properties-ldap.xml";
	
	protected byte currentVersion = 0;
	
	protected void doLoadFromResource() {
		// NO-OP
	}
	
	protected void doLoadFromFile() throws PropertyListHandlerException {
		PropertyListManager.loadPropertyListFromFile("reload",DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME + "/" + FILE_MAIN);
		
		logger.info(PropertyListManager.getPropertyList("reload").toString());
	}
	
	protected void renamePropertyList1ToPropertyList() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME;

		File fileFrom = new File(applicationPropertyPackage + "/" + FILE_VERSION_1);
		File fileTo = new File(applicationPropertyPackage + "/" + FILE_MAIN);
		fileFrom.renameTo(fileTo);
		currentVersion=1;
	}

	protected void renamePropertyListToPropertyList1() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME + "/";
		
		File fileFrom = new File(applicationPropertyPackage + FILE_MAIN);
		File fileTo = new File(applicationPropertyPackage + FILE_VERSION_1);
		fileFrom.renameTo(fileTo);
		currentVersion=0;
	}
	
	protected void renamePropertyList2ToPropertyList() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME;

		File fileFrom = new File(applicationPropertyPackage + "/" + FILE_VERSION_2);
		File fileTo = new File(applicationPropertyPackage + "/" + FILE_MAIN);
		fileFrom.renameTo(fileTo);
		currentVersion=2;
	}

	protected void renamePropertyListToPropertyList2() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME;
		
		File fileFrom = new File(applicationPropertyPackage + "/" + FILE_MAIN);
		File fileTo = new File(applicationPropertyPackage + "/" + FILE_VERSION_2);
		fileFrom.renameTo(fileTo);
		currentVersion=0;
	}

	protected void renamePropertyList3ToPropertyList() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME;

		File fileFrom = new File(applicationPropertyPackage + "/" + FILE_VERSION_3);
		File fileTo = new File(applicationPropertyPackage + "/" + FILE_MAIN);
		fileFrom.renameTo(fileTo);
		currentVersion=3;
	}

	protected void renamePropertyListToPropertyList3() {
		String applicationPropertyPackage = DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME;
		
		File fileFrom = new File(applicationPropertyPackage + "/" + FILE_MAIN);
		File fileTo = new File(applicationPropertyPackage + "/" + FILE_VERSION_3);
		fileFrom.renameTo(fileTo);
		currentVersion=0;
	}

	protected void pause(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
			
		} catch (InterruptedException ie) {
			fail(ie.toString());
		}
	}
	
	public ApplicationEBaseTestCase(String arg0) {
		super(arg0);
	}
}
