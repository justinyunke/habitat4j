package org.productivity.java.habitat4j.common;

import java.util.Enumeration;
import java.util.Hashtable;

import org.productivity.java.habitat4j.util.InstanceOsHelper;

/**
 * This class provides server-wide information used by all applications.
 * An instance of this is managed by the ServerIdentityManager Singleton
 * class.  PropertyListManager relies on an instance of ServerIdentity
 * to make contextual decisions on which properties to load for use by
 * the application.
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
 * @version $Id: ServerIdentity.java,v 1.22 2006/08/16 13:32:21 cvs Exp $
 */
public class ServerIdentity implements Habitat4JConstants {
	private Hashtable fixed = null;
	private Hashtable roles = null;
	private Hashtable pragma = null;
	
	private String version = null;
	private String modifiedBy = null;
	
	public ServerIdentity() {
		String instanceOS = InstanceOsHelper.getInstanceOS();
		setInstanceOS(instanceOS);
	}

	/**
	 * @return Returns the name of the calling application.
	 */
	public String getApplicationName() {
		if (fixed == null) { fixed = new Hashtable(); }

		return (String) fixed.get(HABITAT4J_CONTEXT_APPLICATION_NAME);
	}
	
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @return Returns the modifiedBy.
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	/**
	 * @return Returns the server's environment designation.
	 */
	public String getEnvironment() {
		if (fixed == null) { fixed = new Hashtable(); }

		return (String) fixed.get(HABITAT4J_CONTEXT_ENVIRONMENT);
	}
	/**
	 * @param environment the server's environment designation to set
	 */
	public void setEnvironment(String environment) {
		if (fixed == null) { fixed = new Hashtable(); }

		fixed.put(HABITAT4J_CONTEXT_ENVIRONMENT,environment);
	}
	/**
	 * @return Returns the description of the server's instance.
	 */
	public String getInstanceDescription() {
		if (fixed == null) { fixed = new Hashtable(); }
		
		return (String) fixed.get(HABITAT4J_CONTEXT_INSTANCE_DESCRIPTION);
	}
	/**
	 * @param instanceDescription the server's instance description to set
	 */
	public void setInstanceDescription(String instanceDescription) {
		if (fixed == null) { fixed = new Hashtable(); }

		fixed.put(HABITAT4J_CONTEXT_INSTANCE_DESCRIPTION,instanceDescription);
	}
	/**
	 * @return Returns the server's instance enumeration.
	 */
	public String getInstanceEnumeration() {
		if (fixed == null) { fixed = new Hashtable(); }
		
		return (String) fixed.get(HABITAT4J_CONTEXT_INSTANCE_ENUMERATION);
	}
	/**
	 * @param instanceEnumeration the server's instance enumeration to set
	 */
	public void setInstanceEnumeration(String instanceEnumeration) {
		if (fixed == null) { fixed = new Hashtable(); }

		fixed.put(HABITAT4J_CONTEXT_INSTANCE_ENUMERATION,instanceEnumeration);
	}
	/**
	 * @return Returns the server's instance name.
	 */
	public String getInstanceName() {
		if (fixed == null) { fixed = new Hashtable(); }

		return (String) fixed.get(HABITAT4J_CONTEXT_INSTANCE_NAME);
	}
	/**
	 * @param instanceName the server's instance operating system to set
	 */
	public void setInstanceOS(String instanceOS) {
		if (fixed == null) { fixed = new Hashtable(); }

		fixed.put(HABITAT4J_CONTEXT_INSTANCE_OS,instanceOS);
	}
	/**
	 * @return Returns the server's instance operating system.
	 */
	public String getInstanceOS() {
		if (fixed == null) { fixed = new Hashtable(); }

		return (String) fixed.get(HABITAT4J_CONTEXT_INSTANCE_OS);
	}
	/**
	 * @param instanceName the server's instance name to set
	 */
	public void setInstanceName(String instanceName) {
		if (fixed == null) { fixed = new Hashtable(); }

		fixed.put(HABITAT4J_CONTEXT_INSTANCE_NAME,instanceName);
	}
	/**
	 * @return Returns where there are pragmatic definitions available.
	 */
	public boolean hasPragmaDefinitions() {
		if (pragma == null || pragma.size() < 1) {
			return false;
		}

		return true;
	}
	/**
	 * @return Returns the list of pragmatic definitions in the form of a Hashtable.
	 */
	public Hashtable getPragmaDefinitions() {
		if (pragma == null) { pragma = new Hashtable(); }

		return pragma;
	}
	/**
	 * @param name - the String name of the pragma definition to retrieve
	 * @return Returns the pragmatic definition defined by a specific name.
	 */
	public String getPragmaDefinition(String name) {
		if (pragma == null) { pragma = new Hashtable(); }
		
		return (String) pragma.get(name);
	}
	/**
	 * @param name - the String name of the pragma definition to set
	 * @param value - the String value of the pragma definition to set
	 */
	public void setPragmaDefinition(String name, String value) {
		if (pragma == null) { pragma = new Hashtable(); }
		
		pragma.put(name,value);
	}	
	/**
	 * @return Returns the Hashtable list of roles.
	 */
	public Hashtable getRoles() {
		if (roles == null) { roles = new Hashtable(); }

		return roles;
	}
	
	/**
	 * This method adds a ServerIdentityRole to a ServerIdentity instance.  If the role has
	 * already been defined, the specific instance is returned to be overridden.
	 * 
	 * @param name the role name to add, such as "dbServer," "appServer," or "webServer"
	 * @return Returns an instance of ServerIdentityRole
	 */
	public ServerIdentityRole addRole(String name) {
		if (roles == null) { roles = new Hashtable(); }

		ServerIdentityRole role = (ServerIdentityRole) roles.get(name);

		if (role == null) {
			role = new ServerIdentityRole();
			role.setName(name);
			roles.put(name,role);
		}
		
		return role;
	}

	/**
	 * @param name the role name to get, such as "dbServer," "appServer," or "webServer"
	 * @return Returns an instance of ServerIdentityRole
	 */
	public ServerIdentityRole getRole(String name) {
		if (roles == null) { roles = new Hashtable(); }
		
		ServerIdentityRole role = (ServerIdentityRole) roles.get(name);
		
		return role;
	}
	
	/**
	 * @param applicationName the name of the calling application to be set
	 */
	public void setApplicationName(String applicationName) {
		if (fixed == null) { fixed = new Hashtable(); }

		if (getApplicationName() == null) {
			fixed.put(HABITAT4J_CONTEXT_APPLICATION_NAME,applicationName);
		}
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @param modifiedBy The modifiedBy to set.
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return Returns the fixed items, such as applicationName, environment, etc.
	 */
	public Hashtable getFixed() {
		if (fixed == null) { fixed = new Hashtable(); }
		
		return fixed;
	}
	
	/**
	 * This method is used by toDisplayString() to present a name/value pair
	 * from a specific table.
	 * 
	 * @param table the Hashtable from which to retrieve "name"
	 * @param name the variable name to retrieve
	 * @return Returns an equal-delimited name/value pair
	 */
	private String getToStringNameValuePair(Hashtable table, String name) {
		return name + "=" + table.get(name);
	}
	
	/**
	 * This method resets all of the contents of this object.
	 */
	public void reset() {
		fixed = null;
		roles = null;
		pragma = null;
		
		version = null;
		modifiedBy = null;		
	}
	
	/**
	 * This method provides a human-readable dump of all variables that make up an
	 * instance of ServerIdentity.
	 * 
	 * @return Returns a human-readable dump of all variables in the form of a String.
	 */
	public String toString() {
		StringBuffer response = new StringBuffer();
		
		response.append("### FIXED DEFINITIONS ###" + EOL_SEQUENCE);
		Enumeration fixedEnum = fixed.keys();
		while (fixedEnum.hasMoreElements()) {
			String name = (String) fixedEnum.nextElement();
			response.append(getToStringNameValuePair(fixed,name));
			response.append(EOL_SEQUENCE);
		}
		
		if (roles == null || roles.isEmpty()) {
			response.append(EOL_SEQUENCE + "### ROLE DEFINITIONS: [none] ###" + EOL_SEQUENCE);
			
		} else {
			Enumeration rolesEnum = roles.keys();
			while (rolesEnum.hasMoreElements()) {
				String name = (String) rolesEnum.nextElement();

				ServerIdentityRole serverRole = (ServerIdentityRole) roles.get(name);
				response.append(EOL_SEQUENCE);
				response.append(serverRole.toString());
			}
		}

		response.append(EOL_SEQUENCE);
		if (pragma == null || pragma.isEmpty()) {
			response.append("### PRAGMA DEFINITIONS: [none] ###" + EOL_SEQUENCE);
			
		} else {
			response.append("### PRAGMA DEFINITIONS ###" + EOL_SEQUENCE);
			Enumeration pragmaEnum = pragma.keys();
			while (pragmaEnum.hasMoreElements()) {
				String name = (String) pragmaEnum.nextElement();
				response.append(getToStringNameValuePair(pragma,name));
				response.append(EOL_SEQUENCE);
			}
		}

		return response.toString();
	}
}
