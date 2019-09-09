package org.productivity.java.habitat4j.common;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class provides ServerIdentity with one or more roles that can
 * be used later by PropertyListManager to make decisions on which properties
 * to load.  A ServerIdentityRole contains information on basic "role" information -
 * database server, application server, web server - as well as other
 * vendor-specific information such as product name, version, and patch level.
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
 * @version $Id: ServerIdentityRole.java,v 1.3 2005/03/23 07:55:43 cvs Exp $
 */
public class ServerIdentityRole extends Hashtable implements Habitat4JConstants {
	private static final long serialVersionUID = 3256723991824643897L;

	/**
	 * @return Returns the role name, which is required to be one of the strings in Constants.HABITAT4J_CONTEXT_LEGAL_ROLE_NAMES.
	 * @see Habitat4JConstants
	 */
	public String getName() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_NAME);
	}
	
	/**
	 * @return Returns the role type, which is typical something like "MySQL," "J2EE," or "HTTP."
	 */
	public String getType() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_TYPE);
	}

	/**
	 * @return Returns the patch level.
	 */
	public String getPatchLevel() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_PATCH_LEVEL);
	}
	
	/**
	 * @return Returns the product name.
	 */
	public String getProduct() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_PRODUCT);
	}
	
	/**
	 * @return Returns the vendor name.
	 */
	public String getVendor() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_VENDOR);
	}
	
	/**
	 * @return Returns the production version.
	 */
	public String getVersion() {
		return (String) get(HABITAT4J_CONTEXT_ROLE_VERSION);
	}
	
	/**
	 * This method clears all variables within this class.
	 */
	public void clear() {
		super.clear();
	}
	
	/**
	 * @param name the role name to set, which is required to be one of the strings in Constants.HABITAT4J_CONTEXT_LEGAL_ROLE_NAMES
	 */
	public void setName(String name) {
		put(HABITAT4J_CONTEXT_ROLE_NAME,name);
	}
	
	/**
	 * @param type the role type to set, which is typical something like "MySQL," "J2EE," or "HTTP"
	 */
	public void setType(String type) {
		put(HABITAT4J_CONTEXT_ROLE_TYPE,type);
	}

	/**
	 * @param product the product name to set
	 */
	public void setProduct(String product) {
		put(HABITAT4J_CONTEXT_ROLE_PRODUCT,product);
	}
	
	/**
	 * @param vendor the vendor name to set
	 */
	public void setVendor(String vendor) {
		put(HABITAT4J_CONTEXT_ROLE_VENDOR,vendor);
	}
	
	/**
	 * @param version the product version to set
	 */
	public void setVersion(String version) {
		put(HABITAT4J_CONTEXT_ROLE_VERSION,version);
	}
	
	/**
	 * @param patchLevel the product patch level to set
	 */
	public void setPatchLevel(String patchLevel) {
		put(HABITAT4J_CONTEXT_ROLE_PATCH_LEVEL,patchLevel);
	}
	
	/**
	 * @param name the role variable name to retrieve
	 * @return Returns an equal-sign delimited pair for presentation purposes.
	 */
	private String getDisplayStringNameValuePair(String name) {
		return name + "=" + get(name);
	}
	
	/**
	 * This method provides a human-readable dump of all role information.
	 * 
	 * @return Returns a human-readable dump of all role information in the form of a String.
	 */
	public String toString() {
		StringBuffer response = new StringBuffer();
		
		response.append("### ROLE DEFINITION: " + getName() + " ###" + EOL_SEQUENCE);
		Enumeration fixedEnum = keys();
		while (fixedEnum.hasMoreElements()) {
			String name = (String) fixedEnum.nextElement();
			response.append(getDisplayStringNameValuePair(name));
			response.append(EOL_SEQUENCE);
		}
		
		return response.toString();
	}
}
