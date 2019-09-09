package org.productivity.java.habitat4j.compat;

import java.util.Hashtable;

import org.productivity.java.habitat4j.common.Habitat4JConstants;

/**
 * PropertyListXMLMapManager is a utility for managing multiple PropertyListXMLMap
 * definitions.
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
 * @version $Id: PropertyListXMLMapManager.java,v 1.4 2005/09/03 22:45:22 cvs Exp $
 */
public class PropertyListXMLMapManager implements Habitat4JConstants {
	private static PropertyListXMLMapManager instance = null;
	
	protected Hashtable propertyListXMLMaps = null;
	
	private PropertyListXMLMapManager() {
		propertyListXMLMaps = new Hashtable();
	}
	
	public static PropertyListXMLMapManager getInstance() {
		if (instance == null) {
			instance = new PropertyListXMLMapManager();
		}
		
		return instance;
	}
	
	public PropertyListXMLMap getPropertyListXMLMap(String id) {
		if (!propertyListXMLMaps.contains(id)) {
			return null;
		}
		
		return (PropertyListXMLMap) propertyListXMLMaps.get(id);
	}

	public void addPropertyListXMLMap(String id, PropertyListXMLMap map) {
		if (id == null || id.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || map == null) {
			return;
		}
		
		propertyListXMLMaps.put(id,map);
	}
}
