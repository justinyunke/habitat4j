package org.productivity.java.habitat4j.junit.examples.reload;

import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF;

/**
 * Sample class used to implement the reload preExecute and postExecute processes.
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
 * @version $Id: SampleReloadEventHandler.java,v 1.2 2004/08/30 00:27:29 cvs Exp $
 */
public class SampleReloadEventHandler implements ReloadEventHandlerIF {
	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF#preExecute(org.productivity.java.habitat4j.common.PropertyList)
	 */
	public boolean preReload(PropertyList oldPropertyList) {
		return true;
	}
	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF#postExecute(org.productivity.java.habitat4j.common.PropertyList)
	 */
	public boolean postReload(PropertyList oldPropertyList,PropertyList newPropertyList) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF#onFailure(org.productivity.java.habitat4j.common.PropertyList, org.productivity.java.habitat4j.common.exception.PropertyListHandlerException)
	 */
	public void onReloadFailure(PropertyList oldPropertyList,PropertyListHandlerException exception) {
		return;
	}
}
