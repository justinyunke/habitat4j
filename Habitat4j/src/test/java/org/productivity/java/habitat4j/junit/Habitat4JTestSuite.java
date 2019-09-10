package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.common.Habitat4JLogger;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Habitat4J Test Suite.
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
 * @version $Id: Habitat4JTestSuite.java,v 1.15 2007/02/25 04:23:10 cvs Exp $
 */
public class Habitat4JTestSuite extends TestSuite {
	private static boolean commandLine = false;
		
	public static Test suite() {
		if (!commandLine) {
			// Grab the Habitat4JLogger's Singleton instance
			Habitat4JLogger logger = Habitat4JLogger.getInstance();
			
			// Set log4j mode
			logger.selectLog4j();
		}
		
		TestSuite suite = new TestSuite("Habitat4J JUnit Test Suite");
				
		suite.addTest(Habitat4JApplicationsABCTestSuite.suite());
		
		// This one requires some local file switches; removed from Maven process
//		suite.addTest(Habitat4JApplicationsDETestSuite.suite());
		
		suite.addTest(Habitat4JApplicationFTestSuite.suite());
		
		// This one requires some local file switches; removed from Maven process
//		suite.addTest(Habitat4JApplicationGTestSuite.suite());
		
		return suite;
	}
	
	/**
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		commandLine = true;
		
		if (args.length > 0) {
			// Grab the Habitat4JLogger's Singleton instance
			Habitat4JLogger logger = Habitat4JLogger.getInstance();

			// If the first argument is "log4j," use that mode.
			if (args[0].equals("log4j")) {
				logger.selectLog4j();
				
			// else, use the System.out/System.err mode
			} else {
				logger.selectSystem();
			}

			// If the level is specified in the 2nd argument, set it
			if (args.length == 2) {
				logger.setLevel(new Byte(args[1]).byteValue());
			}
		}
		
		// Run the suite!
		junit.textui.TestRunner.run(Habitat4JTestSuite.suite());
	}
}
