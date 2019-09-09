package org.productivity.java.habitat4j;

import java.util.Hashtable;

import org.productivity.java.habitat4j.common.AbstractServerIdentityManager;
import org.productivity.java.habitat4j.common.ServerIdentity;
import org.productivity.java.habitat4j.common.ServerIdentityRole;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;
import org.productivity.java.habitat4j.compat.ServerIdentityXMLMap;

/**
 * ServerIdentityManager provides access to a server-wide XML file called
 * an "identity" file.  This class manages the system-wide instance of
 * ServerIdentity, which provides the PropertyListManager with information
 * used to load properties based on the appropriate context.
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
 * @version $Id: ServerIdentityManager.java,v 1.33 2006/08/16 13:32:21 cvs Exp $
 */
public class ServerIdentityManager extends AbstractServerIdentityManager {
	private static ServerIdentityManager instance = null;
	private static String ServerIdentityManagerVersion = "$Id: ServerIdentityManager.java,v 1.33 2006/08/16 13:32:21 cvs Exp $";
	
	/**
	 * This method returns the application name specified when initializing the ServerIdentity instance.
	 * 
	 * @return Returns the application name specified when initializing the ServerIdentity instance.
	 */
	public static String getApplicationName() {
		if (instance == null) return null;
		
		return instance._getApplicationName();
	}

	/**
	 * This method returns the environment specified in the ServerIdentity instance.
	 * 
	 * @return Returns the environment specified in the ServerIdentity instance.
	 */
	public static String getEnvironment() {
		if (instance == null) return null;
		
		return instance._getEnvironment();
	}

	/**
	 * This method returns the instance description specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance description specified in the ServerIdentity instance.
	 */
	public static String getInstanceDescription() {
		if (instance == null) return null;
		
		return instance._getInstanceDescription();
	}

	/**
	 * This method returns the instance enumeration specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance enumeration specified in the ServerIdentity instance.
	 */
	public static String getInstanceEnumeration() {
		if (instance == null) return null;
		
		return instance._getInstanceEnumeration();
	}

	/**
	 * This method returns the instance name specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance name specified in the ServerIdentity instance.
	 */
	public static String getInstanceName() {
		if (instance == null) return null;
		
		return instance._getInstanceName();		
	}

	/**
	 * This method returns the instance operating system specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance operating system specified in the ServerIdentity instance.
	 */
	public static String getInstanceOS() {
		if (instance == null) return null;
		
		return instance._getInstanceOS();		
	}

	/**
	 * @return Returns the ServerIdentity XML file modifiedBy attribute.
	 */
	public static String getModifiedBy() {
		if (instance == null) return null;
		
		return instance._getModifiedBy();
	}

	/**
	 * This method returns the pragma definition in the ServerIdentity instance defined by a specific name.
	 * 
	 * @param name - the name of the pragma definition
	 * @return Returns the pragma definition in the ServerIdentity instance defined by a specific name.
	 */
	public static String getPragmaDefinition(String name) {
		if (instance == null) return null;
		
		return instance._getPragmaDefinition(name);
	}

	/**
	 * This method returns the pragma definition hashtable specified in the ServerIdentity instance.
	 * 
	 * @return Returns the pragma definition hashtable specified in the ServerIdentity instance.
	 */
	public static Hashtable getPragmaDefinitions() {
		if (instance == null) return null;
		
		return instance._getPragmaDefinitions();
	}

	/**
	 * This method returns the ServerIdentityRole in the ServerIdentity instance defined by a specific name.
	 * 
	 * @param name - the name of the role
	 * @return Returns the ServerIdentityRole in the ServerIdentity instance defined by a specific name.
	 */
	public static ServerIdentityRole getRole(String name) {
		if (instance == null) return null;
		
		return instance._getRole(name);
	}

	/**
	 * This method returns the roles hashtable specified in the ServerIdentity instance.
	 * 
	 * @return Returns the roles hashtable specified in the ServerIdentity instance.
	 */
	public static Hashtable getRoles() {
		if (instance == null) return null;
		
		return instance._getRoles();
	}

	/**
	 * This method returns the ServerIdentity object managed by this ServerIdentityManager.
	 * 
	 * @return Returns the ServerIdentity object managed by this ServerIdentityManager.
	 */
	public static ServerIdentity getServerIdentity() {
		if (instance == null) {
			instance = getServerIdentityManager();
		}

		return instance._getServerIdentity();
	}
	
	/**
	 * This method returns this singleton instance of the ServerIdentityManager.
	 * 
	 * @return Returns this singleton instance of the ServerIdentityManager.
	 */
	public static ServerIdentityManager getServerIdentityManager() {
		if (instance == null) {
			instance = new ServerIdentityManager();
		}

		return instance;
	}

	/**
	 * @return Returns the ServerIdentity XML file version attribute.
	 */
	public static String getVersion() {
		if (instance == null) return null;
		
		return instance._getVersion();
	}

	/**
	 * This method returns this singleton instance of the ServerIdentityManager.
	 * 
	 * @param applicationName
	 * @param mode
	 * @return Returns this singleton instance of the ServerIdentityManager.
	 */
	public static ServerIdentityManager initialize(String applicationName,String mode){
		if (instance == null) {
			instance = new ServerIdentityManager();
			instance._initialize(applicationName,mode);
			instance.logger.info("Habitat4J Version " + HABITAT4J_VERSION);
			instance.logger.info("Habitat4J Stock ServerIdentityManager Version " + ServerIdentityManagerVersion + " initialized");
		}
		
		return instance;
	}

	/**
	 * This method returns this singleton instance of the ServerIdentityManager.
	 * 
	 * @param applicationName - the name of the calling application
	 * @return Returns this singleton instance of the ServerIdentityManager.
	 */
	public static ServerIdentityManager initialize(String applicationName){
		return initialize(applicationName,SERVER_IDENTITY_MODE_FILE);
	}

	/**
	 * This method returns whether the ServerIdentityManager has been initialized.
	 * 
	 * @return Returns whether the ServerIdentityManager has been initialized.
	 */
	public static boolean isInitialized() {
		if (instance == null) return false;
		
		return instance._isInitialized();
	}

	/**
	 * This method returns the ServerIdentity object managed by this ServerIdentityManager object.
	 * 
	 * @return Returns the ServerIdentity object managed by this ServerIdentityManager object.
	 * @throws ServerIdentityHandlerException
	 */
	public static ServerIdentity loadServerIdentity() throws ServerIdentityHandlerException {
		if (instance == null) {
			throw new ServerIdentityHandlerException("ServerIdentityManager not initialized");
		}

		return instance._loadServerIdentity();
	}
	
	/**
	 * This method returns the ServerIdentity object managed by this ServerIdentityManager object.
	 * 
	 * @param map - ServerIdentityXMLMap definition to use
	 * @return Returns the ServerIdentity object managed by this ServerIdentityManager object.
	 * @throws ServerIdentityHandlerException
	 */
	public static ServerIdentity loadServerIdentity(ServerIdentityXMLMap map) throws ServerIdentityHandlerException {
		if (instance == null) {
			throw new ServerIdentityHandlerException("ServerIdentityManager not initialized");
		}

		return instance._loadServerIdentity(map);
	}
	
	/**
	 * This method resets this singleton instance of the ServerIdentityManager.  It is implemented
	 * for purposes of JUnit testing, and probably does not serve a practical purpose.
	 */
	public static void reset() {
		if (instance == null) return;

		if (isInitialized()) {	
			instance = null;
		}
	}

	/**
	 * This method sets a Habitat4J feature.
	 * 
	 * @param name - the Habitat4J feature to set
	 * @param value - the Habitat4J feature's value to set
	 * @throws ServerIdentityHandlerException
	 */
	public static void setFeature(String name, boolean value) throws ServerIdentityHandlerException {
		if (instance == null) {
			throw new ServerIdentityHandlerException("ServerIdentityManager not initialized");
		}

		instance._setFeature(name,value);
	}

	/**
	 * This method sets a Habitat4J feature.
	 * 
	 * @param name - the Habitat4J feature to set
	 * @param value - the Habitat4J feature's value to set
	 * @throws ServerIdentityHandlerException
	 */
	public static void setFeature(String name, String value) throws ServerIdentityHandlerException {
		if (instance == null) {
			throw new ServerIdentityHandlerException("ServerIdentityManager not initialized");
		}

		instance._setFeature(name,value);
	}
	
	/**
	 * @param name - the pragma definition name to set
	 * @param value - the pragma definition value to set
	 * @throws ServerIdentityHandlerException
	 */
	public static void setPragmaDefinition(String name, String value) throws ServerIdentityHandlerException {
		if (instance == null) {
			throw new ServerIdentityHandlerException("ServerIdentityManager not initialized");
		}
		
		instance._setPragmaDefinition(name,value);
	}

	/**
	 * This method returns a human-readable presentation of the ServerIdentity object maintained by this singleton ServerIdentityManager object.
	 * 
	 * @return Returns a human-readable presentation of the ServerIdentity object maintained by this singleton ServerIdentityManager object.
	 */
	public static String toDisplayString() {
		return instance._toDisplayString();
	}
	
	private ServerIdentityManager() {
		super();
	}
}
