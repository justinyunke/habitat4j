package org.productivity.java.habitat4j.junit;

import junit.framework.TestCase;

import org.productivity.java.habitat4j.Habitat4JBootstrap;
import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.ServerIdentityManager;

/**
 * Habitat4J Test Case for Application G for Bootstrap
 * 
 * <p>Note: server-identity.xml must be present in c:\ or c:\projects or d:\ or d:\projects,
 * and the following pragma definition available, corresponding to workspace:</p>
 * 
 * <code>
 * 		&lt;pragma name="habitat4jWorkspaceDir"&gt;E:\eclipse\workspaces\habitat4j-v1&lt;/pragma&gt;
 * </code>
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
 * @version $Id: ApplicationGforBootstrapTestCase.java,v 1.2 2007/02/25 18:52:04 cvs Exp $
 */
public class ApplicationGforBootstrapTestCase extends TestCase {
	private static final String APPLICATION_NAME			= "ApplicationG";
	
	public static final String[] TEST_LIST = {
		"testReload"
	};
	
	protected void setUp() throws Exception {
		ServerIdentityManager.reset();
		PropertyListManager.reset();
		
		String path = Habitat4JBootstrap.createServerIdentityPath(
			"server-identity.xml",
			new String[] {
				"c:/",
				"c:/projects",
				"d:/",
				"d:/projects"
			}
		);
		
		Habitat4JBootstrap.initialize(
			APPLICATION_NAME, 
			"org/productivity/java/habitat4j/junit/examples/resource/Habitat4JTestSuite/apps/ApplicationG/properties-bootstrap.xml",
			true, // xsdValidationEnabled
			false, // log4jEnabled
			"INFO", // logLevel 
			path // serverIdentityFilePath
		);
	}
	
	protected void tearDown() throws Exception {
		//
	}
	
	public void testReload() {
		PropertyListManager.reloadOnFileChange("test0");
		PropertyListManager.reloadOnFileChange("test1");
		PropertyListManager.reloadOnFileChange("test2");
	}

	public ApplicationGforBootstrapTestCase(String arg0) {
		super(arg0);
	}
}
