package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleLDAPBean;
import org.productivity.java.habitat4j.junit.threads.Habitat4JReloadThread;

/**
 * This class consists of JUnit tests for deploying Application E on Production Server #1
 * with Habitat4J.  It implements a multi-threaded environment for reloading on an
 * interval *and* On-The-Fly.
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
 * @version $Id: ApplicationEonProduction1TestCase.java,v 1.2 2005/03/23 07:55:45 cvs Exp $
 */
public class ApplicationEonProduction1TestCase extends ApplicationEBaseTestCase {
	private static final String SERVER_NAME			= "servers/production/app-server-1/";
	private final static String propertyFilePath	= DATA_EXAMPLES_FILE_PATH + "apps/" + APPLICATION_NAME + "/" + FILE_MAIN_LDAP;

	public static final String[] TEST_LIST = {
			"testReloadIntervalOnProduction1",
			"testReloadOnTheFlyOnProduction1"
		};
	
	private final static int numberOfThreads	= 100;
	
	private final static int iterationsInterval	= 125;
	private final static int pauseInterval		= 1;
	
	private final static int iterationsOnTheFly	= 30;
	private final static int pauseOnTheFly		= 1;
	
	private Habitat4JReloadThread[] rht = null;

	protected void setUp() throws Exception {
		super.setUp(SERVER_NAME,APPLICATION_NAME);
		
		try {
			PropertyListManager.loadPropertyListFromFile("ldap",propertyFilePath);
			
		} catch (PropertyListHandlerException plhe) {
			logger.error(plhe);
		}

		rht = new Habitat4JReloadThread[numberOfThreads];

		for (int i=0; i<numberOfThreads; i++) {
			rht[i] = new Habitat4JReloadThread();
		}
	}
	
	private static void setReloadMode(boolean onTheFly) {
		try {
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD,"true");
			
			if (onTheFly) {
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY,"true");
			} else {
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY,"false");
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL,1);
			}
			
		} catch (PropertyListHandlerException plhe) {
			//
		}
	}
	
	private void startThreads(Habitat4JReloadThread[] thread) {
		for (int i=0; i<numberOfThreads; i++) {
			logger.info("Started Thread: " + i);
			thread[i].start(i,propertyFilePath);
		}
	}

	private void stopThreads(Habitat4JReloadThread[] thread) {
		for (int i=0; i<numberOfThreads; i++) {
			logger.info("Stopped Thread: " + i);
			thread[i].stopRunning();
		}
	}

	public void testReloadIntervalOnProduction1() {
		logger.info("### Reload on Interval Test ###");
		setReloadMode(false);								

		startThreads(rht);
		for (int i=0; i<iterationsInterval; i++) {
			SampleLDAPBean ldap = (SampleLDAPBean) PropertyListManager.getPropertyBean("ldap","login.ldap.server");
			assertEquals(ldap.getHost(),"ldap-prod-1.bogus.productivity.org");
			pause(pauseInterval);
		}
		
		stopThreads(rht);
	}
	
	public void testReloadOnTheFlyOnProduction1() {
		logger.info("### Reload On the Fly Test ###");
		setReloadMode(true);				
		
		startThreads(rht);			
		for (int i=0; i<iterationsOnTheFly; i++) {
			SampleLDAPBean ldap = (SampleLDAPBean) PropertyListManager.getPropertyBean("ldap","login.ldap.server");
			assertEquals(ldap.getHost(),"ldap-prod-1.bogus.productivity.org");
			pause(pauseOnTheFly);
		}
		stopThreads(rht);
	}

	public ApplicationEonProduction1TestCase(String arg0) {
		super(arg0);
	}	
}
