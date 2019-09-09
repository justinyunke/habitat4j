package org.productivity.java.habitat4j.common.sax;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.ServerIdentity;
import org.productivity.java.habitat4j.common.ServerIdentityRole;
import org.productivity.java.habitat4j.common.exception.BaseHandlerException;
import org.productivity.java.habitat4j.common.exception.ServerIdentityFileNotFoundException;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;
import org.productivity.java.habitat4j.compat.ServerIdentityXMLMap;
import org.xml.sax.Attributes;

/**
 * Class used for handling the parsing of ServerIdentity XML files.
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
 * @version $Id: ServerIdentityHandler.java,v 1.35 2005/09/03 22:45:22 cvs Exp $
 */
public class ServerIdentityHandler extends AbstractBaseHandler {
	private ServerIdentity serverIdentity		= null;
	
	private StringBuffer currentApplicationName	= new StringBuffer(HABITAT4J_APPNAME_DEFAULT);
	private ServerIdentityRole currentRole 		= null;
	private StringBuffer currentPragmaName		= new StringBuffer();

	private Hashtable features					= null;
	
	private ServerIdentityHandler() throws BaseHandlerException {
		super();
	}
	
	private boolean pragmaValueSetInAttributeFlag = false;

	private static HashSet contextRoleNamesHash = new HashSet();
	
	/**
	 * @param serverIdentity
	 * @param features
	 * @param map
	 * @throws ServerIdentityHandlerException
	 * @throws BaseHandlerException
	 */
	public ServerIdentityHandler(ServerIdentity serverIdentity, Hashtable features, ServerIdentityXMLMap map) throws ServerIdentityHandlerException, BaseHandlerException {
		super();
		
		this.features = features;
		this.map = map;

		String xsdValidationPath = getXsdValidationPath(features,Habitat4JFeatures.SERVER_IDENTITY_FEATURE_XSD_VALIDATION,HABITAT4J_SERVER_IDENTITY_XSD_RESOURCE_PATH_DEFAULT);
		initialize(serverIdentity,xsdValidationPath);
	}	
	
	/**
	 * This method goes through the provided serverIdentityPath (which can be one path or
	 * a semicolon-delimited list of path entries) in linear order, checks to see that the
	 * path exists, and returns said path.  If no entries are found, a ServerIdentityFileNotFoundException
	 * is thrown.
	 * 
	 * @param serverIdentityPath - An optionally semicolon-delimited list of paths to a server-identity.xml file.
	 * @return Returns a String containing a valid path to a server-identity.xml file.
	 * @throws ServerIdentityFileNotFoundException
	 */
	private static String getValidServerIdentityPath(String serverIdentityPath) throws ServerIdentityFileNotFoundException {
		String logMethodName = ServerIdentityHandler.class.getName() + ".getValidServerIdentityPath(..) - ";

		StringTokenizer tokenizer = new StringTokenizer(serverIdentityPath,";");
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			File file = new File(token);
			if (file.exists()) {
				return token;
			}
		}
		
		throw new ServerIdentityFileNotFoundException(logMethodName + "No server-identity.xml file available in provided path (" + serverIdentityPath + ")");
	}

	/**
	 * @param serverIdentity
	 * @param xsdResourcePath
	 * @throws ServerIdentityHandlerException
	 * @throws BaseHandlerException
	 */
	private void initialize(ServerIdentity serverIdentity, String xsdResourcePath) throws ServerIdentityHandlerException, BaseHandlerException {
		String logMethodName = this.getClass().getName() + " constructor - ";

		this.serverIdentity = serverIdentity;

		// Attempt to read the server-identity.xml path String from the System property, else use thedefault
		String identityFilePath = System.getProperty(HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME);
		if (identityFilePath == null || identityFilePath.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			identityFilePath = HABITAT4J_SERVER_IDENTITY_FILE_PATH_DEFAULT;
			
			logger.debug(
				logMethodName + "ServerIdentity file - Location not found in System.getProperty(\"" +
				HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME + "\"), using hardcoded default: " +
				identityFilePath
			);
		}
		
		// Obtain the server-identity.xml file location
		identityFilePath = getValidServerIdentityPath(identityFilePath);
		
		try {
			if (xsdResourcePath != null) {
				parse(identityFilePath,xsdResourcePath);
					
			} else {
				parse(identityFilePath);
			}
			
			logger.info(logMethodName + "ServerIdentity loaded successfully from file");
			
		} catch (BaseHandlerException xmlhe) {
			throw new ServerIdentityHandlerException(logMethodName + "ServerIdentity NOT loaded from file: " + xmlhe);
		}
	}

	/**
	 * 
	 */
	public void initServerIdentityApplication() {
		setCurrentApplicationName(HABITAT4J_APPNAME_DEFAULT);		
	}

	/**
	 * @param atts
	 */
	public void setServerIdentity(Attributes atts) {
		String _version = atts.getValue("version");
		if (_version != null && !_version.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			serverIdentity.setVersion(_version);
		}
		
		String _modifiedBy = atts.getValue("modifiedBy");
		if (_modifiedBy != null && !_modifiedBy.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			serverIdentity.setModifiedBy(_modifiedBy);
		}
	}

	/**
	 * @param atts
	 */
	public void setServerIdentityApplication(Attributes atts) {
		String name = atts.getValue(atts.getIndex(HABITAT4J_APPNAME_ATTRIBUTE_NAME));
		if (name == null || name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			name = HABITAT4J_APPNAME_DEFAULT;
		}
		
		setCurrentApplicationName(name);		
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationEnvironment(String value) {
		if (isCurrentApplication()) {
			serverIdentity.setEnvironment(value);
		}
	}
	
	/**
	 * @param value
	 */
	public void setServerIdentityApplicationInstanceName(String value) {
		if (isCurrentApplication()) {
			serverIdentity.setInstanceName(value);
		}
	}
	
	/**
	 * @param value
	 */
	public void setServerIdentityApplicationInstanceDescription(String value) {
		if (isCurrentApplication()) {
			serverIdentity.setInstanceDescription(value);
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationInstanceEnumeration(String value) {
		if (isCurrentApplication()) {
			serverIdentity.setInstanceEnumeration(value);		
		}
	}

	/**
	 * 
	 */
	public void initServerIdentityApplicationRolesRole() {
		currentRole = null;		
	}

	/**
	 * @param atts
	 */
	public void setServerIdentityApplicationRolesRole(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setServerIdentityApplicationRolesRole(Attributes atts) ";
		
		if (isCurrentApplication()) {
			String value = atts.getValue(HABITAT4J_ROLE_ATTRIBUTE_NAME);
			
			if (value != null) {
				if (contextRoleNamesHash.size()==0) {
					setContextRoleNames(HABITAT4J_CONTEXT_LEGAL_ROLE_NAMES);
				}
				
				if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.SERVER_IDENTITY_FEATURE_LAX_ROLENAMES)
						|| contextRoleNamesHash.contains(value)) {
					currentRole = serverIdentity.addRole(value);
					
				} else {
					logger.warn(logMethodName + "Skipped invalid role name: " + value);					
				}
			}
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationRolesRoleType(String value) {
		if (isCurrentApplication()) {
			if (currentRole != null) {
				currentRole.setType(value);
			}
		}
	}
	
	/**
	 * @param value
	 */
	public void setServerIdentityApplicationRolesRoleVendor(String value) {
		if (isCurrentApplication()) {
			if (currentRole != null) {
				currentRole.setVendor(value);
			}
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationRolesRoleProduct(String value) {
		if (isCurrentApplication()) {
			if (currentRole != null) {
				currentRole.setProduct(value);
			}
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationRolesRoleVersion(String value) {
		if (isCurrentApplication()) {
			if (currentRole != null) {
				currentRole.setVersion(value);
			}
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationRolesRolePatchLevel(String value) {
		if (isCurrentApplication()) {
			if (currentRole != null) {
				currentRole.setPatchLevel(value);
			}
		}
	}

	/**
	 * @param atts
	 */
	public void setServerIdentityApplicationPragma(Attributes atts) {
		if (isCurrentApplication()) {
			String name = atts.getValue(HABITAT4J_PRAGMA_ATTRIBUTE_NAME);

			currentPragmaName.setLength(0);
			if (name != null && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				currentPragmaName.append(name);
			}

			String value = atts.getValue(HABITAT4J_PRAGMA_ATTRIBUTE_VALUE);
			if (value != null) {
				Hashtable pragma = serverIdentity.getPragmaDefinitions();
				pragma.put(name,value);
				pragmaValueSetInAttributeFlag=true;
			}			
		}
	}

	/**
	 * @param value
	 */
	public void setServerIdentityApplicationPragmaName(String value) {
		if (isCurrentApplication()) {
			if (pragmaValueSetInAttributeFlag) {
				pragmaValueSetInAttributeFlag=false;
				return;
			}

			String name = value;

			currentPragmaName.setLength(0);
			if (name != null && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				currentPragmaName.append(name);
			}
		}		
	}
	
	/**
	 * @param value
	 */
	public void setServerIdentityApplicationPragma(String value) {
		if (isCurrentApplication()) {
			Hashtable pragma = serverIdentity.getPragmaDefinitions();
			String name = currentPragmaName.toString();
			if (name != null && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				pragma.put(name,value);
			}
		}
	}
	
	/**
	 * @param value
	 */
	public void setServerIdentityApplicationPragmaValue(String value) {
		setServerIdentityApplicationPragma(value);
	}

	/**
	 * @return Returns whether we're in the current application's context
	 */
	private boolean isCurrentApplication() {
		if (getCurrentApplicationName().equals(serverIdentity.getApplicationName()) || getCurrentApplicationName().equals(HABITAT4J_APPNAME_DEFAULT)) {
			return true;
		}

		return false;
	}
	
	/**
	 * @return Returns the currentApplicationName.
	 */
	private String getCurrentApplicationName() {
		return currentApplicationName.toString();
	}
	
	/**
	 * @param currentApplicationName The currentApplicationName to set.
	 */
	private void setCurrentApplicationName(String currentApplicationName) {
		this.currentApplicationName.setLength(0);
		this.currentApplicationName.append(currentApplicationName);
	}
	
	/**
	 * @param contextRoleNames
	 */
	private static void setContextRoleNames(String[] contextRoleNames) {
		for (int i=0; i<contextRoleNames.length; i++) {
			contextRoleNamesHash.add(contextRoleNames[i]);
		}		
	}
}
