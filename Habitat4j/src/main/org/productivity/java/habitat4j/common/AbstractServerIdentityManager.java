package org.productivity.java.habitat4j.common;

import java.util.Hashtable;

import org.productivity.java.habitat4j.common.exception.BaseHandlerException;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;
import org.productivity.java.habitat4j.common.sax.ServerIdentityHandler;
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
 * @version $Id: AbstractServerIdentityManager.java,v 1.33 2006/08/16 13:32:21 cvs Exp $
 */
public abstract class AbstractServerIdentityManager implements Habitat4JConstants {
	protected Habitat4JLogger logger = null;

	private Hashtable features = new Hashtable();
	private boolean initializedFlag = false;
	private ServerIdentity serverIdentity = null;
	private boolean loadedFlag = false;
	
	public AbstractServerIdentityManager() {
		super();
		
		logger = Habitat4JLogger.getInstance();
	}

	/**
	 * This method returns the application name specified when initializing the ServerIdentity instance.
	 * 
	 * @return Returns the application name specified when initializing the ServerIdentity instance.
	 */
	protected String _getApplicationName() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getApplicationName();
	}

	/**
	 * This method returns the environment specified in the ServerIdentity instance.
	 * 
	 * @return Returns the environment specified in the ServerIdentity instance.
	 */
	protected String _getEnvironment() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getEnvironment();
	}

	/**
	 * This method returns the instance description specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance description specified in the ServerIdentity instance.
	 */
	protected String _getInstanceDescription() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getInstanceDescription();
	}

	/**
	 * This method returns the instance enumeration specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance enumeration specified in the ServerIdentity instance.
	 */
	protected String _getInstanceEnumeration() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getInstanceEnumeration();
	}

	/**
	 * This method returns the instance name specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance name specified in the ServerIdentity instance.
	 */
	protected String _getInstanceName() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getInstanceName();		
	}

	/**
	 * This method returns the instance operating system specified in the ServerIdentity instance.
	 * 
	 * @return Returns the instance operating system specified in the ServerIdentity instance.
	 */
	protected String _getInstanceOS() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getInstanceOS();		
	}

	/**
	 * @return Returns the ServerIdentity XML file modifiedBy attribute.
	 */
	protected String _getModifiedBy() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getModifiedBy();
	}

	/**
	 * This method returns the pragma definition in the ServerIdentity instance defined by a specific name.
	 * 
	 * @param name - the String name of the pragma definition to retrieve
	 * @return Returns the pragma definition in the ServerIdentity instance defined by a specific name.
	 */
	protected String _getPragmaDefinition(String name) {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getPragmaDefinition(name);
	}

	/**
	 * This method returns the pragma definition hashtable specified in the ServerIdentity instance.
	 * 
	 * @return Returns the pragma definition hashtable specified in the ServerIdentity instance.
	 */
	protected Hashtable _getPragmaDefinitions() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getPragmaDefinitions();
	}

	/**
	 * This method returns the ServerIdentityRole in the ServerIdentity instance defined by a specific name.
	 * 
	 * @param name - the String name of the role to retrieve
	 * @return Returns the ServerIdentityRole in the ServerIdentity instance defined by a specific name.
	 */
	protected ServerIdentityRole _getRole(String name) {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getRole(name);
	}

	/**
	 * This method returns the roles hashtable specified in the ServerIdentity instance.
	 * 
	 * @return Returns the roles hashtable specified in the ServerIdentity instance.
	 */
	protected Hashtable _getRoles() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getRoles();
	}

	/**
	 * This method returns the instance of ServerIdentity.
	 * 
	 * @return Returns the ServerIdentity instance.
	 */
	protected ServerIdentity _getServerIdentity() {
		if (!_isInitialized()) {
			return null;
		}	

		return serverIdentity;
	}	

	/**
	 * @return Returns the ServerIdentity XML file version attribute.
	 */
	protected String _getVersion() {
		if (!_isInitialized() || serverIdentity == null) return null;
		
		return serverIdentity.getVersion();
	}
	
	/**
	 * This method initializes this Singleton class with an instance of ServerIdentity. 
	 * 
	 * @param mode - the ServerIdentity mode ("N" "F" or "J")
	 * @param applicationName the name of the calling application
	 */
	protected void _initialize(String applicationName, String mode) {
		String _mode = SERVER_IDENTITY_MODE_FILE;
		if (mode != null) {
			if (mode.equals(SERVER_IDENTITY_MODE_JVM)) {
				_mode = SERVER_IDENTITY_MODE_JVM;
				
			} else if (mode.equals(SERVER_IDENTITY_MODE_NULL)) {
				_mode = SERVER_IDENTITY_MODE_NULL;				
			}
		}
		features.put(Habitat4JFeatures.SERVER_IDENTITY_FEATURE_MODE,_mode);
		
		ServerIdentity _serverIdentity = new ServerIdentity();
		_serverIdentity.setApplicationName(applicationName);
		setServerIdentity(_serverIdentity);
	}
	
	/**
	 * This method returns the status of initialization of this class.
	 * 
	 * @return Returns the status of the initialization of this class.
	 */
	protected boolean _isInitialized() {
		return initializedFlag;
	}

	/**
	 * @param name
	 * @param value
	 * @throws ServerIdentityHandlerException
	 */
	protected void _setFeature(String name, boolean value) throws ServerIdentityHandlerException {
		String _value = value ? HABITAT4J_GENERIC_VALUE_TRUE : HABITAT4J_GENERIC_VALUE_FALSE;

		_setFeature(name,_value);
	}

	/**
	 * @param name
	 * @param value
	 * @throws ServerIdentityHandlerException
	 */
	protected void _setFeature(String name, String value) throws ServerIdentityHandlerException {
		if (name == null || name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			throw new ServerIdentityHandlerException(HABITAT4J_GENERIC_VALUE_EMPTY_STRING);
		}
		
		if (Habitat4JFeatures.SERVER_IDENTITY_FEATURES.contains(name)) {
			features.put(name,value);
			
		} else {
			throw new ServerIdentityHandlerException("Invalid feature: " + name);						
		}
	}

	/**
	 * This method uses a throw-away instance of the ServerIdentityHandler
	 * to populates it with data from the "identity" XML file.
	 * 
	 * @param map
	 * @throws ServerIdentityHandlerException
	 * @return Returns the ServerIdentity instance
	 */
	protected synchronized ServerIdentity _loadServerIdentity(ServerIdentityXMLMap map) throws ServerIdentityHandlerException {
		String logMethodName = this.getClass().getName() + "._loadServerIdentity(String applicationName - ";
		
		if (loadedFlag) {
			if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.SERVER_IDENTITY_FEATURE_CAN_BE_RELOADED)) {
				serverIdentity.reset();
				
			} else {
				logger.debug("ServerIdentity cannot be reloaded without the \"" + Habitat4JFeatures.SERVER_IDENTITY_FEATURE_CAN_BE_RELOADED + "\" feature set to \"true\"");
				return serverIdentity;
			}
		}
		
		String mode = (String) features.get(Habitat4JFeatures.SERVER_IDENTITY_FEATURE_MODE);
		
		if (mode.equals(SERVER_IDENTITY_MODE_FILE)) {
			_loadServerIdentityViaFile(map);
			
		} else if (mode.equals(SERVER_IDENTITY_MODE_JVM)) {
			_loadServerIdentityViaJVM();

		} else if (mode.equals(SERVER_IDENTITY_MODE_NULL)) {
			logger.info(logMethodName + "ServerIdentity loaded successfully, no context defined");
		}
		
		initializedFlag = true;
		loadedFlag = true;

		setServerIdentity(serverIdentity);
		return serverIdentity;
	}

	protected ServerIdentity _loadServerIdentity() throws ServerIdentityHandlerException {
		return _loadServerIdentity(null);
	}

	/**
	 * @param map
	 * @throws ServerIdentityHandlerException
	 */
	protected void _loadServerIdentityViaFile(ServerIdentityXMLMap map) throws ServerIdentityHandlerException {
		try {
			new ServerIdentityHandler(serverIdentity,features,map);
			
		} catch (BaseHandlerException bhe) {
			throw new ServerIdentityHandlerException(bhe);			
		}		
	}

	/**
	 * @throws ServerIdentityHandlerException
	 */
	protected void _loadServerIdentityViaFile() throws ServerIdentityHandlerException {
		_loadServerIdentityViaFile(null);
	}

	/**
	 */
	protected void _loadServerIdentityViaJVM() {
		String logMethodName = this.getClass().getName() + " loadServerIdentityViaJVM() - ";
		
		String value = null;
		
		value = System.getProperty(SERVER_IDENTITY_MODE_JVM_ENVIRONMENT);
		if (value != null) serverIdentity.setEnvironment(value);

		value = System.getProperty(SERVER_IDENTITY_MODE_JVM_INSTANCE_NAME);
		if (value != null) serverIdentity.setInstanceName(value);

		value = System.getProperty(SERVER_IDENTITY_MODE_JVM_INSTANCE_ENUMERATION);
		if (value != null) serverIdentity.setInstanceEnumeration(value);

		value = System.getProperty(SERVER_IDENTITY_MODE_JVM_INSTANCE_DESCRIPTION);
		if (value != null) serverIdentity.setInstanceDescription(value);
		
		logger.info(logMethodName + "ServerIdentity loaded successfully from JVM properties");
	}

	/**
	 * This private method sets the instance of serverIdentity.  It is called
	 * by the _loadServerIdentity(..) method.
	 * 
	 * @param serverIdentity Sets the instance of ServerIdentity; only called within initializeHandler(...)
	 */
	private void setServerIdentity(ServerIdentity serverIdentity) {
		this.serverIdentity = serverIdentity;
	}
	
	/**
	 * @param name - the pragma definition name to set
	 * @param value - the pragma definition value to set
	 * @throws ServerIdentityHandlerException
	 */
	protected void _setPragmaDefinition(String name, String value) throws ServerIdentityHandlerException{
		if (!_isInitialized() || serverIdentity == null) {
			throw new ServerIdentityHandlerException("Cannot setPragmaDefinition(..) when ServerIdentity has not been initialized");
		}
		
		serverIdentity.setPragmaDefinition(name,value);
	}
	
	/**
	 * This method returns a human-readable dump of the ServerIdentity instance.
	 * 
	 * @return Returns a human-readable dump of the ServerIdentity instance in the form of a String.
	 */
	protected String _toDisplayString() {
		if (!_isInitialized()) {
			return null;
		}	

		return serverIdentity.toString();
	}
}
