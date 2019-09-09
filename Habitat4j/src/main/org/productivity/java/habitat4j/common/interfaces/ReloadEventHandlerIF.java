package org.productivity.java.habitat4j.common.interfaces;

import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;

/**
 * Interface used to implement the preReload, postReload, and onReloadFailure event
 * methods.  In a class that implements this interface:
 * 
 * If preReload(..) returns true, the reload will continue.  If false,
 * the reload will be interrupted and old properties will remain
 * available to the application.
 * 
 * If postReload(..) returns true, the reload will continue.  If false,
 * the reload will be interrupted and old properties will remain
 * available to the application.
 * 
 * onReloadFailure can be used to take action upon a failure.  
 * 
 *  <p>## LICENSE INFORMATION ##</p>
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
 * @version $Id: ReloadEventHandlerIF.java,v 1.3 2004/08/30 00:27:29 cvs Exp $
 */
public interface ReloadEventHandlerIF {
	/**
	 * @param oldPropertyList - the PropertyList instance available prior to the reload
	 * @return Returns "true" if the reload process should continue, else "false"
	 */
	public boolean preReload(PropertyList oldPropertyList);
	
	/**
	 * @param oldPropertyList - the PropertyList instance available prior to the reload
	 * @param newPropertyList - the PropertyList instance available after reload
	 * @return Returns "true" if the reload process should continue, else "false"
	 */
	public boolean postReload(PropertyList oldPropertyList,PropertyList newPropertyList);
	
	/**
	 * @param oldPropertyList - the PropertyList instance available prior to the reload
	 * @param exception - the PropertyListHandlerException instance that will be thrown by Habitat4J
	 */
	public void onReloadFailure(PropertyList oldPropertyList,PropertyListHandlerException exception);
}
