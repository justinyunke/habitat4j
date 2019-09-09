package org.productivity.java.habitat4j.junit.threads;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.Habitat4JLogger;
import org.productivity.java.habitat4j.junit.examples.propertybean.SampleLDAPBean;

/**
 * This class extends Thread for use by ApplicationEonProduction1TestCase.
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
 * @version $Id: Habitat4JReloadThread.java,v 1.2 2005/03/23 07:55:46 cvs Exp $
 */
public class Habitat4JReloadThread extends Thread {
	// This Thread's identifying instance number
	private int number = -1;
	
	// Thread that "touches" the PropertyList file to be "picked up" by the reloading check
	// (-1 for none)
	private int touchThread = 50;
	// Flag to log the touchThread for debugging
	private boolean logTouchThread = false;
	
	// Thread that should be logged for debugging (-1 for none)
	private int logThread = -1;
	
	// The iteration modulus that should be logged for debugging
	private int logMark = 1000;	
	
	// The range, between 0 and pauseRange, that will multiply against the Math.random() method
	// to generate a chaotic timing between threads
	private int pauseRange = 50;
	
	// The logger to use
	private Habitat4JLogger logger = null;
	
	// The property file to "reload"
	private String propertyFile = null;	
	
	private boolean keepRunning = true;
	
	/**
	 * Constructor.
	 */
	public Habitat4JReloadThread() {
		super();
		logger = Habitat4JLogger.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#start()
	 */
	public synchronized void start(int number, String propertyFile) {
		this.number = number;
		this.propertyFile = propertyFile;
		super.start();
	}
	
	private void reWriteFile() {
		FileInputStream fis = null;
		byte[] array = new byte[16384];
		int len = 0;
		
		try {
			fis = new FileInputStream(propertyFile);
			len = fis.read(array);
			
		} catch (FileNotFoundException fnfe) {
			//
		} catch (IOException ioe) {
			//
		}

		try {
			FileOutputStream fos = new FileOutputStream(propertyFile);
			fos.write(array,0,len);
		} catch (FileNotFoundException fnfe) {
			//
		} catch (IOException ioe) {
			//
		}		
	}
	
	public void stopRunning() {
		keepRunning = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		super.run();
		
		int i = 0;
		
		while(keepRunning) {
			i += 1;
			
			// Generate a random amount of time to pause
			int pause = (int) (Math.random() * pauseRange);
			
			if (number == touchThread) {
				// Log this thread if so configured
				if (logTouchThread) {
					logger.info("Thread #" + number + " file change");
				}
				
				// "Touch" the file by rewriting it
				reWriteFile();
				
				// Add some time to the pause, since we don't want to do these threads as often
				pause = pause * 10;
				
			} else if (logThread == number) {
				SampleLDAPBean ldap = (SampleLDAPBean) PropertyListManager.getPropertyBean("ldap","login.ldap.server");
				logger.info("Thread #" + number + ":" + i + " " + pause + " " + ldap.getHost());
			}
			
			// Pause for a time
			try {
				sleep(pause);
				
			} catch (InterruptedException ie) {
				//
			}
			
			// Log the mark if the iteration is a modules of the logMark configuration
			if (i % logMark == 0) {
				logger.info("Thread #" + number + " reached " + logMark + " mark (" + i + ")");
			}
		}
	}
}
