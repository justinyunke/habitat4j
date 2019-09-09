package org.productivity.java.habitat4j.junit;

import org.productivity.java.habitat4j.PropertyListReloadInhibitor;

import junit.framework.TestCase;

/**
 * PropertyListReloadInhibitorTestCase rudimentarily tests the PropertyListReloadInhibitor.
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
 * @version $Id: PropertyListReloadInhibitorTestCase.java,v 1.1 2005/03/23 07:55:45 cvs Exp $
 */
public class PropertyListReloadInhibitorTestCase extends TestCase {
	public void testReloadInhibitor() {
		System.out.println("testReloadInhibitor()");
		
		PropertyListReloadInhibitor reloadInhibitor = PropertyListReloadInhibitor.getInstance();
		
		for (int i=1; i < 11; i++) {			
			if (i == 5) {
				reloadInhibitor.enable();
			}

			if (i == 7) {
				reloadInhibitor.disable();
			}
			
			System.out.println("Test: " + i + " Inhibitor Enabled: " + new Boolean(reloadInhibitor.isReloadInhibited()));

			try {
				Thread.sleep(100);
				
			} catch (InterruptedException ie) {
				//
			}
		}
	}

	public void testReloadInhibitorWithExpiration() {
		System.out.println("testReloadInhibitorWithExpiration()");

		PropertyListReloadInhibitor reloadInhibitor = PropertyListReloadInhibitor.getInstance();
		
		for (int i=1; i < 11; i++) {			
			if (i == 5) {
				reloadInhibitor.enable(300);
			}

			System.out.println("Test: " + i + " Inhibitor Enabled: " + new Boolean(reloadInhibitor.isReloadInhibited()));

			try {
				Thread.sleep(100);
				
			} catch (InterruptedException ie) {
				//
			}
			
		}
	}
}
