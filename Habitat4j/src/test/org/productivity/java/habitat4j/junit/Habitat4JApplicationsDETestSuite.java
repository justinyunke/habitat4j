package org.productivity.java.habitat4j.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Habitat4J Test Suite for Applications D and E.
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
 * @version $Id: Habitat4JApplicationsDETestSuite.java,v 1.9 2007/02/25 18:52:04 cvs Exp $
 */
public class Habitat4JApplicationsDETestSuite extends TestSuite {
	private static boolean timeConsumingTestCasesEnabled = false;
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Applications D and E Tests");
		
		int i;
		
		for(i=0; i<ApplicationDonDev1TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonDev1TestCase(ApplicationDonDev1TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationDonUnitTesting1TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonUnitTesting1TestCase(ApplicationDonUnitTesting1TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationDonUnitTesting2TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonUnitTesting2TestCase(ApplicationDonUnitTesting2TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationDonProduction1TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonProduction1TestCase(ApplicationDonProduction1TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationDonProduction2TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonProduction2TestCase(ApplicationDonProduction2TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationDonProduction3TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationDonProduction3TestCase(ApplicationDonProduction3TestCase.TEST_LIST[i]));
		}

		for(i=0; i<ApplicationEonUnitTesting1TestCase.TEST_LIST.length; i++) {
			suite.addTest(new ApplicationEonUnitTesting1TestCase(ApplicationEonUnitTesting1TestCase.TEST_LIST[i]));
		}

		if (timeConsumingTestCasesEnabled) {
			for(i=0; i<ApplicationEonUnitTesting2TestCase.TEST_LIST.length; i++) {
				suite.addTest(new ApplicationEonUnitTesting2TestCase(ApplicationEonUnitTesting2TestCase.TEST_LIST[i]));
			}
			
			for(i=0; i<ApplicationEonProduction1TestCase.TEST_LIST.length; i++) {
				suite.addTest(new ApplicationEonProduction1TestCase(ApplicationEonProduction1TestCase.TEST_LIST[i]));
			}
		}

		return suite;
	}
}
