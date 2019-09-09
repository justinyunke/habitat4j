package org.productivity.java.habitat4j;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * PropertyListReloadInhibitor provides a facility to globally inhibit
 * reloading of PropertyList files.  It is primarily used when a block
 * of code obtaining a group of properties is sensitive to property changes
 * that may change some of the values and badly affect others in that
 * group.
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
 * @version $Id: PropertyListReloadInhibitor.java,v 1.2 2005/03/26 07:52:08 cvs Exp $
 */
public class PropertyListReloadInhibitor {
	// The Singleton instance
	private static PropertyListReloadInhibitor instance = null;

	// A value of -1 means "no expiration"
	private final static long RELOAD_INHIBITOR_NO_EXPIRATION_VALUE = -1;

	// The Hashtable instance containing reload inhibition information
	protected Hashtable reloadHash = null;

	
	/**
	 * The Singleton private constructor. 
	 */
	private PropertyListReloadInhibitor() {
		// No initialization is needed
	}
	
	/**
	 * @return Returns the Singleton instance of this class.
	 */
	public static PropertyListReloadInhibitor getInstance() {
		if (instance == null) {
			instance = new PropertyListReloadInhibitor();
		}
		
		return instance;
	}
	
	/**
	 * @return Returns the Integer value of the current Thread's hashCode().
	 */
	protected Integer getCurrentThreadHash() {
		return new Integer(Thread.currentThread().hashCode());
	}

	/**
	 * Enables the reload inhibitor.  This will stop Habitat4J from reloading PropertyList files.
	 * 
	 * @param expiration - time in milliseconds for the calling Thread to inhibit reloading
	 */
	public void enable(long expiration) {
		// Get the current Thread's Integer value of hashCode()
		Integer reloadHashKey = getCurrentThreadHash(); 
		
		Long hashedExpiration = null;

		// If the "never expires" flag is set, create a Long of it
		if (expiration == RELOAD_INHIBITOR_NO_EXPIRATION_VALUE) {
			hashedExpiration = new Long(RELOAD_INHIBITOR_NO_EXPIRATION_VALUE);

		// Else, set the value to the current time added to the specified expiration
		} else {
			hashedExpiration = new Long(System.currentTimeMillis() + expiration);
		}
		
		synchronized (instance) {
			// If it hasn't already created an instance of reloadHash, do it
			if (reloadHash == null) {
				reloadHash = new Hashtable();
			}
			
			// Add the value to the reloadHash
			reloadHash.put(reloadHashKey,hashedExpiration);
		}
	}
		
	/**
	 * Enables the reload inhibitor.  This will stop Habitat4J from reloading PropertyList files.
	 */
	public void enable() {
		// By default, pass in the "never expires" value (-1)
		enable(RELOAD_INHIBITOR_NO_EXPIRATION_VALUE);
	}
	
	/**
	 * Disables the reload inhibitor.  This will allow Habitat4J to reload PropertyList files.
	 */
	public void disable() {
		if (reloadHash == null) {
			return;
		}
		
		synchronized (instance) {
			// Get the current thread's Integer hashCode()
			Integer reloadHashKey = getCurrentThreadHash(); 
		
			// If the reloadHash contains it, go ahead and remove it
			if (reloadHash.containsKey(reloadHashKey)) {
				reloadHash.remove(reloadHashKey);
			}
		}
	}
	
	/**
	 * Checks for and removes stale entries in the reloadHash Hashtable.
	 * Does not need to be synchronized, since the only calling method (isReloadInhibited)
	 * has a synchronize block within it.
	 */
	protected void removeAnyStaleEntries() {
		// Grab the keys from reloadHash
		Enumeration enumeration = reloadHash.keys();

		// Loop through all of the entries in the reloadHash
		while (enumeration.hasMoreElements()) {
			Integer reloadHashKey = (Integer) enumeration.nextElement();
			Long hashedExpiration = (Long) reloadHash.get(reloadHashKey);
			
			// If the "never expires" value isn't set, then...
			if (hashedExpiration.longValue() != RELOAD_INHIBITOR_NO_EXPIRATION_VALUE) {
				
				// Check to see if the value has expired, and...
				if (System.currentTimeMillis() >= hashedExpiration.longValue()) {
					
					// Remove the entry
					reloadHash.remove(reloadHashKey);
				}
			}
		}
	}
	
	/**
	 * @return Returns whether reload capabilities within Habitat4J are inhibited
	 */
	public boolean isReloadInhibited() {
		if (reloadHash == null) {
			return false;
		}
		
		synchronized (instance) {
			// Clean up any stale entries relating to this thread
			removeAnyStaleEntries();
		
			// If there are any entries in the ReloadHash (not isEmpty), then reloading will be inhibited
			return !reloadHash.isEmpty();
		}
	}
}
