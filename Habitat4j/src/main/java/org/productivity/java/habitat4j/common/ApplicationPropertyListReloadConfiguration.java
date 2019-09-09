package org.productivity.java.habitat4j.common;

/**
 * This PropertyBean provides reload configuration information
 * for Habitat4JBootstrapServlet and ApplicationPropertyListLoader
 * classes.
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
 * @version $Id: ApplicationPropertyListReloadConfiguration.java,v 1.2 2005/09/21 07:13:38 cvs Exp $
 */
public class ApplicationPropertyListReloadConfiguration {
	protected static final boolean DEFAULT_ENABLED		= false;
	protected static final boolean DEFAULT_ON_THE_FLY	= false;
	protected static final int DEFAULT_INTERVAL			= 60;
	
	protected boolean enabled	= DEFAULT_ENABLED;
	protected boolean onTheFly	= DEFAULT_ON_THE_FLY;
	protected int interval		= DEFAULT_INTERVAL;
	
	/**
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return Returns the interval.
	 */
	public int getInterval() {
		return interval;
	}
	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	/**
	 * @return Returns the onTheFly.
	 */
	public boolean isOnTheFly() {
		return onTheFly;
	}
	/**
	 * @param onTheFly The onTheFly to set.
	 */
	public void setOnTheFly(boolean onTheFly) {
		this.onTheFly = onTheFly;
	}
}
