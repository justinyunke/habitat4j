package org.productivity.java.habitat4j.common.sax;

import java.io.InputStream;
import java.security.Security;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.PropertyArrayVector;
import org.productivity.java.habitat4j.common.PropertyBeanArrayVector;
import org.productivity.java.habitat4j.common.PropertyBeanHash;
import org.productivity.java.habitat4j.common.PropertyHash;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.ServerIdentity;
import org.productivity.java.habitat4j.common.ServerIdentityRole;
import org.productivity.java.habitat4j.common.exception.BaseHandlerException;
import org.productivity.java.habitat4j.common.exception.DecoderException;
import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.exception.PropertyNotAvailableException;
import org.productivity.java.habitat4j.common.interfaces.DecoderIF;
import org.productivity.java.habitat4j.common.interfaces.DecryptorIF;
import org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF;
import org.productivity.java.habitat4j.compat.PropertyListXMLMap;
import org.xml.sax.Attributes;

/**
 * Class used for handling the parsing of PropertyList XML files and resources.
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
 * @version $Id: PropertyListHandler.java,v 1.75 2006/07/08 21:17:01 cvs Exp $
 */
public class PropertyListHandler extends AbstractBaseHandler {
	private ServerIdentity serverIdentity = null;
	
	private PropertyList propertyList = null;
	
	private StringBuffer currentDefinitionPackage = new StringBuffer();
	
	private StringBuffer currentPragmaName = new StringBuffer();
	private StringBuffer currentPragmaValue = new StringBuffer();
	
	private boolean currentContextFlag = true;
	private StringBuffer currentContextPrefix = new StringBuffer();

	private StringBuffer currentPropertyName = new StringBuffer();

	private StringBuffer currentDecoderId = new StringBuffer();
	private StringBuffer currentDecryptorId = new StringBuffer();	
	private boolean currentDecoderFirst = false;
	
	private PropertyArrayVector currentPropertyArray = null;
	private String currentPropertyArrayInstance = HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;

	private PropertyHash currentPropertyHash = null;
	private String currentPropertyHashKey = null;
	private String currentPropertyHashInstance = HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;

	private Object currentPropertyBean = null;

	private StringBuffer currentPropertyBeanArrayId = new StringBuffer();
	private PropertyBeanArrayVector currentPropertyBeanArray = null;
	private String currentPropertyBeanArrayInstance = HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
	
	private StringBuffer currentPropertyBeanHashId = new StringBuffer();

	private PropertyBeanHash currentPropertyBeanHash = null;
	private String currentPropertyBeanHashKey = null;
	private String currentPropertyBeanHashInstance = null;
	
	private boolean propertyValueSetInAttributeFlag = false;
	
	private Hashtable systemPropertyQueue = null;
	private Hashtable securityPropertyQueue = null;
	
	private Hashtable features = null;
	
	/**
	 * @param serverIdentity
	 * @param propertyList
	 * @param propertyListFilePath
	 * @param features
	 * @param map
	 * @throws PropertyListHandlerException
	 * @throws BaseHandlerException
	 */
	public PropertyListHandler(ServerIdentity serverIdentity, PropertyList propertyList, String propertyListFilePath, Hashtable features, PropertyListXMLMap map) throws PropertyListHandlerException, BaseHandlerException {
		super();
		
		this.features = features;
		this.map = map;
		
		String xsdValidationPath = getXsdValidationPath(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_XSD_VALIDATION,HABITAT4J_PROPERTY_LIST_XSD_RESOURCE_PATH_DEFAULT);			
		initialize(serverIdentity,propertyList,propertyListFilePath,null,null,xsdValidationPath);
	}
	
	/**
	 * @param serverIdentity
	 * @param propertyList
	 * @param propertyListInputStream
	 * @param resourceName
	 * @param features
	 * @param map
	 * @throws PropertyListHandlerException
	 * @throws BaseHandlerException
	 */
	public PropertyListHandler(ServerIdentity serverIdentity, PropertyList propertyList, InputStream propertyListInputStream, String resourceName, Hashtable features, PropertyListXMLMap map) throws PropertyListHandlerException, BaseHandlerException {
		super();

		this.features = features;
		this.map = map;
		
		String xsdValidationPath = getXsdValidationPath(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_XSD_VALIDATION,HABITAT4J_PROPERTY_LIST_XSD_RESOURCE_PATH_DEFAULT);
		initialize(serverIdentity,propertyList,null,propertyListInputStream,resourceName,xsdValidationPath);
	}

	/**
	 * @param serverIdentity
	 * @param propertyList
	 * @param propertyListFilePath
	 * @param propertyListInputStream
	 * @param resourceName
	 * @param xsdResourcePath
	 * @throws PropertyListHandlerException
	 * @throws BaseHandlerException
	 */
	private void initialize(ServerIdentity serverIdentity, PropertyList propertyList, String propertyListFilePath, InputStream propertyListInputStream, String resourceName, String xsdResourcePath) throws PropertyListHandlerException, BaseHandlerException {
		String logMethodName = this.getClass().getName() + " constructor - ";

		this.propertyList = propertyList; 
		setServerIdentity(serverIdentity);
		
		if (serverIdentity == null) {
			throw new BaseHandlerException(logMethodName + "ServerIdentity not initialized");
		}

		if (propertyListFilePath != null && (propertyListFilePath == null || propertyListFilePath.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING))) {
			throw new PropertyListHandlerException("PropertyList XML file name is null or empty");
		}
		
		long lastModified=-1;
		
		try {			
			if (propertyListFilePath != null) {
				if (xsdResourcePath != null) {
					lastModified = parse(propertyListFilePath,xsdResourcePath);
				} else {
					lastModified = parse(propertyListFilePath);
				}
				propertyList.setPropertyListFilePath(propertyListFilePath);
			}
			if (propertyListInputStream != null) {
				if (xsdResourcePath != null) {
					parse(propertyListInputStream,xsdResourcePath,resourceName);
				} else {
					parse(propertyListInputStream,resourceName);					
				}
			}
			
			commitSystemProperties();
			commitSecurityProperties();
			
		} catch (BaseHandlerException xmlhe) {
			logger.info(logMethodName + "PropertyList file NOT loaded: " + xmlhe);
			throw new PropertyListHandlerException(xmlhe);
			
		} finally {
			propertyList.setPropertyListFileLastModified(lastModified);
			propertyList.setPropertyListFileLastLoaded(new Date().getTime());
		}
	}

	/**
	 * 
	 */
	private void commitSystemProperties() {
		if (systemPropertyQueue == null || systemPropertyQueue.size() < 1) return;
		
		String logMethodName = "commitSystemProperties(): ";
		
		Enumeration enumeration = systemPropertyQueue.keys();
		
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = (String) systemPropertyQueue.get(name);
			
			String currentValue = System.getProperty(name);
			
			if (currentValue == null) {
				System.setProperty(name,value);
				
			} else if (!currentValue.equals(value)) {
				if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_SYSTEM_PROPERTY_OVERRIDE)) {
					System.setProperty(name,value);
					logger.debug(logMethodName + "Override of System property (" + name + ") succeeded");
					
				} else {
					logger.error(logMethodName + "Attempt to override System property (" + name + ") failed");
				}
				
			} else {
				logger.debug(logMethodName + "Attempt to override System property (" + name + ") with identical value ignored");
			}
		}
	}
	/**
	 * 
	 */
	private void commitSecurityProperties() {
		if (securityPropertyQueue == null || securityPropertyQueue.size() < 1) return;
		
		String logMethodName = "commitSecurityProperties(): ";
		
		Enumeration enumeration = securityPropertyQueue.keys();
		
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = (String) securityPropertyQueue.get(name);
			
			String currentValue = Security.getProperty(name);
			
			if (currentValue == null) {
				Security.setProperty(name,value);
				
			} else if (!currentValue.equals(value)) {
				if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_SECURITY_PROPERTY_OVERRIDE)) {
					Security.setProperty(name,value);
					logger.debug(logMethodName + "Override of Security property (" + name + ") succeeded");
					
				} else {
					logger.error(logMethodName + "Attempt to override Security property (" + name + ") failed");
				}
				
			} else {
				logger.debug(logMethodName + "Attempt to override Security property (" + name + ") with identical value ignored");
			}
		}
	}

	// ###################################### HELPER METHODS ########################################

	/**
	 * This method sets the current decoder and decryptor ids.
	 * 
	 * @param atts - attributes of the current element
	 */
	private void setDecoderDecryptorIds(Attributes atts) {
		String _decodingId = atts.getValue(HABITAT4J_PROPERTY_DECODING_ATTRIBUTE_NAME);
		if (_decodingId != null) currentDecoderId.append(_decodingId);

		String _decryptorId = atts.getValue(HABITAT4J_PROPERTY_DECRYPTING_ATTRIBUTE_NAME);
		if (_decryptorId != null) currentDecryptorId.append(_decryptorId);

		if (atts.getIndex(HABITAT4J_PROPERTY_DECODING_ATTRIBUTE_NAME) < atts.getIndex(HABITAT4J_PROPERTY_DECRYPTING_ATTRIBUTE_NAME)) {
			currentDecoderFirst = true;
		}
	}

	private void initDecoderDecryptorIds() {
		currentDecoderId.setLength(0);
		currentDecryptorId.setLength(0);
		currentDecoderFirst = false;		
	}

	/**
	 * @param in
	 * @param prefix
	 * @param prefixDelimiter
	 * @return Returns a prepended String.
	 */
	private String prependStringWithStringBufferContents(String in, StringBuffer prefix, String prefixDelimiter) {
		if (in != null && prefix != null && prefix.length()>0) {
			StringBuffer buffer = new StringBuffer(in);

			if (prefixDelimiter != null) {
				buffer.insert(0,prefix.toString() + prefixDelimiter);
				
			} else {
				buffer.insert(0,prefix);
			}
			return buffer.toString();
		}

		return in;
	}

	/**
	 * @param name - the property name
	 * @return Returns a property name with the appropriate context prefix
	 */
	private String setCurrentPropertyName(String name) {
		currentPropertyName.setLength(0);
		currentPropertyName.append(currentContextPrefix.toString());
		currentPropertyName.append(name);
		
		return currentPropertyName.toString();
	}
	
	/**
	 * @param substituteVariableName - the FOO part of the ${pragma:FOO} subtitution
	 * @param substitutionVariableHash - the Hashtable to reference for values of FOO
	 * @param value - the original value to modify
	 * @return Returns the original or modified version of the value
	 */
	private String substituteVariable(String substituteVariableName, Hashtable substitutionVariableHash, String value) {
		if (value == null || value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return value;
		}
		
		String pragmaFieldVariable = HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_PREFIX + substituteVariableName + HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_DELIMITER;
		
		if (value.length() < (pragmaFieldVariable.length()+1) ) {
			return value;
		}
		
		int index = 0;
		StringBuffer newValue = new StringBuffer(value);
		
		while (true) {
			// StringBuffer.indexOf(..) - J2SE 1.4+
			int indexPragmaStart = newValue.toString().indexOf(pragmaFieldVariable,index);
			if (indexPragmaStart < 0) {
				return newValue.toString();
			}
			
			// StringBuffer.indexOf(..) - J2SE 1.4+
			int indexPragmaEnd = newValue.toString().indexOf(HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_SUFFIX,indexPragmaStart);
			if (indexPragmaEnd < 0) {
				return newValue.toString();
			}
			
			int indexFieldNameStart = indexPragmaStart + pragmaFieldVariable.length();
			
			String pragmaField = newValue.substring(indexFieldNameStart,indexPragmaEnd);

			String pragmaValue = (String) substitutionVariableHash.get(pragmaField);
			if (pragmaValue == null) {
				pragmaValue = HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
			}
			
			newValue.replace(indexPragmaStart,indexPragmaEnd+1,pragmaValue);
			
			index = indexPragmaStart + pragmaValue.length();
		}
	}

	/**
	 * @param value - the field value to consider for pragma variable substitution
	 * @return Returns the original or modified version of the value
	 */
	private String substitutePragmaVariable(String value) {
		if (!serverIdentity.hasPragmaDefinitions()) {
			return value;
		}
		
		return substituteVariable("pragma",serverIdentity.getPragmaDefinitions(),value);
	}

	/**
	 * @param name - the String name of the property name
	 * @param value - the original property String value
	 * @return Returns a mangled copy of the property value
	 */
	private String mangleValue(String name, String value) {
		if (!isCurrentContext()) return null;

		String logMethodName = this.getClass().getName() + ".mangleValue(String value) - ";
		
		if (value == null || value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return value;
		}

		DecoderIF decoder = null;
		DecryptorIF decryptor = null;

		try {
			if (currentDecoderId.length()>0) {
				decoder = propertyList.getDecoderInstance(
					serverIdentity.getApplicationName(),
					currentDecoderId.toString()
				);
			}
		} catch (DecoderException decoe) {
			logger.error(logMethodName + "Could not obtain decoder \"" + currentDecryptorId + "\", exception: " + decoe.toString());
			return null;
		}
		
		try {			
			if (currentDecryptorId.length()>0) {		
				decryptor = propertyList.getDecryptorInstance(
					serverIdentity.getApplicationName(),
					currentDecryptorId.toString()
				);
			}
		} catch (DecryptorException decre) {
			logger.error(logMethodName + "Could not obtain decryptor \"" + currentDecryptorId + "\", exception: " + decre.toString());
			return null;
		}

		if (decoder == null && decryptor == null) {
			return substitutePragmaVariable(value);
		}
		
		String value1 = null;
		String value2 = null;

		if (currentDecoderFirst) {
			if (decoder != null) {
				try {
					value1 = decoder.decode(propertyList.getListName(),name,value);
					propertyList.flagDecodedProperty(name);
					
				} catch (DecoderException de) {
					logger.error(logMethodName + "Decoder - " + de);					
					value1 = null;
				}
			} else {
				value1 = value;
			}

			if (decryptor != null) {
				try {
					value2 = decryptor.decrypt(propertyList.getListName(),name,value1);
					propertyList.flagDecryptedProperty(name);
					
				} catch (DecryptorException de) {
					logger.error(logMethodName + "Decryptor - " + de);					
					value2 = null;
				}
			} else {
				value2 = value1;
			}
			
			return substitutePragmaVariable(value2);
		}
		if (decryptor != null) {
			try {
				value1 = decryptor.decrypt(propertyList.getListName(),name,value);
				propertyList.flagDecryptedProperty(name);
				
			} catch (Exception e) {
				logger.error(logMethodName + "Decoder - " + e);	
				value1 = null;
			}
		} else {
			value1 = value;
		}

		if (decoder != null) {
			try {
				value2 = decoder.decode(propertyList.getListName(),name,value1);
				propertyList.flagDecodedProperty(name);
				
			} catch (Exception e) {
				logger.error(logMethodName + "Decryptor - " + e);					
				value2 = null;
			}
		} else {
			value2 = value1;
		}
		
		return substitutePragmaVariable(value2);
	}
	
	/**
	 * @return Returns the PropertyArray name with proper delimiter
	 */
	private String getExactPropertyArrayName() {
		return currentPropertyName.toString() + HABITAT4J_PROPERTY_TYPE_DELIMITER + currentPropertyArray.size();
	}

	/**
	 * @return Returns the PropertyArray name with proper delimiter
	 */
	private String getExactPropertyHashName() {
		return currentPropertyName.toString() + HABITAT4J_PROPERTY_TYPE_DELIMITER + currentPropertyHashKey;
	}

	/**
	 * @param _parameter
	 * @return Returns the PropertyBean name with proper delimiter
	 */
	private String getExactPropertyBeanName(String _parameter) {
		String parameter = _parameter.substring(0,1).toLowerCase() + _parameter.substring(1,_parameter.length());
		
		return currentPropertyName.toString() + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter;
	}

	/**
	 * @param _parameter
	 * @return Returns the exact property bean array name
	 */
	private String getExactPropertyBeanArrayName(String _parameter) {
		if (currentPropertyBeanArray == null) return null;
		 
		String parameter = _parameter.substring(0,1).toLowerCase() + _parameter.substring(1,_parameter.length());
		
		return currentPropertyName.toString() + HABITAT4J_PROPERTY_TYPE_DELIMITER + (currentPropertyBeanArray.size()-1) + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter;
	}

	/**
	 * @param _parameter
	 * @return Returns the exact property bean hash name
	 */
	private String getExactPropertyBeanHashName(String _parameter) {
		if (currentPropertyBeanHash == null) return null;
		 
		String parameter = _parameter.substring(0,1).toLowerCase() + _parameter.substring(1,_parameter.length());
		
		return currentPropertyName.toString() + HABITAT4J_PROPERTY_TYPE_DELIMITER + currentPropertyBeanHashKey + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter;
	}

	/**
	 * @param definitionObject
	 * @throws DecoderException
	 * @throws DecryptorException
	 */
	private void invokeDefinitionInitializer(Object definitionObject) throws DecoderException, DecryptorException {
		if (definitionObject instanceof DecoderIF) {
			DecoderIF decoder = (DecoderIF) definitionObject;
			decoder.initialize(serverIdentity.getApplicationName(),propertyList.getListName());
			
		} else if (definitionObject instanceof DecryptorIF) {
			DecryptorIF decryptor = (DecryptorIF) definitionObject;
			decryptor.initialize(serverIdentity.getApplicationName(),propertyList.getListName());
		}
	}
	
	/**
	 * @param atts - the attributes from the XML element
	 * @param interfaceClass - the Class designated for this definition
	 * @param logMethodName - the methodName touse in logging
	 * @return Returns a Class object.
	 */
	private Object getDefinitionObject(Attributes atts, Class interfaceClass, String logMethodName) {
		String _clazzName = atts.getValue("class");
		if (_clazzName == null || _clazzName.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) return null;
		String clazzName = prependStringWithStringBufferContents(_clazzName,currentDefinitionPackage,".");

		boolean alwaysNewInstance = false;
		String _alwaysNewInstance = atts.getValue("alwaysNewInstance");
		if (_alwaysNewInstance != null && ( 
		       _alwaysNewInstance.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_TRUE)
			|| _alwaysNewInstance.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_ON)
			|| _alwaysNewInstance.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_ON) )) {
		  alwaysNewInstance = true;
       }
		
		Class clazz = null;
		try {
			Class _clazz = Class.forName(clazzName);
			
			if (interfaceClass.isAssignableFrom(_clazz)) {
				clazz = _clazz;
			}
			
		} catch (ClassNotFoundException cnfe) {
			logger.error(logMethodName + clazzName + " - " + cnfe);
			return null;
		}
		
		if (clazz == null) {
			logger.error(logMethodName + clazzName + " - does not implement interface class " + interfaceClass.getName());
			return null;			
		}

		if (alwaysNewInstance) {
			return clazz;
		}
		
		Object definitionObject = null;
		
		try {
			definitionObject = clazz.newInstance();
			invokeDefinitionInitializer(definitionObject);
			
		} catch (DecoderException decoderE) {
			logger.error(logMethodName + clazzName + " - could not initialize decoder object: " + decoderE);
			
		} catch (DecryptorException decryptorE) {
			logger.error(logMethodName + clazzName + " - could not initialize decryptor object: " + decryptorE);
			
		} catch (InstantiationException ie) {
			logger.error(logMethodName + clazzName + " - could not instantiate class: " + ie);
			
		} catch (IllegalAccessException iae) {
			logger.error(logMethodName + clazzName + " - could not instantiate class: " + iae);
		}
		
		return definitionObject;
	}

	/**
	 * @param atts - the attributes from the XML element
	 * @param interfaceClass - the Class designated for this definition
	 * @param logMethodName - the methodName touse in logging
	 * @return Returns a Class object.
	 */
	private Class getDefinitionClass(Attributes atts, Class interfaceClass, String logMethodName) {
		String _clazzName = atts.getValue("class");
		if (_clazzName == null || _clazzName.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) return null;
		String clazzName = prependStringWithStringBufferContents(_clazzName,currentDefinitionPackage,".");

		Class clazz = null;
		try {
			Class _clazz = Class.forName(clazzName);

			if (interfaceClass == null || interfaceClass.isAssignableFrom(_clazz)) {
				clazz = _clazz;
			}
			
		} catch (ClassNotFoundException cnfe) {
			logger.error(logMethodName + clazzName + " - " + cnfe);
		}
		
		if (clazz == null && interfaceClass != Object.class) {
			logger.error(logMethodName + clazzName + " - does not implement interface class " + interfaceClass.getName());
		}

		return clazz;
	}
	
	/**
	 * @return Returns whether the current context is evident.
	 */
	private boolean isCurrentContext() {
		return currentContextFlag;
	}
	
	/**
	 * @param serverIdentity The serverIdentity to set.
	 */
	private void setServerIdentity(ServerIdentity serverIdentity) {
		this.serverIdentity = serverIdentity;
	}
	
	// ###################################### INVOKER HANDLERS ########################################

	protected void invokeElementInitializer(String methodName) {
		if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_INIT_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_INIT_METHOD_PREFIX)) {
			super.invokeElementInitializer(methodName);
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_INIT_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_INIT_METHOD_PREFIX)) {
				
			super.invokeElementInitializer(methodName);
			
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_INIT_METHOD_PREFIX.length()
				&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_INIT_METHOD_PREFIX)) {
				
			if (!isCurrentContext()) return;
				
			initDecoderDecryptorIds();
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_HASH_INIT_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_HASH_INIT_METHOD_PREFIX)) {
			super.invokeElementInitializer(methodName);
				
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_HASH_ITEM_INIT_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_INIT_METHOD_PREFIX)) {
			super.invokeElementInitializer(methodName);
			
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_HASH_ITEM_INIT_METHOD_PREFIX.length()
				&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_INIT_METHOD_PREFIX)) {
				
			if (!isCurrentContext()) return;
				
			initDecoderDecryptorIds();
				
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_INIT_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_INIT_METHOD_PREFIX)) {
					
			if (!isCurrentContext()) return;

			initDecoderDecryptorIds();

		} else {
			super.invokeElementInitializer(methodName);
		}
	}

	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.sax.AbstractBaseHandler#invokeSetter(java.lang.String, org.xml.sax.Attributes)
	 */
	protected void invokeSetter(String methodName,Attributes atts) {
		if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_SETTER_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_SETTER_METHOD_PREFIX)) {
			super.invokeSetter(methodName,atts);
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX)) {

			super.invokeSetter(methodName,atts);
			
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX)) {
			
			if (!isCurrentContext()) return;

			String parameter =
				methodName.substring(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
				methodName.substring(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()+1,methodName.length());

			setDecoderDecryptorIds(atts);
		
			if (atts.getIndex(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME) > -1) {
				String _value = atts.getValue(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME);
				String value = mangleValue(getExactPropertyBeanName(parameter),_value);
				setBeanParameter(currentPropertyBean,parameter,value);
			}
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_HASH_SETTER_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_HASH_SETTER_METHOD_PREFIX)) {
			super.invokeSetter(methodName,atts);
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX)) {

			super.invokeSetter(methodName,atts);
			
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX)) {
			
			if (!isCurrentContext()) return;

			String parameter =
				methodName.substring(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
				methodName.substring(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()+1,methodName.length());

			setDecoderDecryptorIds(atts);
		
			if (atts.getIndex(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME) > -1) {
				String _value = atts.getValue(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME);
				String value = mangleValue(getExactPropertyBeanName(parameter),_value);
				setBeanParameter(currentPropertyBean,parameter,value);
			}			
			
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX)) {
					
			if (!isCurrentContext()) return;

			String parameter =
				methodName.substring(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
				methodName.substring(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()+1,methodName.length());

			setDecoderDecryptorIds(atts);

			if (atts.getIndex(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME) > -1) {
				String _value = atts.getValue(HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME);
				String value = mangleValue(getExactPropertyBeanName(parameter),_value);
				setBeanParameter(currentPropertyBean,parameter,value);
			}

		} else if (methodName.length()>0) {
			super.invokeSetter(methodName,atts);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.sax.AbstractBaseHandler#invokeSetter(java.lang.String, java.lang.String)
	 */
	protected void invokeSetter(String methodName,String _value) {
		if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_SETTER_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_SETTER_METHOD_PREFIX)) {
			
			// This particular case is not handled.
			
		} else if (methodName.length() == HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()
					&& methodName.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX)) {

			// This particular case is not handled.

		} else if (methodName.length() == HABITAT4J_PROPERTY_ARRAY_ITEM_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_ARRAY_ITEM_METHOD_PREFIX)) {

			if (!isCurrentContext()) return;

			String value = mangleValue(getExactPropertyArrayName(),_value);
			
			super.invokeSetter(methodName,value);		

		} else if (methodName.length() == HABITAT4J_PROPERTY_HASH_ITEM_METHOD_PREFIX.length()
				&& methodName.equals(HABITAT4J_PROPERTY_HASH_ITEM_METHOD_PREFIX)) {

			if (!isCurrentContext()) return;

			String value = mangleValue(getExactPropertyHashName(),_value);
			
			super.invokeSetter(methodName,value);		

		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX)) {
			
			if (!isCurrentContext()) return;

			String parameter =
				methodName.substring(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
				methodName.substring(HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX.length()+1,methodName.length());

			String value = mangleValue(getExactPropertyBeanArrayName(parameter),_value);
			
			setBeanParameter(currentPropertyBean,parameter,value);
				
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()
				&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX)) {
				
				if (!isCurrentContext()) return;

				String parameter =
					methodName.substring(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
					methodName.substring(HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX.length()+1,methodName.length());

				String value = mangleValue(getExactPropertyBeanHashName(parameter),_value);
				
				setBeanParameter(currentPropertyBean,parameter,value);
					
		} else if (methodName.length() > HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()
			&& methodName.startsWith(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX)) {

			if (!isCurrentContext()) return;

			String parameter =
				methodName.substring(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length(),HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()+1).toUpperCase()+
				methodName.substring(HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX.length()+1,methodName.length());

			String value = mangleValue(getExactPropertyBeanName(parameter),_value);
						
			setBeanParameter(currentPropertyBean,parameter,value);
			
		} else if (methodName.length()>0) {
			String value = mangleValue(currentPropertyName.toString(),_value);
			
			super.invokeSetter(methodName,value);
		}	
	}

	// ###################################### INTIALIZERS ########################################

	/**
	 * 
	 */
	public void initPropertyListContextPropertyBean() {
		if (!isCurrentContext()) return;
		
		currentPropertyBean = null;
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyBeanArray() {
		if (!isCurrentContext()) return;

		currentPropertyBeanArray = null;
		currentPropertyBeanArrayInstance = HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
		currentPropertyBeanArrayId.setLength(0);
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyBeanArrayItem() {
		if (!isCurrentContext()) return;

		String logMethodName = this.getClass().getName() + ".initPropertyListContextPropertyBeanArrayItem() - ";
		
		currentPropertyBean = propertyList.getNewPropertyBeanInstance(currentPropertyBeanArrayId.toString());
		
		if (currentPropertyBeanArray != null) {
			if (currentPropertyBeanArrayInstance.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND)) {				
				currentPropertyBeanArray.add(0,currentPropertyBean);
				
			} else if (currentPropertyBeanArrayInstance.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND)
					   || currentPropertyBeanArrayInstance.equals(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
				currentPropertyBeanArray.add(currentPropertyBean);				
				
			} else {
				logger.error(logMethodName + "PropertyBeanArray \"" + currentPropertyName + "\" has invalid instance attribute \"" + currentPropertyBeanArrayInstance + "\", skipped");
			}
		
			initDecoderDecryptorIds();
		}
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyBeanHash() {
		if (!isCurrentContext()) return;

		currentPropertyBeanHash = null;
		currentPropertyBeanHashId.setLength(0);
		currentPropertyHashInstance = null;
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyBeanHashItem() {
		if (!isCurrentContext()) return;

		currentPropertyBean = null;
		currentPropertyBeanHashKey = null;
		
		initDecoderDecryptorIds();
	}

	/**
	 * 
	 */
	public void initPropertyListContext() {
		currentContextPrefix.setLength(0);
		
		if (!isCurrentContext()) return;

		currentContextFlag = true;
	}
	
	/**
	 * 
	 */
	public void initPropertyListDefinitions() {
		currentDefinitionPackage.setLength(0);
	}

	/**
	 * 
	 */
	public void initPropertyListDefinitionsPragma() {
		currentPragmaName.setLength(0);
		currentPragmaValue.setLength(0);
	}

	/**
	 * 
	 */
	public void initPropertyListContextProperty() {
		if (!isCurrentContext()) return;

		initDecoderDecryptorIds();
		propertyValueSetInAttributeFlag=false;
	}
	
	/**
	 * 
	 */
	public void initPropertyListContextSystemProperty() {
		if (!isCurrentContext()) return;

		initPropertyListContextProperty();
	}

	/**
	 * 
	 */
	public void initPropertyListContextSecurityProperty() {
		if (!isCurrentContext()) return;

		initPropertyListContextProperty();
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyArray() {
		if (!isCurrentContext()) return;

		currentPropertyArray = null;
		currentPropertyArrayInstance = HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyArrayItem() {
		if (!isCurrentContext()) return;
		
		initDecoderDecryptorIds();
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyHash() {
		if (!isCurrentContext()) return;

		currentPropertyHash = null;
		currentPropertyHashInstance = null;
	}

	/**
	 * 
	 */
	public void initPropertyListContextPropertyHashItem() {
		if (!isCurrentContext()) return;
		
		currentPropertyHashKey = null;
		initDecoderDecryptorIds();
	}

	// ###################################### SETTERS ########################################

	public void setPropertyList(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setPropertyList(Attributes atts) - ";

		String _listName = atts.getValue("name");
		if (_listName != null && !_listName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			propertyList.setListName(_listName);
		}

		String _listVersion = atts.getValue("version");
		if (_listVersion != null && !_listVersion.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			propertyList.setListVersion(_listVersion);
		}
		
		String _listModifiedBy = atts.getValue("modifiedBy");
		if (_listModifiedBy != null && !_listModifiedBy.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			propertyList.setListModifiedBy(_listModifiedBy);
		}
		
		String _listReloadSerial = atts.getValue("reloadSerial");
		if (_listReloadSerial != null && !_listReloadSerial.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			try {
				int listReloadSerialInt = new Integer(_listReloadSerial).intValue();
				propertyList.setListReloadSerial(listReloadSerialInt);
			
			} catch (NumberFormatException nfe) {
				logger.error(logMethodName + "reloadSerial attribute not an integer: " + nfe);
			}
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextPropertyBean(Attributes atts) {
		if (!isCurrentContext()) return;
		
		String logMethodName = this.getClass().getName() + ".setPropertyListContextPropertyBean(Attributes atts) - ";

		String id = atts.getValue(HABITAT4J_PROPERTY_BEAN_ID_ATTRIBUTE_NAME);
		
		String _name = atts.getValue(HABITAT4J_PROPERTY_BEAN_NAME_ATTRIBUTE_NAME);
		String name = setCurrentPropertyName(_name);
		
		String instance = atts.getValue(HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_NAME);
		if (instance == null || instance.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) instance = HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
		
		setDecoderDecryptorIds(atts);
		
		if (id != null && name != null && !id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			if (propertyList.isPropertyBeanIdValid(id)) {
				
				boolean createNew = false;
				
				if (propertyList.containsKey(name)) {
					if (instance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
						propertyList.remove(name);
						createNew = true;
						
					} else if (instance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_REVISE)) {
						try {
							currentPropertyBean = propertyList.getPropertyBean(name,false);
							
						} catch (PropertyNotAvailableException pnae) {
							currentPropertyBean = null;
						}
					}
					
				} else {
					createNew = true;
				}

				if (createNew) {
					currentPropertyBean = propertyList.getNewPropertyBeanInstance(id);
					
					if (currentPropertyBean != null) {
						propertyList.put(name,currentPropertyBean);
					}
				}

				if (currentPropertyBean == null) {
					logger.error(logMethodName + "Could not create property bean instance for id=" + id);
				}
				
			} else {
				logger.error(logMethodName + "Unknown id=" + id + ", this must be defined in your PropertyList file.");													
			}
		}
	}	

	/**
	 * @param atts
	 */
	public void setPropertyListContextPropertyBeanArray(Attributes atts) {
		if (!isCurrentContext()) return;

		String logMethodName = this.getClass().getName() + ".setPropertyListContextPropertyBeanArray(Attributes atts) - ";

		if (!isCurrentContext()) {
			return;
		}
		
		String id = atts.getValue(HABITAT4J_PROPERTY_BEAN_ID_ATTRIBUTE_NAME);

		String _name = atts.getValue(HABITAT4J_PROPERTY_BEAN_NAME_ATTRIBUTE_NAME);	
		String name = setCurrentPropertyName(_name);
		
		currentPropertyBeanArrayInstance = atts.getValue(HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_NAME);
		if (currentPropertyBeanArrayInstance == null || currentPropertyBeanArrayInstance.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) currentPropertyBeanArrayInstance = HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
		
		if (id != null && name != null && !id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			if (propertyList.isPropertyBeanIdValid(id)) {
				currentPropertyBeanArrayId.append(id);

				boolean createNew = false;
				
				if (propertyList.containsKey(name)) {
					if (currentPropertyBeanArrayInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
						propertyList.remove(name);
						createNew = true;
						
					} else if (currentPropertyBeanArrayInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND)
							|| currentPropertyBeanArrayInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND)) {
						currentPropertyBeanArray = (PropertyBeanArrayVector) propertyList.get(name);
					}
					
				} else {
					createNew = true;
				}
				
				if (createNew) {
					currentPropertyBeanArray = new PropertyBeanArrayVector();
					propertyList.put(name,currentPropertyBeanArray);
				}
				
			} else {
				logger.error(logMethodName + "Unknown id=" + id + ", this must be defined in your PropertyList file.");													
			}
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextPropertyBeanHash(Attributes atts) {
		if (!isCurrentContext()) return;

		String logMethodName = this.getClass().getName() + ".setPropertyListContextPropertyBeanHash(Attributes atts) - ";

		if (!isCurrentContext()) {
			return;
		}
		
		String id = atts.getValue(HABITAT4J_PROPERTY_BEAN_ID_ATTRIBUTE_NAME);

		String _name = atts.getValue(HABITAT4J_PROPERTY_BEAN_NAME_ATTRIBUTE_NAME);	
		String name = setCurrentPropertyName(_name);
		
		currentPropertyBeanHashInstance = atts.getValue(HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_NAME);
		if (currentPropertyBeanHashInstance == null || currentPropertyBeanHashInstance.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) currentPropertyBeanHashInstance = HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
		
		if (id != null && name != null && !id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			if (propertyList.isPropertyBeanIdValid(id)) {
				currentPropertyBeanHashId.append(id);

				boolean createNew = false;
				
				if (propertyList.containsKey(name)) {
					if (currentPropertyBeanHashInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
						propertyList.remove(name);
						createNew = true;
						
					} else if (currentPropertyBeanHashInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REVISE)) {
						currentPropertyBeanHash = (PropertyBeanHash) propertyList.get(name);
					}
					
				} else {
					createNew = true;
				}
				
				if (createNew) {
					currentPropertyBeanHash = new PropertyBeanHash();
					propertyList.put(name,currentPropertyBeanHash);
				}
				
			} else {
				logger.error(logMethodName + "Unknown id=" + id + ", this must be defined in your PropertyList file.");													
			}
		}
	}

	public void setPropertyListContextPropertyBeanHashItem(Attributes atts) {
		if (!isCurrentContext()) return;			

		String logMethodName = this.getClass().getName() + " setPropertyListContextPropertyBeanHashItem(atts) - ";
		
		setDecoderDecryptorIds(atts);		

		currentPropertyBeanHashKey = atts.getValue(HABITAT4J_PROPERTY_HASH_KEY_ATTRIBUTE_NAME);
		
		if (currentPropertyBeanHash.containsKey(currentPropertyBeanHashKey)) {
			currentPropertyBean = currentPropertyBeanHash.get(currentPropertyBeanHashKey);

			if (currentPropertyBeanHashInstance == null || currentPropertyBeanHashInstance.equals(HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
				logger.warn(logMethodName + "PropertyBeanHash key is already in hash, skipping property name \"" + currentPropertyName + "\" item with key \"" + currentPropertyBeanHashKey + "\"");
				return;
			}
			
		} else {
			currentPropertyBean = propertyList.getNewPropertyBeanInstance(currentPropertyBeanHashId.toString());
		}
		
		currentPropertyBeanHash.put(currentPropertyBeanHashKey,currentPropertyBean);	
	}
	
	/**
	 * @param atts
	 */
	public void setPropertyListDefinitions(Attributes atts) {
		String _package = atts.getValue(HABITAT4J_DEFINITIONS_PACKAGE);
		
		if (_package != null && !_package.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			currentDefinitionPackage.append(_package);		
		}		
	}

	/**
	 * @param atts
	 * @param logMethodName
	 * @return Returns the copyMethod.
	 */
	protected String getPropertyBeanCopyMethod(Attributes atts, String logMethodName) {
		String copyMethod = atts.getValue(HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_ATTRIBUTE_NAME);
		
		if (copyMethod == null || copyMethod.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return null;
		}

		String _copyMethod = null;
		
		if (copyMethod.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_REFLECT)) {
			_copyMethod = HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_REFLECT;
			
		} else if (copyMethod.equalsIgnoreCase(HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_SERIALIZE)) {
			_copyMethod = HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_SERIALIZE;
			
		} else {
			logger.warn(logMethodName + "PropertyBean descriptor's copyMethod attribute \"" + copyMethod + "\" is invalid.  Using default copyMethod of \"" + HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_DEFAULT + "\"");
		}
		
		return _copyMethod; 
	}
	
	/**
	 * @param atts
	 */
	public void setPropertyListDefinitionsPropertyBean(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsPropertyBean(Attributes atts) - ";

		String id = atts.getValue(HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_ID_ATTRIBUTE_NAME);
		
		if (id == null || id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return;
		}
				
		Class definitionClass = getDefinitionClass(atts,Object.class,logMethodName);
		
		if (definitionClass != null) {
			propertyList.addPropertyBeanDefinition(id,definitionClass);
		}
		
		String copyMethod = getPropertyBeanCopyMethod(atts,logMethodName);
		
		if (copyMethod != null) {
			propertyList.addPropertyBeanCopyMethodDefinition(definitionClass,copyMethod);
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListDefinitionsDecoder(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsDecoder(Attributes atts) - ";

		String id = atts.getValue("id");
		if (id == null || id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) return;
				
		Object definitionObject = getDefinitionObject(atts,DecoderIF.class,logMethodName);
		if (definitionObject != null) {
			propertyList.addDecoderDefinition(id,definitionObject);	
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListDefinitionsDecryptor(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsDecryptor(Attributes atts) - ";

		String id = atts.getValue("id");
		if (id == null || id.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) return;
				
		Object definitionObject = getDefinitionObject(atts,DecryptorIF.class,logMethodName);
		if (definitionObject != null) {
			propertyList.addDecryptionDefinition(id,definitionObject);	
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListDefinitionsReloadEventHandler(Attributes atts) {
		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsReloadEventHandler(Attributes atts) - ";

		Object reloadEventHandlerObject = getDefinitionObject(atts,ReloadEventHandlerIF.class,logMethodName);
		if (reloadEventHandlerObject != null) {
			propertyList.setReloadEventHandler(reloadEventHandlerObject);	
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListDefinitionsPragma(Attributes atts) {
//		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsPragma(Attributes atts) - ";

		String name = atts.getValue("name");
		if (name == null) return;

		currentPragmaName.append(name);

		String value = atts.getValue("value");
		if (value != null) {
			currentPragmaValue.append(value);
		}
	}

	/**
	 * @param value
	 */
	public void setPropertyListDefinitionsPragma(String value) {
		String logMethodName = this.getClass().getName() + ".setPropertyListDefinitionsPragma(String): ";
		
		if (value != null) {
			currentPragmaValue.setLength(0);
			currentPragmaValue.append(value);
		}
		
		if (currentPragmaName.length() < 1) {
			return;
		}

		if (serverIdentity.getPragmaDefinition(currentPragmaName.toString()) == null) {
			propertyList.setPragmaDefinition(currentPragmaName.toString(),currentPragmaValue.toString());
			
		} else {
			if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_SERVER_IDENTITY_PRAGMA_OVERRIDE)) {
				propertyList.setPragmaDefinition(currentPragmaName.toString(),currentPragmaValue.toString());
				
			} else {
				logger.warn(logMethodName + "attempt to override ServerIdentity pragma definition \"" + currentPragmaName.toString() + "\" ignored");
			}
		}
	}

	/**
	 * @param value1 - first value
	 * @param value2 - second value
	 * @return Returns whether the two provided Strings are equal, taking into account HABITAT4J_FEATURE_CASE_INSENSITIVE_CONTEXT_MATCHING.
	 */
	private boolean compareStrings(String value1, String value2) {
		boolean match = false;

		boolean wildCardMatching = !Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DISABLE_WILDCARD_CONTEXT_MATCHING);

		if (wildCardMatching) {
			if (value2.equals("*")) {
				match = true;
			}
		}
			
		if (!match) {
			boolean caseSensitive = Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_CASE_SENSITIVE_CONTEXT_MATCHING);
			
			String _value2 = value2;
			
			boolean reverseLogic = false;
			
			if (wildCardMatching) {
				if (value2.startsWith("!")) {
					_value2 = value2.substring(1);
					reverseLogic = true;
				}
			}
			
			if ((!caseSensitive && value1.equalsIgnoreCase(_value2)) || (caseSensitive && value1.equals(_value2))) {
				match = true;
			}
			
			if (reverseLogic) {
				match = !match;
			}
		}
		
		return match;
	}
	
	/**
	 * @param atts
	 */
	public void setPropertyListContext(Attributes atts) {
		currentContextFlag = true;

		String roleName = atts.getValue(HABITAT4J_CONTEXT_ROLE_NAME);

		for (int i=0; i<atts.getLength(); i++) {
			boolean matchedAttribute = false;

			String name = atts.getLocalName(i);
			String value = atts.getValue(i);

			if (name != null && name.equals(HABITAT4J_CONTEXT_PREFIX) && value != null && !value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				currentContextPrefix.append(value);
				continue;
			}
						
			String fixedValue = (String) serverIdentity.getFixed().get(name);
			if (fixedValue != null) {
				matchedAttribute = true;
			}
			if (fixedValue != null && !compareStrings(fixedValue,value)) {
				currentContextFlag = false;
				break;
			}
			if (matchedAttribute) continue;

			fixedValue = propertyList.getPragmaDefinition(name);
			if (fixedValue != null) {
				matchedAttribute = true;
			}
			if (fixedValue != null && !compareStrings(fixedValue,value)) {
				currentContextFlag = false;
				break;
			}
			
			if (matchedAttribute) continue;
			
			fixedValue = serverIdentity.getPragmaDefinition(name);
			if (fixedValue != null) {
				matchedAttribute = true;
			}
			if (fixedValue != null && !compareStrings(fixedValue,value)) {
				currentContextFlag = false;
				break;
			}
			if (matchedAttribute) continue;
			
			if (roleName != null && !roleName.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				ServerIdentityRole serverRole = serverIdentity.getRole(roleName);
				if (serverRole != null) {
					fixedValue = (String) serverRole.get(name);
					if (fixedValue != null) matchedAttribute = true;
					if (fixedValue != null && !compareStrings(fixedValue,value)) {
						currentContextFlag = false;
						break;
					}
				}
			}
			
			if (matchedAttribute == false) {
				currentContextFlag = false;
				break;
			}
		}
	}

	/**
	 * @param name - the System property name
	 * @param value - the System property value
	 */
	private void queueSystemProperty(String name, String value) {
		if (systemPropertyQueue == null) {
			systemPropertyQueue = new Hashtable();
		}
		
		systemPropertyQueue.put(name,value);
	}

	/**
	 * @param name - the Security property name
	 * @param value - the Security property value
	 */
	private void queueSecurityProperty(String name, String value) {
		if (securityPropertyQueue == null) {
			securityPropertyQueue = new Hashtable();
		}
		
		securityPropertyQueue.put(name,value);
	}

	/**
	 * @param propertyKind - specifies the type of property entry
	 * @param value - specifies the value of the property
	 */
	private void setProperty(byte propertyKind, String value) {
		String name = currentPropertyName.toString();
		
		switch(propertyKind) {
			case HABITAT4J_PROPERTY_KIND_HABITAT4J :
				if (value == null && propertyList.containsKey(name)) {
					propertyList.remove(name);
					
				} else if (value != null) {
					propertyList.put(name,value);
				}
			break;
			
			case HABITAT4J_PROPERTY_KIND_SYSTEM:
				queueSystemProperty(name,value);
			break;
			
			case HABITAT4J_PROPERTY_KIND_SECURITY:
				queueSecurityProperty(name,value);
			break;
		}
		
		initDecoderDecryptorIds();
		propertyValueSetInAttributeFlag=false;
	}
	
	/**
	 * @param propertyKind
	 * @param atts
	 */
	private void setPropertyListContextProperty(byte propertyKind, Attributes atts) {
		if (!isCurrentContext()) return;

		String _name = atts.getValue(HABITAT4J_PROPERTY_NAME_ATTRIBUTE_NAME);
		if (_name != null && !_name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			setCurrentPropertyName(_name);
			
		} else {
			return;
		}
		
		String name = currentPropertyName.toString();
		
		setDecoderDecryptorIds(atts);

		String _value = atts.getValue(HABITAT4J_PROPERTY_VALUE_ATTRIBUTE_NAME);

		if (_value != null) {
			
			String value = mangleValue(name,_value);
			setProperty(propertyKind,value);
			propertyValueSetInAttributeFlag=true;
		}			
	}

	/**
	 * @param value
	 */
	private void setPropertyListContextPropertyName(String value) {
		if (!isCurrentContext()) return;

		String _name = value;
		if (_name != null && !_name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			setCurrentPropertyName(_name);
		}
	}

	/**
	 * @param propertyKind
	 * @param value
	 */
	private void setPropertyListContextProperty(byte propertyKind, String value) {
		if (!isCurrentContext()) return;

		if (propertyValueSetInAttributeFlag) {
			return;
		}
			
		String name = currentPropertyName.toString();
		if (name != null && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			setProperty(propertyKind,value);
		}
	}

	/**
	 * @param propertyKind
	 * @param value
	 */
	private void setPropertyListContextPropertyValue(byte propertyKind, String value) {
		if (!isCurrentContext()) return;

		if (propertyValueSetInAttributeFlag) {
			return;
		}
			
		String name = currentPropertyName.toString();
		if (name != null && !name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			setProperty(propertyKind,value);
		}
	}

	/**
	 * @param atts
	 */
	private void setPropertyListContextPropertyValue(Attributes atts) {
		if (!isCurrentContext()) return;

		setDecoderDecryptorIds(atts);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextProperty(Attributes atts) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_HABITAT4J,atts);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextProperty(String value) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_HABITAT4J,value);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextPropertyValue(String value) {
		setPropertyListContextPropertyValue(HABITAT4J_PROPERTY_KIND_HABITAT4J,value);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextSystemProperty(Attributes atts) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_SYSTEM,atts);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSystemPropertyName(String value) {
		setPropertyListContextPropertyName(value);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSystemProperty(String value) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_SYSTEM,value);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSystemPropertyValue(String value) {
		setPropertyListContextPropertyValue(HABITAT4J_PROPERTY_KIND_SYSTEM,value);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextSystemPropertyValue(Attributes atts) {
		setPropertyListContextPropertyValue(atts);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextSecurityProperty(Attributes atts) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_SECURITY,atts);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSecurityPropertyName(String value) {
		setPropertyListContextPropertyName(value);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSecurityProperty(String value) {
		setPropertyListContextProperty(HABITAT4J_PROPERTY_KIND_SECURITY,value);
	}
	
	/**
	 * @param value
	 */
	public void setPropertyListContextSecurityPropertyValue(String value) {
		setPropertyListContextPropertyValue(HABITAT4J_PROPERTY_KIND_SECURITY,value);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextSecurityPropertyValue(Attributes atts) {
		setPropertyListContextPropertyValue(atts);
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextPropertyArray(Attributes atts) {
		if (!isCurrentContext()) return;

		String _name = atts.getValue(HABITAT4J_PROPERTY_NAME_ATTRIBUTE_NAME);
			
		currentPropertyArrayInstance = atts.getValue(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_NAME);
		if (currentPropertyArrayInstance == null || currentPropertyArrayInstance.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) currentPropertyArrayInstance = HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;
		
		if (_name != null && !_name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			String name = setCurrentPropertyName(_name);

			boolean createNew = false;
			
			if (propertyList.containsKey(name)) {
				if (currentPropertyArrayInstance.equalsIgnoreCase(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
					propertyList.remove(name);
					createNew = true;
					
				} else if (currentPropertyArrayInstance.equals(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND) 
						  || currentPropertyArrayInstance.equals(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND)) {
					currentPropertyArray = (PropertyArrayVector) propertyList.get(name);
				}
			} else {
				createNew = true;
			}
			
			if (createNew) {
				currentPropertyArray = new PropertyArrayVector();
				propertyList.put(name,currentPropertyArray);								
			}			
		}
	}

	/**
	 * @param atts - the value to set
	 */
	public void setPropertyListContextPropertyArrayItem(Attributes atts) {
		if (!isCurrentContext()) return;			

		setDecoderDecryptorIds(atts);
	}

	/**
	 * @param value - the value to set
	 */
	public void setPropertyListContextPropertyArrayItem(String value) {
		if (!isCurrentContext()) return;

		String logMethodName = this.getClass().getName() + ".setPropertyListContextPropertyArrayItem(String) - ";
				
		if (currentPropertyArrayInstance.equals(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND)) {
			currentPropertyArray.add(0,value);
			
		} else if (currentPropertyArrayInstance.equals(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND)
				 || currentPropertyArrayInstance.equals(HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
			currentPropertyArray.add(value);
			
		} else {			
			logger.error(logMethodName + "PropertyArray \"" + currentPropertyName + "\" has invalid instance attribute \"" + currentPropertyBeanArrayInstance + "\", skipped");
		}
	}

	/**
	 * @param atts
	 */
	public void setPropertyListContextPropertyHash(Attributes atts) {
		if (!isCurrentContext()) return;

		String _name = atts.getValue(HABITAT4J_PROPERTY_NAME_ATTRIBUTE_NAME);
			
		currentPropertyHashInstance = atts.getValue(HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_NAME);
		if (currentPropertyHashInstance == null || currentPropertyHashInstance.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) currentPropertyHashInstance = HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_DEFAULT;

		if (_name != null && !_name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			String name = setCurrentPropertyName(_name);
			
			boolean createNew = false;
			
			if (propertyList.containsKey(name)) {
				if (currentPropertyHashInstance.equals(HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
					propertyList.remove(name);
					createNew = true;
					
				} else if (currentPropertyHashInstance.equals(HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REVISE)) {
					currentPropertyHash = (PropertyHash) propertyList.get(name);
				}
			} else {
				createNew = true;
			}
			
			if (createNew) {
				currentPropertyHash = new PropertyHash();
				propertyList.put(name,currentPropertyHash);
			}
		}
	}

	/**
	 * @param atts - the value to set
	 */
	public void setPropertyListContextPropertyHashItem(Attributes atts) {
		if (!isCurrentContext()) return;			

		currentPropertyHashKey = atts.getValue(HABITAT4J_PROPERTY_HASH_KEY_ATTRIBUTE_NAME);
		
		setDecoderDecryptorIds(atts);
	}

	/**
	 * @param value - the value to set
	 */
	public void setPropertyListContextPropertyHashItem(String value) {
		if (!isCurrentContext()) return;
		
		String logMethodName = this.getClass().getName() + ".setPropertyListContextPropertyHashItem: ";
		
		if (currentPropertyHashKey == null) {
			logger.warn(logMethodName + "PropertyHash key is null, skipping property name \"" + currentPropertyName + "\" item with value \"" + value + "\"");
			return;
		}
		
		if (currentPropertyHash.containsKey(currentPropertyHashKey)) {
			if (currentPropertyHashInstance == null || currentPropertyHashInstance.equals(HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE)) {
				logger.warn(logMethodName + "PropertyHash key is already in hash, skipping property name \"" + currentPropertyName + "\" item with key \"" + currentPropertyHashKey + "\"");
				return;
			}
		}
		
		if (value == null) {
			value = new String();
		}
		
		currentPropertyHash.put(currentPropertyHashKey,value);
	}
}
