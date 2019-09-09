package org.productivity.java.habitat4j;

import java.util.HashMap;
import java.util.Map;

import org.productivity.java.habitat4j.common.ApplicationPropertyListLoader;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.Habitat4JLogger;
import org.productivity.java.habitat4j.common.exception.ApplicationPropertyListLoaderException;
import org.productivity.java.habitat4j.common.exception.Habitat4JBootstrapException;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;
import org.productivity.java.habitat4j.common.interfaces.ApplicationPropertyListLoaderIF;

/**
 * Habitat4JBootstrap provides several static methods for initializing, or "bootstrapping,"
 * your application for using Habitat4J.
 * 
 * <p>This class imposes a standard for defining locations of
 * PropertyList XML files, and also a standard for naming of
 * the files.</p>
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
 * @version $Id: Habitat4JBootstrap.java,v 1.8 2005/11/15 15:01:27 cvs Exp $
 */
public class Habitat4JBootstrap implements Habitat4JConstants {
	protected static Habitat4JLogger logger = null;

	protected static final String DEFAULT_BOOTSTRAP_PROPERTY_LIST_FILENAME				= "properties-bootstrap.xml";
	protected static final Class DEFAULT_APPLICATION_PROPERTY_LIST_LOADER				= ApplicationPropertyListLoader.class;

	public static final String BOOTSTRAP_PARAM_APPLICATION_NAME							= "applicationName";
	public static final String BOOTSTRAP_PARAM_BOOTSTRAP_PROPERTIES_PATH				= "bootstrapPropertiesPath";
	public static final String BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED					= "xsdValidationEnabled";
	public static final String BOOTSTRAP_PARAM_LOG4J_ENABLED							= "log4jEnabled";
	public static final String BOOTSTRAP_PARAM_LOG_LEVEL								= "logLevel";
	public static final String BOOTSTRAP_PARAM_SERVER_IDENTITY_FILE_PATH				= "serverIdentityFilePath";
	public static final String BOOTSTRAP_PARAM_SERVER_IDENTITY_MODE						= "serverIdentityMode";
	public static final String BOOTSTRAP_PARAM_APPLICATION_PROPERTY_LIST_LOADER_CLASS	= "applicationPropertyListLoaderClass";
	
	// This is the default mapping, which provides a trivial one-to-one array (applicationName -> applicationName, etc.)
	protected static final String[][] defaultBootstrapParamNameArray = {
		{ BOOTSTRAP_PARAM_APPLICATION_NAME,							BOOTSTRAP_PARAM_APPLICATION_NAME },
		{ BOOTSTRAP_PARAM_BOOTSTRAP_PROPERTIES_PATH,				BOOTSTRAP_PARAM_BOOTSTRAP_PROPERTIES_PATH },
		{ BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED,					BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED },
		{ BOOTSTRAP_PARAM_LOG4J_ENABLED,							BOOTSTRAP_PARAM_LOG4J_ENABLED },
		{ BOOTSTRAP_PARAM_LOG_LEVEL,								BOOTSTRAP_PARAM_LOG_LEVEL },
		{ BOOTSTRAP_PARAM_SERVER_IDENTITY_FILE_PATH,				BOOTSTRAP_PARAM_SERVER_IDENTITY_FILE_PATH },
		{ BOOTSTRAP_PARAM_SERVER_IDENTITY_MODE,						BOOTSTRAP_PARAM_SERVER_IDENTITY_MODE },
		{ BOOTSTRAP_PARAM_APPLICATION_PROPERTY_LIST_LOADER_CLASS,	BOOTSTRAP_PARAM_APPLICATION_PROPERTY_LIST_LOADER_CLASS }
	};
	
	static {
		logger = Habitat4JLogger.getInstance();
	}
	
	/**
	 * getDefaultBootstrapParamNameMap provides a default HashMap of the default one-to-one mappings in
	 * defaultBootstrapParamNameArray.  It can be used as a starting point when creating a custom
	 * bootstrap parameter name HashMap.
	 * 
	 * @return Returns a trivial map generated from the defaultBootstrapParamNameArray one-to-one map.
	 */
	public static HashMap getDefaultBootstrapParamNameMap() {
		HashMap defaultBootstrapParamNameMap = new HashMap();
		
		for (int i=0; i < defaultBootstrapParamNameArray.length; i++) {
			String paramName = defaultBootstrapParamNameArray[i][0];
			String paramValue = defaultBootstrapParamNameArray[i][1];
			
			defaultBootstrapParamNameMap.put(paramName,paramValue);
		}
		
		return defaultBootstrapParamNameMap;
	}	
	
	/**
	 * @param applicationName - the name of the calling application
	 * @throws Habitat4JBootstrapException
	 */
	protected static void configureApplicationName(String applicationName) throws Habitat4JBootstrapException {
		if (applicationName == null || applicationName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			throw new Habitat4JBootstrapException("applicationName must be defined for Habitat4JBootstrap to function.");
		}
	}

	/**
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 */
	protected static void configureServerIdentityXsdValidation(boolean xsdValidationEnabled) {
		try {
			ServerIdentityManager.setFeature(Habitat4JFeatures.SERVER_IDENTITY_FEATURE_XSD_VALIDATION,xsdValidationEnabled);
			
		} catch (ServerIdentityHandlerException sihe) {
			// This block should never be reached; above is properly configured.
		}
	}

	/**
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 */
	protected static void configurePropertyListXsdValidation(boolean xsdValidationEnabled) {
		try {
			PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_XSD_VALIDATION,xsdValidationEnabled);
			
		} catch (PropertyListHandlerException plhe) {
			// This block should never be reached; above code is properly configured.
		}
	}

	/**
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 */
	protected static void configureLog4j(boolean log4jEnabled, String logLevel) {
		// Enable log4j if "true"
		if (log4jEnabled) {
			Habitat4JLogger.getInstance().selectLog4j();
			
		// .. else enable the stock logger if "false"
		} else {
			Habitat4JLogger.getInstance().selectSystem();
		}
		
		// Get the logLevel value
		if (logLevel != null && !logLevel.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			
			// Set the desired level
			Habitat4JLogger.getInstance().setLevel(logLevel);		
		}		
	}
	
	/**
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 */
	protected static void configureServerIdentity(String serverIdentityFilePath) {
		StringBuffer serverIdentityFilePathBuffer = new StringBuffer();
		
		// Add the explicitly provided path first
		if (serverIdentityFilePath != null && !serverIdentityFilePath.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			serverIdentityFilePathBuffer.append(serverIdentityFilePath);
			serverIdentityFilePathBuffer.append(";");
		}
		
		// ... then add the one from the System property
		String systemFilePath = System.getProperty(Habitat4JConstants.HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME);		
		if (systemFilePath != null && !systemFilePath.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			serverIdentityFilePathBuffer.append(systemFilePath);
			serverIdentityFilePathBuffer.append(";");
		}
		
		// .. then add the Habitat4J default ("/server-identity.xml")
		serverIdentityFilePathBuffer.append(Habitat4JConstants.HABITAT4J_SERVER_IDENTITY_FILE_PATH_DEFAULT);
		
		serverIdentityFilePath = serverIdentityFilePathBuffer.toString();
		
		// If it's not empty and not based on the systemFilePath, then debug log this information, and set the System property
		if (serverIdentityFilePath != null && !serverIdentityFilePath.equals(systemFilePath)) {
			Habitat4JLogger.getInstance().debug("Path which will be used for searching for a server-identity.xml file is: " + serverIdentityFilePath);
			
			System.setProperty(Habitat4JConstants.HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME,serverIdentityFilePath);
		}
	}

	/**
	 * @param applicationName - the name of the calling application
	 * @param serverIdentityMode - the ServerIdentity mode to use
	 */
	protected static void configurePropertyListManager(String applicationName, String serverIdentityMode) {
		PropertyListManager.initialize(applicationName,serverIdentityMode);
	}
	
	/**
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @throws Habitat4JBootstrapException
	 */
	protected static void loadBootstrapPropertyList(String bootstrapPropertyListPath) throws Habitat4JBootstrapException {
		if (bootstrapPropertyListPath == null || bootstrapPropertyListPath.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			String packageName = Habitat4JBootstrap.class.getPackage().getName();
			
			bootstrapPropertyListPath = packageName.replace('.','/') + "/" + DEFAULT_BOOTSTRAP_PROPERTY_LIST_FILENAME;			
		}
		
		try {
			PropertyListManager.loadPropertyList(bootstrapPropertyListPath);
			
		} catch (PropertyListHandlerException plhe) {
			throw new Habitat4JBootstrapException(plhe);
		}
	}

	/**
	 * @param applicationName - the name of the calling application
	 * @param appPropertyListLoaderClassName - a String containing the fully qualified class for the implementation of ApplicationPropertyListLoaderIF
	 * @throws Habitat4JBootstrapException
	 */
	protected static void loadApplicationPropertyLists(String applicationName, String appPropertyListLoaderClassName) throws Habitat4JBootstrapException {
		if (appPropertyListLoaderClassName == null || appPropertyListLoaderClassName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			loadApplicationPropertyLists(applicationName,appPropertyListLoaderClassName);
		}
		
		try {
			Class appPropertyListLoaderClassObject = Habitat4JBootstrap.class.getClassLoader().loadClass(appPropertyListLoaderClassName);
			loadApplicationPropertyLists(applicationName,appPropertyListLoaderClassObject);
			
		} catch (ClassNotFoundException cnfe) {
			Habitat4JLogger.getInstance().warn("No PropertyList files were loaded for this application");
			throw new Habitat4JBootstrapException(cnfe);			
		}
	}

	/**
	 * @param applicationName - the name of the calling application
	 * @param appPropertyListLoaderClass - the Class object for the implementation of ApplicationPropertyListLoaderIF
	 * @throws Habitat4JBootstrapException
	 */
	protected static void loadApplicationPropertyLists(String applicationName, Class appPropertyListLoaderClass) throws Habitat4JBootstrapException {
		try {
			ApplicationPropertyListLoaderIF loader = null;
			
			if (appPropertyListLoaderClass == null) {
				loader = (ApplicationPropertyListLoaderIF) DEFAULT_APPLICATION_PROPERTY_LIST_LOADER.newInstance();
				
			} else {
				loader = (ApplicationPropertyListLoaderIF) appPropertyListLoaderClass.newInstance();
			}
			
			loader.load(applicationName);
			
		} catch (ApplicationPropertyListLoaderException le) {
			throw new Habitat4JBootstrapException(le);
			
		} catch (InstantiationException ie) {
			Habitat4JLogger.getInstance().warn("No PropertyList files were loaded for this application");
			throw new Habitat4JBootstrapException(ie);
			
		} catch (IllegalAccessException iae) {
			Habitat4JLogger.getInstance().warn("No PropertyList files were loaded for this application");
			throw new Habitat4JBootstrapException(iae);
			
		} catch (ClassCastException cce) {
			throw new Habitat4JBootstrapException(cce);	
		}
	}

	/**
	 * @throws Habitat4JBootstrapException
	 */
	protected static void loadServerIdentity() throws Habitat4JBootstrapException {
		try {
			ServerIdentityManager.loadServerIdentity();
			
		} catch (ServerIdentityHandlerException sihe) {
			throw new Habitat4JBootstrapException(sihe);
		}
	}
	
	/**
	 * @param applicationName - the name of the calling application
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @param serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(String applicationName, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel, String serverIdentityFilePath, String serverIdentityMode) throws Habitat4JBootstrapException {
		configureLog4j(log4jEnabled,logLevel);
		configureServerIdentity(serverIdentityFilePath);
		
		ServerIdentityManager.initialize(applicationName,serverIdentityMode);
		configureServerIdentityXsdValidation(xsdValidationEnabled);
		loadServerIdentity();
	}
	
	/**
	 * @param applicationName - the name of the calling application
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(String applicationName, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel, String serverIdentityFilePath) throws Habitat4JBootstrapException {
		initializeServerIdentity(applicationName,xsdValidationEnabled,log4jEnabled,logLevel,serverIdentityFilePath,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE);
	}

	/**
	 * @param applicationName - the name of the calling application
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @param serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(String applicationName,String serverIdentityFilePath, String serverIdentityMode) throws Habitat4JBootstrapException {
		configureServerIdentity(serverIdentityFilePath);

		ServerIdentityManager.initialize(applicationName,serverIdentityMode);
		loadServerIdentity();
	}	

	/**
	 * @param applicationName - the name of the calling application
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(String applicationName,String serverIdentityFilePath) throws Habitat4JBootstrapException {
		initializeServerIdentity(applicationName,serverIdentityFilePath,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE);
	}	

	/**
	 * @param applicationName - the name of the calling application
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(String applicationName) throws Habitat4JBootstrapException {
		ServerIdentityManager.initialize(applicationName);
		loadServerIdentity();
	}
	
	/**
	 * @param bootstrapPropertyListPath
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param appPropertyListLoaderClass
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializePropertyLists(String bootstrapPropertyListPath, boolean xsdValidationEnabled, String appPropertyListLoaderClass) throws Habitat4JBootstrapException {
		String appName = ServerIdentityManager.getApplicationName();

		PropertyListManager.initialize(appName);
		configurePropertyListXsdValidation(xsdValidationEnabled);
		
		loadBootstrapPropertyList(bootstrapPropertyListPath);

		if (appPropertyListLoaderClass == null) {
			loadApplicationPropertyLists(appName,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
			
		} else {
			loadApplicationPropertyLists(appName,appPropertyListLoaderClass);
		}
	}

	/**
	 * @param bootstrapPropertyListPath
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializePropertyLists(String bootstrapPropertyListPath,boolean xsdValidationEnabled) throws Habitat4JBootstrapException {
		initializePropertyLists(bootstrapPropertyListPath,xsdValidationEnabled,null);
	}

	/**
	 * @param bootstrapPropertyListPath
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializePropertyLists(String bootstrapPropertyListPath) throws Habitat4JBootstrapException {
		initializePropertyLists(bootstrapPropertyListPath,false);
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
	 * <li>logLevel - a String containing one of the following log levels: debug, info, warn, error, or fatal mainly used for setting Habitat4J's native logging level (non-log4j)</li>
	 * <li>serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths pointing to a server-identity.xml file or files</li>
	 * <li>serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * <li>appPropertyListLoaderClassName - a String containing the fully qualified class for the customized implementation of ApplicationPropertyListLoaderIF</li>
	 * </li>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @param serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * @param appPropertyListLoaderClass - a Class that implements ApplicationPropertyListLoaderIF
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel, String serverIdentityFilePath, String serverIdentityMode, Class appPropertyListLoaderClass) throws Habitat4JBootstrapException {
		configureApplicationName(applicationName);
		configureLog4j(log4jEnabled,logLevel);
		configureServerIdentity(serverIdentityFilePath);
		configurePropertyListManager(applicationName,serverIdentityMode);
		configureServerIdentityXsdValidation(xsdValidationEnabled);
		configurePropertyListXsdValidation(xsdValidationEnabled);
		
		loadBootstrapPropertyList(bootstrapPropertyListPath);

		if (appPropertyListLoaderClass == null) {
			loadApplicationPropertyLists(applicationName,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
			
		} else {
			loadApplicationPropertyLists(applicationName,appPropertyListLoaderClass);
		}
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
	 * <li>logLevel - a String containing one of the following log levels: debug, info, warn, error, or fatal mainly used for setting Habitat4J's native logging level (non-log4j)</li>
	 * <li>serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths pointing to a server-identity.xml file or files</li>
	 * <li>serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * <li>appPropertyListLoaderClassName - a String containing the fully qualified class for the customized implementation of ApplicationPropertyListLoaderIF</li>
	 * </li>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @param serverIdentityMode - a String containing the ServerIdentity mode of operation (NULL, JVM, FILE)
	 * @param appPropertyListLoaderClassName - a String containing the fully qualified class for the implementation of ApplicationPropertyListLoaderIF
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel, String serverIdentityFilePath, String serverIdentityMode, String appPropertyListLoaderClassName) throws Habitat4JBootstrapException {
		configureApplicationName(applicationName);
		configureLog4j(log4jEnabled,logLevel);
		configureServerIdentity(serverIdentityFilePath);
		configurePropertyListManager(applicationName,serverIdentityMode);
		configureServerIdentityXsdValidation(xsdValidationEnabled);
		configurePropertyListXsdValidation(xsdValidationEnabled);
		
		loadBootstrapPropertyList(bootstrapPropertyListPath);

		if (appPropertyListLoaderClassName == null || appPropertyListLoaderClassName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			loadApplicationPropertyLists(applicationName,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
			
		} else {
			loadApplicationPropertyLists(applicationName,appPropertyListLoaderClassName);
		}
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
	 * <li>logLevel - a String containing one of the following log levels: debug, info, warn, error, or fatal mainly used for setting Habitat4J's native logging level (non-log4j)</li>
	 * <li>serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths pointing to a server-identity.xml file or files</li>
	 * </li>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @param serverIdentityFilePath - a String containing a single path or semicolon-delimited list of paths
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel, String serverIdentityFilePath) throws Habitat4JBootstrapException {
		initialize(applicationName,bootstrapPropertyListPath,xsdValidationEnabled,log4jEnabled,logLevel,serverIdentityFilePath,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
	 * <li>logLevel - a String containing one of the following log levels: debug, info, warn, error, or fatal mainly used for setting Habitat4J's native logging level (non-log4j)</li>
	 * </li>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @param logLevel - a String containing either: "debug" "info" "warn" "error" or "fatal"
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled, boolean log4jEnabled, String logLevel) throws Habitat4JBootstrapException {
		initialize(applicationName,bootstrapPropertyListPath,xsdValidationEnabled,log4jEnabled,logLevel,null,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * <li>log4jEnabled - a boolean value which determines whether Habitat4J should use log4j</li>
	 * </ul>
	 *
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @param log4jEnabled - boolean whether log4j should be used for logging
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled, boolean log4jEnabled) throws Habitat4JBootstrapException {
		initialize(applicationName,bootstrapPropertyListPath,xsdValidationEnabled,log4jEnabled,null,null,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
	}

	/**
	 * To use this initialize() method, specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths)</li>
	 * <li>xsdValidationEnabled - a boolean value which determines whether Habitat4J should validate the server-identity and property-list files against the stock Habitat4J XSD files</li>
	 * </ul>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @param xsdValidationEnabled - boolean whether XSD validation should occur
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath, boolean xsdValidationEnabled) throws Habitat4JBootstrapException {
		initialize(applicationName,bootstrapPropertyListPath,xsdValidationEnabled,false,null,null,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
	}

	/**
	 * This initialize() method is the simplest to use, but has the least features.  Simply specify:
	 * 
	 * <ul>
	 * <li>applicationName - the name of your application</li>
	 * <li>bootstrapPropertyListPath - a path, either on the filesystem, or in the classpath (use slashes to delimit package paths.)</li>
	 * </ul>
	 * 
	 * @param applicationName - the name of the calling application
	 * @param bootstrapPropertyListPath - the file- or class-path to the bootstrap property file
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(String applicationName, String bootstrapPropertyListPath) throws Habitat4JBootstrapException {
		initialize(applicationName,bootstrapPropertyListPath,false,false,null,null,Habitat4JConstants.SERVER_IDENTITY_MODE_FILE,DEFAULT_APPLICATION_PROPERTY_LIST_LOADER);
	}
	
	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameter values
	 * @param bootstrapInitParamsNameMap - the Map instance of name/value pairs that contain initialization parameter name-to-name mappings
	 * @param name - the name in the name/value pair
	 * @return Returns the value in the name/value pair
	 * 
	 * @throws Habitat4JBootstrapException
	 */
	protected static String getMappedInitParamValue(Map bootstrapInitParams, Map bootstrapInitParamsNameMap, String name) throws Habitat4JBootstrapException {
		String methodName = "getMappedValue(): ";
		
		// If the values Map provided is null, throw an Exception
		if (bootstrapInitParams == null) {
			throw new Habitat4JBootstrapException(methodName + "values Map instance is null");
		}
		
		// If the name provided is null or empty, throw an Exception
		if (name == null || name.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			throw new Habitat4JBootstrapException(methodName + "cannot provide a null or empty name");
		}		

		// Start with the original name
		String mappedName = name;

		// If provided a name-to-name mapping, retrieve the mapped name
		if (bootstrapInitParamsNameMap != null) {
			mappedName = (String) bootstrapInitParamsNameMap.get(name);			

			// If it didn't look up, log a warning and continue with the original name 
			if (mappedName == null) {
				mappedName = name;
				logger.warn(Habitat4JBootstrap.class.getName() + "." + methodName + "no bootstrapInitParamsNameMap entry found for key \"" + name + "\" - using this unmapped (original) name");
			}			
		}

		// Get the mapped value
		String mappedValue = (String) bootstrapInitParams.get(mappedName);
		
		// Return it
		return mappedValue;
	}
	
	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @param bootstrapInitParamsNameMap - the Map instance of name/value pairs that contain initialization parameter name-to-name mappings
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(Map bootstrapInitParams, Map bootstrapInitParamsNameMap) throws Habitat4JBootstrapException {
		String applicationName = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_APPLICATION_NAME);
		boolean xsdValidationEnabled = new Boolean(getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED)).booleanValue();
		boolean log4jEnabled = new Boolean(getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_LOG4J_ENABLED)).booleanValue();
		String logLevel = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_LOG_LEVEL);
		String serverIdentityFilePath = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_SERVER_IDENTITY_FILE_PATH);
		String serverIdentityMode = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_SERVER_IDENTITY_MODE);

		initializeServerIdentity(applicationName,xsdValidationEnabled,log4jEnabled,logLevel,serverIdentityFilePath,serverIdentityMode);
	}

	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializeServerIdentity(Map bootstrapInitParams) throws Habitat4JBootstrapException {
		initializeServerIdentity(bootstrapInitParams,null);
	}

	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @param bootstrapInitParamsNameMap - the Map instance of name/value pairs that contain initialization parameter name-to-name mappings
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializePropertyLists(Map bootstrapInitParams, Map bootstrapInitParamsNameMap) throws Habitat4JBootstrapException {
		boolean xsdValidationEnabled = new Boolean(getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED)).booleanValue();
		String bootstrapPropertiesPath = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_BOOTSTRAP_PROPERTIES_PATH);
		String applicationPropertyListLoaderClass = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_APPLICATION_PROPERTY_LIST_LOADER_CLASS);
		
		initializePropertyLists(bootstrapPropertiesPath,xsdValidationEnabled,applicationPropertyListLoaderClass);
	}

	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @throws Habitat4JBootstrapException
	 */
	public static void initializePropertyLists(Map bootstrapInitParams) throws Habitat4JBootstrapException {
		initialize(bootstrapInitParams,null);
	}

	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @param bootstrapInitParamsNameMap - the Map instance of name/value pairs that contain initialization parameter name-to-name mappings
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(Map bootstrapInitParams, Map bootstrapInitParamsNameMap) throws Habitat4JBootstrapException {
		String applicationName = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_APPLICATION_NAME);
		boolean xsdValidationEnabled = new Boolean(getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_XSD_VALIDATION_ENABLED)).booleanValue();
		boolean log4jEnabled = new Boolean(getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_LOG4J_ENABLED)).booleanValue();
		String logLevel = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_LOG_LEVEL);
		String serverIdentityFilePath = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_SERVER_IDENTITY_FILE_PATH);
		String serverIdentityMode = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_SERVER_IDENTITY_MODE);
		String bootstrapPropertiesPath = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_BOOTSTRAP_PROPERTIES_PATH);
		String applicationPropertyListLoaderClass = getMappedInitParamValue(bootstrapInitParams,bootstrapInitParamsNameMap,BOOTSTRAP_PARAM_APPLICATION_PROPERTY_LIST_LOADER_CLASS);

		initialize(applicationName,bootstrapPropertiesPath,xsdValidationEnabled,log4jEnabled,logLevel,serverIdentityFilePath,serverIdentityMode,applicationPropertyListLoaderClass);
	}

	/**
	 * @param bootstrapInitParams - the Map instance of name/value pairs that contain bootstrap initialization parameters
	 * @throws Habitat4JBootstrapException
	 */
	public static void initialize(Map bootstrapInitParams) throws Habitat4JBootstrapException {
		initialize(bootstrapInitParams,null);
	}
	
	/**
	 * createServerIdentityPath() is a helper method for specifying multiple locations
	 * for a server-identity file.
	 * 
	 * @param serverIdentityFileName - filename for server-identity file.
	 * @param directories - all directories where the server-identity file could exist.
	 * @return Returns a semicolon-delimited server-identity path.
	 */
	public static String createServerIdentityPath(String serverIdentityFileName, String[] directories) {
		String _serverIdentityFileName = serverIdentityFileName;
		
		if (_serverIdentityFileName == null || _serverIdentityFileName.trim().equals("")) {
			_serverIdentityFileName = Habitat4JConstants.HABITAT4J_SERVER_IDENTITY_FILE_PATH_DEFAULT;
		}
		
		if (directories == null || directories.length < 1) {
			return _serverIdentityFileName;
		}
		
		if (_serverIdentityFileName.charAt(0) == '/' || _serverIdentityFileName.charAt(0) == '\\') {
			_serverIdentityFileName = _serverIdentityFileName.substring(1);
		}
		
		StringBuffer path = new StringBuffer();
		String separator = System.getProperty("file.separator");
		if (separator == null || separator.equals("")) {
			separator = "/";
		}
		
		for (int i=0; i<directories.length; i++) {
			path.append(directories[i]);
			path.append(separator);
			path.append(_serverIdentityFileName);
			
			if (i < directories.length-1) {
				path.append(";");
			}			
		}
		
		return path.toString();
	}
}
