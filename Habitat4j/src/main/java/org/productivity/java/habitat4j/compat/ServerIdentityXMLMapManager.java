package org.productivity.java.habitat4j.compat;

import java.util.Hashtable;

import org.productivity.java.habitat4j.common.Habitat4JConstants;

/**
 * ServerIdentityXMLMapManager is a utility for managing multiple ServerIdentityXMLMap
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
 * @version $Id: ServerIdentityXMLMapManager.java,v 1.4 2005/09/03 22:45:22 cvs Exp $
 */
public class ServerIdentityXMLMapManager implements Habitat4JConstants {
	private static ServerIdentityXMLMapManager instance = null;
	
	protected Hashtable serverIdentityXMLMaps = null;
	
	private ServerIdentityXMLMapManager() {
		serverIdentityXMLMaps = new Hashtable();
	}
	
	public static ServerIdentityXMLMapManager getInstance() {
		if (instance == null) {
			instance = new ServerIdentityXMLMapManager();
		}
		
		return instance;
	}
	
	public ServerIdentityXMLMap getServerIdentityXMLMap(String id) {
		if (!serverIdentityXMLMaps.contains(id)) {
			return null;
		}
		
		return (ServerIdentityXMLMap) serverIdentityXMLMaps.get(id);
	}
	
	public void addServerIdentityXMLMap(String id, ServerIdentityXMLMap map) {
		if (id == null || id.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || map == null) {
			return;
		}
		
		serverIdentityXMLMaps.put(id,map);
	}
}
