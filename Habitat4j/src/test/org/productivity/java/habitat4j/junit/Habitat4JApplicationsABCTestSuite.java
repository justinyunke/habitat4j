package org.productivity.java.habitat4j.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Habitat4J Test Suite for Applications A, B, and C.
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
 * @version $Id: Habitat4JApplicationsABCTestSuite.java,v 1.3 2004/07/19 04:14:00 cvs Exp $
 */
public class Habitat4JApplicationsABCTestSuite extends TestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Applications A, B, and C Tests");
		
		int i = 0;
		for(i=0; i<ApplicationATestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationATestCase(ApplicationATestCase.TEST_LIST[i]));
		}
		
		for(i=0; i<ApplicationBTestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationBTestCase(ApplicationBTestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationCTestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationCTestCase(ApplicationCTestCase.TEST_LIST[i]));
		}

		return suite;
	}
}
