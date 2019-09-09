package org.productivity.java.habitat4j.common;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;

import org.productivity.java.habitat4j.PropertyListReloadInhibitor;
import org.productivity.java.habitat4j.common.exception.BaseHandlerException;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.exception.PropertyNotAvailableException;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;
import org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF;
import org.productivity.java.habitat4j.common.sax.PropertyListHandler;
import org.productivity.java.habitat4j.compat.PropertyListXMLMap;
import org.productivity.java.habitat4j.compat.ServerIdentityXMLMap;

/**
 * PropertyListManager provides access to application-wide properties
 * that are loaded contextually based on information gained from the
 * ServerIdentityManager.
 * 
 * <p>The use of Habitat4J starts with the initialization of this Singleton
 * class by your application using the initialize method.</p> 
 * 
 * <p>PropertyListManager can manage one or more lists, accessed by a
 * PropertyList symbolic name.</p>
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
 * @version $Id: AbstractPropertyListManager.java,v 1.65 2007/02/25 18:52:04 cvs Exp $
 */
public abstract class AbstractPropertyListManager extends Hashtable implements Habitat4JConstants {
	protected Habitat4JLogger logger = null;
	
	private Hashtable features = null;
	private boolean initializedFlag = false;
	private PropertyListHandler propertyHandler = null;
	
	private ServerIdentity serverIdentity = null; 
	private ServerIdentityXMLMap serverIdentityMap = null;
	private AbstractServerIdentityManager serverIdentityManager = null;

	private Hashtable globalPropertyBeanDefinitions = new Hashtable();
	private Hashtable globalDecoderDefinitions = new Hashtable();
	private Hashtable globalDecryptorDefinitions = new Hashtable();
	private Object globalReloadEventHandler = null;
	
	private HashSet reloadablePropertyLists = null;

	public AbstractPropertyListManager() {
		super();

		logger = Habitat4JLogger.getInstance();
		features = new Hashtable();
		
		try {
			_setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_PROPERTY_BEAN_CLONEABLE_OBJECT_COPY,true);
			
		} catch (PropertyListHandlerException plhe) {
			// Hardcoded value above won't ever cause a PropertyListHandlerException
		}
	}

	/**
	 * This method is used to obtain a list of all properties from the "default" PropertyList. 
	 * 
	 * @return Returns an Enumeration of the list of property names
	 * @throws PropertyNotAvailableException
	 */
	protected Enumeration _getProperties() throws PropertyNotAvailableException {
		return _getProperties(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}

	/**
	 * This method is used to obtain a list of all properties from the PropertyList
	 * specified by the propertyListName parameter. 
	 * 
	 * @param propertyListName the PropertyList instance from which to retrieve
	 * @return Returns an Enumeration of the list of property names
	 * @throws PropertyNotAvailableException
	 */
	protected Enumeration _getProperties(String propertyListName) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			return null;
		}

		checkThatPropertyListExists(propertyListName);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getProperties();
	}

	/**
	 * This method is used to obtain a property's String value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the value of a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getProperty(String name) throws PropertyNotAvailableException {
		return _getProperty(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain properties from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the value of a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getProperty(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);
	
		return propertyList.getProperty(name);
	}

	/**
	 * This method is used to obtain properties from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the primitive int value of a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected int _getPropertyAsInt(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return -1;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);
	
		return propertyList.getPropertyAsInt(name);
	}

	/**
	 * This method is used to obtain a property's int value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the primitive int value of a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected int _getPropertyAsInt(String name) throws PropertyNotAvailableException {
		return _getPropertyAsInt(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain properties from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the primitive boolean value of a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyTrue(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}	

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyTrue(name);
	}

	/**
	 * This method is used to obtain a property's int value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the primitive boolean value of a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyTrue(String name) throws PropertyNotAvailableException {
		return _isPropertyTrue(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's array of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String[] _getPropertyArray(String name) throws PropertyNotAvailableException {
		return _getPropertyArray(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's array of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String[] _getPropertyArray(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.getPropertyArray(name);
	}

	/**
	 * This method is used to obtain a property's hash of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyHash _getPropertyHash(String name) throws PropertyNotAvailableException {
		return _getPropertyHash(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyHash _getPropertyHash(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.getPropertyHash(name);
	}

	/**
	 * This method is used to obtain a property's hash of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Properties _getPropertyHashAsJavaProperties(String name) throws PropertyNotAvailableException {
		return _getPropertyHashAsJavaProperties(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Properties _getPropertyHashAsJavaProperties(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		PropertyHash hash = propertyList.getPropertyHash(name);		
		Enumeration hashEnumeration = hash.keys();
		
		Properties properties = new Properties();
		
		while (hashEnumeration.hasMoreElements()) {
			String key = (String) hashEnumeration.nextElement();
			String value = (String) hash.get(key);
			
			properties.put(key,value);
		}
		
		return properties;
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyHashValue(String name,String key) throws PropertyNotAvailableException {
		return _getPropertyHashValue(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name,key);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyHashValue(String propertyListName,String name,String key) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.getPropertyHashValue(name,key);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanHashValue(String name,String key) throws PropertyNotAvailableException {
		return _getPropertyBeanHashValue(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name,key);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanHashValue(String propertyListName,String name,String key) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.getPropertyBeanHashValue(name,key,false);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanHashValueCopy(String name,String key) throws PropertyNotAvailableException {
		return _getPropertyBeanHashValueCopy(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name,key);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanHashValueCopy(String propertyListName,String name,String key) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.getPropertyBeanHashValue(name,key,true);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyHashValueSet(String name,String key) throws PropertyNotAvailableException {
		return _isPropertyHashValueSet(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name,key);
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from the PropertyHash.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyHashValueSet(String propertyListName,String name,String key) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}

		checkThatPropertyListExists(propertyListName,name);
		
		PropertyList propertyList = (PropertyList) get(propertyListName);

		return propertyList.isPropertyHashValue(name,key);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBean(String name) throws PropertyNotAvailableException {
		return _getPropertyBean(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBean(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBean(name,false);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyBeanHash _getPropertyBeanHash(String name) throws PropertyNotAvailableException {
		return _getPropertyBeanHash(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyBeanHash _getPropertyBeanHash(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBeanHash(name,false);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * the default PropertyList.  This method differs from the regular _getPropertyBean(..)
	 * in that it returns a copy (clone) of the instance that can be modified by the 
	 * application without altering the stored property.
	 * 
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanCopy(String name) throws PropertyNotAvailableException {
		return _getPropertyBeanCopy(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method returns an instance of a PropertyBean from
	 * a particular PropertyList.  This method differs from the regular _getPropertyBean(..)
	 * in that it returns a copy (clone) of the instance that can be modified by the 
	 * application without altering the stored property.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an instance of a PropertyBean.
	 * @throws PropertyNotAvailableException
	 */
	protected Object _getPropertyBeanCopy(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBean(name,true);
	}

	/**
	 * This method is used to obtain a property's array of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Object[] _getPropertyBeanArray(String name) throws PropertyNotAvailableException {
		return _getPropertyBeanArray(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's array of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Object[] _getPropertyBeanArray(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBeanArray(name,false);
	}

	/**
	 * This method is used to obtain a property's array of String values from the default PropertyList.
	 * This method differs from the regular _getPropertyBeanArray(..)
	 * in that it returns a copy (clone) of the array that can be modified by the 
	 * application without altering the stored PropertyBean Objects.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Object[] _getPropertyBeanArrayCopy(String name) throws PropertyNotAvailableException {
		return _getPropertyBeanArrayCopy(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's array of String values from a particular PropertyList.
	 * This method differs from the regular _getPropertyBeanArray(..)
	 * in that it returns a copy (clone) of the array that can be modified by the 
	 * application without altering the stored PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Object[] _getPropertyBeanArrayCopy(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBeanArray(name,true);
	}

	/**
	 * This method is used to obtain a property's array of String values from the default PropertyList.
	 * This method differs from the regular _getPropertyBeanArray(..)
	 * in that it returns a copy (clone) of the array that can be modified by the 
	 * application without altering the stored PropertyBean Objects.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyBeanHash _getPropertyBeanHashCopy(String name) throws PropertyNotAvailableException {
		return _getPropertyBeanHashCopy(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method is used to obtain a property's array of String values from a particular PropertyList.
	 * This method differs from the regular _getPropertyBeanArray(..)
	 * in that it returns a copy (clone) of the array that can be modified by the 
	 * application without altering the stored PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected PropertyBeanHash _getPropertyBeanHashCopy(String propertyListName,String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyBeanHash(name,true);
	}

	/**
	 * This method gets a property's class from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns the class of the property from the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Class _getPropertyClass(String name) throws PropertyNotAvailableException {
		return _getPropertyClass(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method gets a property's class from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the class of the property from a particular PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected Class _getPropertyClass(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyClass(name);
	}

	/**
	 * This method gets a property's classname from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns the classname of the property from the default PropertyList.
 	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyClassName(String name) throws PropertyNotAvailableException {
		return _getPropertyClassName(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}

	/**
	 * This method gets a property's classname from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the classname of the property from a particular PropertyList.
 	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyClassName(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return null;
		}			

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.getPropertyClassName(name);
	}

	/**
	 * This method is used to obtain the default instance of PropertyList.
	 * 
	 * @return Returns an Enumeration of the list of property names.
	 */
	protected PropertyList _getPropertyList() {
		return _getPropertyList(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);		
	}
	
	/**
	 * This method is used to obtain the instance of PropertyList specified
	 * by the propertyListName parameter.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns an Enumeration of the list of property names.
	 */
	protected PropertyList _getPropertyList(String propertyListName) {
		if (!_isInitialized(propertyListName)) {
			return null;
		}

		return (PropertyList) get(propertyListName);
	}
	
	/**
	 * This method renames a particular PropertyList instance.
	 * 
	 * @param oldPropertyListName - the name of the PropertyList to rename
	 * @param newPropertyListName - the new name of the PropertyList
	 * @throws PropertyListHandlerException
	 */
	protected void _renamePropertyList(String oldPropertyListName, String newPropertyListName) throws PropertyListHandlerException {
		String logMethodName = this.getClass().getName() + ".renamePropertyList(String applicationName) - ";
		String errorMessage = "cannot rename PropertyList \"" + oldPropertyListName + "\" to \"" + newPropertyListName + "\": "; 
		
		if (!containsKey(oldPropertyListName)) {
			throw new PropertyListHandlerException(logMethodName + errorMessage + "original PropertyList does not exist.");
		}

		if (containsKey(newPropertyListName)) {
			throw new PropertyListHandlerException(logMethodName + errorMessage + "new PropertyList already exists.");
		}
		
		PropertyList propertyList = (PropertyList) get(oldPropertyListName);		
		propertyList.setListName(newPropertyListName);
		
		put(newPropertyListName,propertyList);
		
		remove(oldPropertyListName);
	}
	
	/**
	 * This method removes a PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList to remove
	 * @throws PropertyListHandlerException
	 */
	protected void _removePropertyList(String propertyListName) throws PropertyListHandlerException {
		String logMethodName = this.getClass().getName() + ".removePropertyList(String applicationName) - ";
		String errorMessage = "cannot remove PropertyList \"" + propertyListName + "\" "; 

		if (!containsKey(propertyListName)) {
			throw new PropertyListHandlerException(logMethodName + errorMessage + "PropertyList does not exist");
		}
		
		remove(propertyListName);
	}

	/**
	 * This method returns the "modified by" of the default PropertyList.
	 * 
	 * @return Returns the "modified by" of the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyListModifiedBy() throws PropertyNotAvailableException {
		return _getPropertyListModifiedBy(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}

	/**
	 * This method returns the "modified by" of the PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the "modified by" of the PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected String _getPropertyListModifiedBy(String propertyListName) throws PropertyNotAvailableException {
		checkThatPropertyListExists(propertyListName);
		
		PropertyList propertyList = _getPropertyList(propertyListName);
		
		return propertyList.getListModifiedBy();
	}

	/**
	 * This method returns the reload serial enumeration of the default PropertyList.
	 * 
	 * @return Returns the reload serial enumeration of the default PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected int _getPropertyListReloadSerial() throws PropertyNotAvailableException {
		return _getPropertyListReloadSerial(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}

	/**
	 * This method returns the reload serial enumeration of the PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the reload serial enumeration of the PropertyList.
	 * @throws PropertyNotAvailableException
	 */
	protected int _getPropertyListReloadSerial(String propertyListName) throws PropertyNotAvailableException {
		checkThatPropertyListExists(propertyListName);
		
		PropertyList propertyList = _getPropertyList(propertyListName);
		
		return propertyList.getListReloadSerial();
	}

	/**
	 * This method returns the version of the default PropertyList.
	 * 
	 * @return Returns the version of the default PropertyList.
	 * @throws PropertyNotAvailableException 
	 */
	protected String _getPropertyListVersion() throws PropertyNotAvailableException {
		return _getPropertyListVersion(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}
	
	/**
	 * This method returns the version of the PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the version of the PropertyList.
	 * @throws PropertyNotAvailableException 
	 */
	protected String _getPropertyListVersion(String propertyListName) throws PropertyNotAvailableException {
		checkThatPropertyListExists(propertyListName);
		
		PropertyList propertyList = _getPropertyList(propertyListName);
		
		return propertyList.getListVersion();
	}

	/**
	 * @return Returns the serverIdentity instance.
	 */
	protected ServerIdentity _getServerIdentity() {
		if (!_isInitialized()) {
			return null;
		}

		return serverIdentity;
	}

	/**
	 * This method will initialize the ServerIdentityManager and generate the Singleton
	 * instance of this class. 
	 * 
	 * @param applicationName - the name of the calling application
	 * @param mode - the ServerIdentity mode identifier ("N" "F" or "J")
	 * @param map
	 * @param serverIdentityManager - the instance of ServerIdentityManager
	 */
	protected void _initialize(String applicationName, AbstractServerIdentityManager serverIdentityManager, String mode, ServerIdentityXMLMap map) {
		String logMethodName = this.getClass().getName() + ".initialize(String applicationName) - ";

		if (_isInitialized()) {
			logger.warn(logMethodName + "Attempt to re-initialize PropertyListManager ignored");
			return;
		}	

		this.serverIdentityMap = map;
			
		this.serverIdentityManager = serverIdentityManager;
			
		if (!serverIdentityManager._isInitialized()) {
			serverIdentityManager._initialize(applicationName,mode);
		}
			
		initializedFlag = true;
	}

	/**
	 * This method will initialize the ServerIdentityManager and generate the Singleton
	 * instance of this class. 
	 * 
	 * @param applicationName - the name of the calling application
	 * @param mode
	 * @param serverIdentityManager - the instance of ServerIdentityManager
	 */
	protected void _initialize(String applicationName, AbstractServerIdentityManager serverIdentityManager, String mode) {
		_initialize(applicationName,serverIdentityManager,mode,null);
	}

	/**
	 * This method will initialize the ServerIdentityManager and generate the Singleton
	 * instance of this class. 
	 * 
	 * @param applicationName - the name of the calling application
	 * @param serverIdentityManager - the instance of ServerIdentityManager
	 */
	protected void _initialize(String applicationName, AbstractServerIdentityManager serverIdentityManager) {
		_initialize(applicationName,serverIdentityManager,SERVER_IDENTITY_MODE_FILE,null);
	}


	/**
	 * This method is used to determine whether the default PropertyList instance has been initialized.
	 * 
	 * @return Returns whether this instance has been initialized.
	 */
	protected boolean _isInitialized() {
		return _isInitialized(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}
	
	/**
	 * This method is used to determine whether PropertyListManager has been
	 * successfully initialized.  It's primarily used within this class to
	 * verify a valid instance exists before executing code, but could be
	 * used outside of this class.
	 * 
	 * @param propertyListName - the name of the PropertyList instance
	 * @return Returns the status of initialization of PropertyListManager
	 */
	protected boolean _isInitialized(String propertyListName) {
		if (initializedFlag) {
			if (Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY)) {
				_reloadOnFileChange(propertyListName);
				
			} else if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL)) {
				int reloadInterval = Habitat4JFeatures.getFeatureInt(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL);
				if (reloadInterval>0) {
					reloadOnFileChange(propertyListName,reloadInterval);
				}
			}
		}
		
		return initializedFlag;
	}

	/**
	 * This method returns whether a property contains an instance of a PropertyBean Object.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an instance of a PropertyBean Object.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABean(String name) throws PropertyNotAvailableException {
		return _isPropertyABean(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);		
	}

	/**
	 * This method returns whether a property contains an instance of a PropertyBean Object.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an instance of a PropertyBean Object.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABean(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}	

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyABean(name);
	}

	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABeanArray(String name) throws PropertyNotAvailableException {
		return _isPropertyABeanArray(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);		
	}
	
	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABeanArray(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}			

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyABeanArray(name);
	}

	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABeanHash(String name) throws PropertyNotAvailableException {
		return _isPropertyABeanHash(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);		
	}
	
	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyABeanHash(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}			

		checkThatPropertyListExists(propertyListName,name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyABeanHash(name);
	}

	/**
	 * This method returns whether a property from the default PropertyList contains an array of Strings.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of Strings.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAnArray(String name) throws PropertyNotAvailableException {
		return _isPropertyAnArray(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);		
	}

	/**
	 * This method returns whether a property contains an array of String values.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether the property specified by name contains an array of String values.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAnArray(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}

		checkThatPropertyListExists(propertyListName,name);

		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyAnArray(name);
	}

	/**
	 * This method returns whether a property from the default PropertyList contains a hash of Strings.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains a hash of String values.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAHash(String name) throws PropertyNotAvailableException {
		return _isPropertyAHash(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);		
	}

	/**
	 * This method returns whether a property contains a hash of String values.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether the property specified by name contains a hash of String values.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAHash(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}

		checkThatPropertyListExists(propertyListName,name);

		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyAHash(name);
	}

	/**
	 * This method returns whether a property contains a String value.
	 * 
	 * @param name - the name of the property
	 * @return Returns whether the property specified by name contains a String value.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAString(String name) throws PropertyNotAvailableException {
		return _isPropertyAString(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name);
	}


	/**
	 * This method returns whether a property contains a String value.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether the property specified by name contains a String value.
	 * @throws PropertyNotAvailableException
	 */
	protected boolean _isPropertyAString(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!_isInitialized(propertyListName)) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,name);
			return false;
		}

		checkThatPropertyListExists(propertyListName,name);

		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		return propertyList.isPropertyAString(name);
	}
	
	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyList(String propertyListPath,PropertyListXMLMap map) throws PropertyListHandlerException {
		_loadPropertyList(null,propertyListPath,map);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyList(String propertyListName,String propertyListPath,PropertyListXMLMap map) throws PropertyListHandlerException {
		URL resourcePath = this.getClass().getClassLoader().getResource(propertyListPath);
		
		if (resourcePath == null) {
			_loadPropertyListFromFile(propertyListName,propertyListPath,false,map);
			
		} else {
			_loadPropertyListFromResource(propertyListName,propertyListPath,map);
		}
	}
	

	/**
	 * This method is used to retrieve a PropertyList from a file,
	 * using the specified PropertyList name.  This method can only be called once.
	 * Use the method with propertyListName as a parameter to load multiple PropertyList
	 * instances.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromFile(String propertyListName, String propertyListFilePath,PropertyListXMLMap map) throws PropertyListHandlerException {
		_loadPropertyListFromFile(propertyListName,propertyListFilePath,false,map);
	}

	/**
	 * This method is used to retrieve a PropertyList from a file,
	 * using the "default" PropertyList symbolic name.  This method can only be called once.
	 * Use the method with propertyListName as a parameter to load multiple PropertyList
	 * instances.
	 * 
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromFile(String propertyListFilePath,PropertyListXMLMap map) throws PropertyListHandlerException {
		_loadPropertyListFromFile(null,propertyListFilePath,false,map);
	}

	/**
	 * This method is called by your application to retrieve a PropertyList from a file
	 * using the symbolic name specified by the propertyListName argument.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @param tempPropertyList - marks this PropertyList as a temporary one (used for reloading)
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromFile(String propertyListName,String propertyListFilePath,boolean tempPropertyList,PropertyListXMLMap map) throws PropertyListHandlerException {
		loadPropertyList(propertyListName,propertyListFilePath,null,tempPropertyList,map);
	}

	/**
	 * This method is called by your application to retrieve a PropertyList from a classpath
	 * resource, using the "default" PropertyList symbolic name.  This method can only be
	 * called once.  Use the method with propertyListName as a parameter to load multiple
	 * PropertyList instances.
	 * 
	 * @param propertyListResourcePath - the classloader path of the PropertyList file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromResource(String propertyListResourcePath,PropertyListXMLMap map) throws PropertyListHandlerException {
		_loadPropertyListFromResource(null,propertyListResourcePath,map);
	}

	/**
	 * This method is used to retrieve a PropertyList from a classpath
	 * resource using the symbolic name specified by the propertyListName argument.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListResourcePath - the classloader path of the PropertyList file
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromResource(String propertyListName,String propertyListResourcePath,PropertyListXMLMap map) throws PropertyListHandlerException {
		loadPropertyList(propertyListName,null,propertyListResourcePath,false,map);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyList(String propertyListPath) throws PropertyListHandlerException {
		_loadPropertyList(null,propertyListPath);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyList(String propertyListName, String propertyListPath) throws PropertyListHandlerException {
		URL resourcePath = this.getClass().getClassLoader().getResource(propertyListPath);
		
		if (resourcePath == null) {
			_loadPropertyListFromFile(propertyListName,propertyListPath,false);
			
		} else {
			_loadPropertyListFromResource(propertyListName,propertyListPath);
		}
	}
	
	/**
	 * This method is used to retrieve a PropertyList from a file,
	 * using the "default" PropertyList symbolic name.  This method can only be called once.
	 * Use the method with propertyListName as a parameter to load multiple PropertyList
	 * instances.
	 * 
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromFile(String propertyListFilePath) throws PropertyListHandlerException {
		_loadPropertyListFromFile(null,propertyListFilePath,false,null);
	}

	/**
	 * This method is called by your application to retrieve a PropertyList from a file
	 * using the symbolic name specified by the propertyListName argument.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @param tempPropertyList - marks this PropertyList as a temporary one (used for reloading)
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromFile(String propertyListName, String propertyListFilePath,boolean tempPropertyList) throws PropertyListHandlerException {
		loadPropertyList(propertyListName,propertyListFilePath,null,tempPropertyList,null);
	}

	/**
	 * This method is called by your application to retrieve a PropertyList from a classpath
	 * resource, using the "default" PropertyList symbolic name.  This method can only be
	 * called once.  Use the method with propertyListName as a parameter to load multiple
	 * PropertyList instances.
	 * 
	 * @param propertyListResourcePath - the classloader path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromResource(String propertyListResourcePath) throws PropertyListHandlerException {
		_loadPropertyListFromResource(null,propertyListResourcePath,null);
	}

	/**
	 * This method is used to retrieve a PropertyList from a classpath
	 * resource using the symbolic name specified by the propertyListName argument.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListResourcePath - the classloader path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	protected void _loadPropertyListFromResource(String propertyListName, String propertyListResourcePath) throws PropertyListHandlerException {
		loadPropertyList(propertyListName,null,propertyListResourcePath,false,null);
	}

	/**
	 * This method reloads a file-based XML document based on file change.
	 */
	protected void _reloadOnFileChange() {
		_reloadOnFileChange(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT);
	}

	/**
	 * This method reloads a file-based XML document based on file change.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 */
	protected void _reloadOnFileChange(String propertyListName) {
		reloadOnFileChange(propertyListName, 0);
	}

	/**
	 * This method sets Habitat4J features.
	 * 
	 * @param name - the name of the feature
	 * @param value - the boolean value of the feature
	 * @throws PropertyListHandlerException
	 */
	protected void _setFeature(String name, boolean value) throws PropertyListHandlerException {
		if (value) {
			_setFeature(name,HABITAT4J_GENERIC_VALUE_TRUE);

		} else {
			_setFeature(name,HABITAT4J_GENERIC_VALUE_FALSE);
		}
	}
	
	/**
	 * This method sets Habitat4J features.
	 * 
	 * @param name - the name of the feature
	 * @param value - the int value of the feature
	 * @throws PropertyListHandlerException
	 */
	protected void _setFeature(String name, int value) throws PropertyListHandlerException {
		_setFeature(name,new Integer(value).toString());
	}

	/**
	 * This method sets Habitat4J features.
	 * 
	 * @param name - the name of the feature
	 * @param value - the value of the feature
	 * @throws PropertyListHandlerException
	 */
	protected void _setFeature(String name, String value) throws PropertyListHandlerException {
		if (name == null || name.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			throw new PropertyListHandlerException("Invalid feature");
		}
		
		if (Habitat4JFeatures.SERVER_IDENTITY_FEATURES.contains(name)) {
			if (serverIdentityManager != null) {
				try {
					serverIdentityManager._setFeature(name,value);
					
				} catch (ServerIdentityHandlerException sihe) {
					//
				}
			} else {
				throw new PropertyListHandlerException("ServerIdentityManager not initialized");				
			}
			
		} else if (Habitat4JFeatures.PROPERTY_LIST_FEATURES.contains(name)) {
			features.put(name,value);			
			
		} else {
			throw new PropertyListHandlerException("Invalid feature");			
		}
	}
	
	/**
	 * This method is used to produce a human-readable list of all PropertyList
	 * instances.
	 * 
	 * @return Returns a ready-for-display/logging dump of all PropertyList instances
	 */
	protected String _toDisplayString() {
		if (!_isInitialized()) {
			return null;
		}
		
		StringBuffer response = new StringBuffer();
		
		Enumeration lists = keys();
		while (lists.hasMoreElements()) {
			String listName = (String) lists.nextElement();
			response.append("### PROPERTY LIST: " + listName +" ###" + EOL_SEQUENCE);
			PropertyList propertyList = _getPropertyList(listName);
			response.append(propertyList.toString());
			if (lists.hasMoreElements()) {
				response.append(EOL_SEQUENCE);
			}
		}
		return response.toString();
	}

	/**
	 * @param reloadObject - the ReloadEventHandlerIF implementation
	 * @param propertyList - the previous propertyList object
	 * @param exception - the exception that was thrown
	 */
	private void doReloadOnFailure(ReloadEventHandlerIF reloadObject,PropertyList propertyList,PropertyListHandlerException exception) {
		if (reloadObject == null) return;
		
		reloadObject.onReloadFailure(propertyList,exception);
	}

	/**
	 * @param reloadObject - the ReloadEventHandlerIF implementation
	 * @param oldPropertyList - the previous propertyList object
	 * @param newPropertyList - the new propertyList object
	 * @return Returns whether the postReload method returned true.
	 */
	private boolean doReloadPostExecute(ReloadEventHandlerIF reloadObject,PropertyList oldPropertyList,PropertyList newPropertyList) {
		if (reloadObject == null) return true;
		
		return reloadObject.postReload(oldPropertyList,newPropertyList);
	}

	/**
	 * @param reloadObject - the ReloadEventHandlerIF implementation
	 * @param propertyList - the current propertyList object
	 * @return Returns whether the postExecute method returned true.
	 */
	private boolean doReloadPreExecute(ReloadEventHandlerIF reloadObject,PropertyList propertyList) {
		if (reloadObject == null) return true;
		
		return reloadObject.preReload(propertyList);
	}
	
	/**
	 * This method returns whether the reload serial is acceptable.
	 * 
	 * @param oldReloadSerial
	 * @param newReloadSerial
	 * @return Returns whether the reload serial is acceptable.
	 */
	private boolean doReloadSerialCheck(int oldReloadSerial,int newReloadSerial) {
		if (oldReloadSerial == -1 && newReloadSerial == -1) {
			return true;
			
		} else if (newReloadSerial == -1) {
			return false;
			
		} else if (newReloadSerial > oldReloadSerial) {
			return true;
			
		} else {
			return false;
		}
	}

	/**
	 * This private method provides access to the mechanisms that bring in a PropertyList,
	 * which include files and classpath resources.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the path of the PropertyList XML file
	 * @param propertyListResourcePath - the path of the PropertyList XML resource
	 * @param map - the PropertyListXMLMap to use for compatibility with other schemas
	 * @param tempPropertyList - designates this PropertyList as temporary
	 * @return Returns the instance of PropertyList loaded.
	 * 
	 * @throws PropertyListHandlerException
	 */
	private PropertyList loadPropertyList(String propertyListName, String propertyListFilePath, String propertyListResourcePath, boolean tempPropertyList, PropertyListXMLMap map) throws PropertyListHandlerException {
		String logMethodName = this.getClass().getName() + ".loadPropertyList(...) - ";
		
		if (serverIdentity == null) {
			try {
				setServerIdentity(serverIdentityManager._loadServerIdentity(serverIdentityMap));
				
			} catch (ServerIdentityHandlerException sihe) {
				throw new PropertyListHandlerException(sihe);
			}
		}

		if (!_isInitialized()) {
			throw new PropertyListHandlerException("PropertyListManager not initialized");
		}
		
		if (propertyListName != null && contains(propertyListName)) {
			throw new PropertyListHandlerException(logMethodName + "Attempt to re-initialize PropertyList \"" + propertyListName + "\" ignored",Habitat4JLogger.WARN);
		}
		
		PropertyList propertyList = new PropertyList(propertyListName,globalPropertyBeanDefinitions,globalDecoderDefinitions,globalDecryptorDefinitions,globalReloadEventHandler);
		
		if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_PROPERTY_BEAN_STRING_OBJECT_COPY)) {
			propertyList.setShouldCopyStringObjects(true);
		}
		if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_PROPERTY_BEAN_WRAPPER_OBJECT_COPY)) {
			propertyList.setShouldCopyWrapperObjects(true);
		}
		if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_PROPERTY_BEAN_CLONEABLE_OBJECT_COPY)) {
			propertyList.setShouldCopyCloneableObjects(true);
		}
		if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_SUPPRESS_PROPERTY_BEAN_COPY_WARNINGS)) {
			propertyList.setShouldSuppressPropertyBeanCopyWarnings(true);
		}
		if (Habitat4JFeatures.isFeatureSet(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_TO_STRING_ITEMS_PER_LINE)) {
			propertyList.setToStringItemsPerLine(Habitat4JFeatures.getFeatureInt(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_TO_STRING_ITEMS_PER_LINE));
		}
		
		boolean isFile = propertyListFilePath != null;
		boolean isResource = propertyListResourcePath != null;
		
		String propertyListPath = null;
		
		try {
			if (isFile) {
				propertyListPath = propertyListFilePath;
				logger.debug(logMethodName + "Loading PropertyList from file: " + propertyListPath);
				
				propertyHandler = new PropertyListHandler(serverIdentity,propertyList,propertyListFilePath,features,map);
			}
			
			if (isResource) {
				propertyListPath = propertyListResourcePath;
				logger.debug(logMethodName + "Loading PropertyList from resource: " + propertyListPath);			
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propertyListResourcePath);
				if (inputStream == null) {
					throw new PropertyListHandlerException(logMethodName + "Resource not found: " + propertyListResourcePath);
				}
				
				propertyHandler = new PropertyListHandler(serverIdentity,propertyList,inputStream,propertyListResourcePath,features,map);
			}

		} catch (PropertyListHandlerException plhe) {
			throw plhe;
			
		} catch (BaseHandlerException bhe) {
			throw new PropertyListHandlerException(bhe);
		}

		// Grab the propertyList name from the file (received via the Handler) and
		// if it's empty set it to null to be handled in the subsequent logic
		String propertyListNameInFile = null;
		
		if (!tempPropertyList) {
			propertyListNameInFile = propertyList.getListName();
			if (propertyListNameInFile != null && propertyListNameInFile.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) propertyListNameInFile = null;
		}
		
		// If there was no explicit PropertyList name specified, handle the following logic
		if (propertyListName == null) {
			
			// If the name provided in the list isn't null
			if (propertyListNameInFile != null) {
				// If we already have a list of this name, throw an exception
				if (contains(propertyListNameInFile)) {
					throw new PropertyListHandlerException(logMethodName + "Attempt to re-initialize PropertyList \"" + propertyListNameInFile + "\" ignored",Habitat4JLogger.WARN);
				}
				
				// If  this is a new list, use the name in the file
				propertyListName = propertyListNameInFile;
				
			// Use the "default" list if there wasn't one specified in the file
			} else {
				propertyListName = HABITAT4J_PROPERTY_LIST_NAME_DEFAULT;				}
			
		// Generate a logged WARNing which informs that the explicit name given in this method
		// will override the one specified in the file
		} else if (propertyListNameInFile != null && !propertyListName.equals(propertyListNameInFile)) {
			String fileOrResource = HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
			if (isFile) {
				fileOrResource = "file";
				
			} else if (isResource) {
				fileOrResource = "resource";
			}	
			logger.warn(logMethodName + "Explicitly given PropertyList name of \"" + propertyListName + "\" overrides name of \"" + propertyListNameInFile + "\" given in PropertyList " + fileOrResource);
		}
		
		// Set the name of the list to the PropertyList
		propertyList.setListName(propertyListName);
		
		// Add the PropertyList to the PropertyListManager
		put(propertyListName,propertyList);
		
		if (!tempPropertyList) {
			logger.info(logMethodName + "PropertyList \"" + propertyListName +"\" successfully loaded from path: " + propertyListPath);
		}
		
		return propertyList;
	}

	/**
	 * This method is used to reload XML documents.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param reloadIntervalMinutes - the time (in minutes) to reload
	 */
	private void reloadOnFileChange(String propertyListName, int reloadIntervalMinutes) {
		String logMethodName = this.getClass().getName() + ".reloadOnFileChange(...) - ";
		
		if (!Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD)) {
			return;
		}
		
		if (containsKey(propertyListName)) {
			if (!_isReloadablePropertyList(propertyListName)) {
				return;
			}
			
			PropertyList propertyList = (PropertyList) get(propertyListName);
			
			if (!propertyList.isReloadable()) {
				return;
			}
			
			PropertyListReloadInhibitor inhibitor = PropertyListReloadInhibitor.getInstance();
			if (inhibitor.isReloadInhibited()) {
				logger.debug(logMethodName + "PropertyList \"" + propertyListName + "\" reload is not permitted due to enabled ReloadInhibitor.");
				return;
			}
			
			long currentTime = 0;
			
			if (reloadIntervalMinutes > 0) {
				currentTime = new Date().getTime();
				if (currentTime-propertyList.getPropertyListFileLastLoaded() < (reloadIntervalMinutes*60*1000)) {
					logger.debug(logMethodName + "Reload interval not reached, no reload performed.");
					return;
				}
			}
			
			if (propertyList.shouldReload()) {
				if (propertyList.isReloadBlock()) {
					logger.debug(logMethodName + "PropertyList \"" + propertyListName + "\" reload currently in progress, aborting this attempt.");
					return;
				}

				synchronized (propertyHandler) {
					propertyList.setReloadBlock(true);
					
					if (reloadIntervalMinutes>0) {
						currentTime = new Date().getTime();
						if (currentTime-propertyList.getPropertyListFileLastLoaded() < (reloadIntervalMinutes*60*1000)) {
							propertyList.setReloadBlock(false);
							return;
						}
					}
					
					String tempPropertyListName = HABITAT4J_TEMP_PROPERTY_LIST_PREFIX + propertyListName;  
					ReloadEventHandlerIF reloadObject = propertyList.getReloadDefinitionInstance();
					
					try {						
						if (doReloadPreExecute(reloadObject,propertyList)) {
							_loadPropertyListFromFile(tempPropertyListName,propertyList.getPropertyListFilePath(),true);
							PropertyList tempPropertyList = (PropertyList) get(tempPropertyListName);
							
							if (!doReloadPostExecute(reloadObject,propertyList,tempPropertyList)) {
								tempPropertyList = null;
								logger.error(logMethodName + "PropertyList \"" + propertyListName + "\" post-execute failed during reload, reverted to previous PropertyList file.");
								
							} else if (!doReloadSerialCheck(propertyList.getListReloadSerial(),tempPropertyList.getListReloadSerial())) {
								tempPropertyList = null;
								logger.error(logMethodName + "PropertyList \"" + propertyListName + "\" doesn't have an acceptable serial number, reverted to previous PropertyList file.");
								
							} else {
								if (reloadObject != null && !Habitat4JFeatures.isFeatureTrue(features,Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_DISABLE_HANDLER_TRANSFER)) {
									if (!tempPropertyList.hasReloadEventHandler()) {
										tempPropertyList.setReloadEventHandler(reloadObject);
										logger.debug(logMethodName + "PropertyList \"" + propertyListName + "\" successfully transferred reload Object into newly loaded PropertyList.");
									}
								}
								
								tempPropertyList.setListName(propertyListName);
								put(propertyListName,tempPropertyList);
								logger.info(logMethodName + "PropertyList \"" + propertyListName + "\" reloaded successfully");
							}
						} else {
							logger.error(logMethodName + "PropertyList \"" + propertyListName + "\" pre-execute failed during reload, reverted to previous PropertyList file.");						
						}
					
					} catch (PropertyListHandlerException plhe) {
						logger.error(logMethodName + "Problem experienced re-reading PropertyList \"" + propertyListName + "\", reload ignored: " + plhe);
						doReloadOnFailure(reloadObject,propertyList,plhe);
	
					} finally {
						remove(tempPropertyListName);
						propertyList.setReloadBlock(false);
					}
				} // end sync block
			} else {
				if (reloadIntervalMinutes>0) {
					synchronized (propertyHandler) {
						propertyList.setPropertyListFileLastLoaded(currentTime);
					}
				}
				logger.debug(logMethodName + "No changes made to PropertyList file \"" + propertyListName + "\", reload not performed.");
			}
		}
	}
	/**
	 * @param serverIdentity the serverIdentity to set, used solely in the constructor of this class
	 */
	private void setServerIdentity(ServerIdentity serverIdentity) {
		this.serverIdentity = serverIdentity;
	}
	
	/**
	 * @param id
	 * @param decoderObject
	 */
	protected void _addGlobalDecoderDefinition(String id, Object decoderObject) {
		if (id == null || id.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || decoderObject == null) {
			return;
		}
		
		globalDecoderDefinitions.put(id,decoderObject);
	}

	/**
	 * @param id
	 * @param decoderClass
	 */
	protected void _addGlobalDecoderDefinition(String id, Class decoderClass) {
		if (decoderClass == null) {
			return;
		}
		
		_addGlobalDecoderDefinition(id,(Object) decoderClass);
	}
	
	/**
	 * @param id
	 * @param decryptorObject
	 */
	protected void _addGlobalDecryptorDefinition(String id, Object decryptorObject) {
		if (id == null || id.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || decryptorObject == null) {
			return;
		}
		
		globalDecryptorDefinitions.put(id,decryptorObject);
	}

	/**
	 * @param id
	 * @param decryptorClass
	 */
	protected void _addGlobalDecrytorDefinition(String id, Class decryptorClass) {
		if (decryptorClass == null) {
			return;
		}
		
		_addGlobalDecryptorDefinition(id,decryptorClass);
	}

	/**
	 * @param id
	 * @param propertyBeanClass
	 */
	protected void _addGlobalPropertyBeanDefinition(String id, Class propertyBeanClass) {
		if (id == null || id.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || propertyBeanClass == null) {
			return;
		}
		
		globalPropertyBeanDefinitions.put(id,propertyBeanClass);
	}
	/**
	 * @return Returns the globalReloadEventHandler.
	 */
	protected Object _getGlobalReloadEventHandler() {
		return globalReloadEventHandler;
	}
	
	/**
	 * @param globalReloadEventHandler The globalReloadEventHandler to set.
	 */
	protected void _setGlobalReloadEventHandler(Object globalReloadEventHandler) {
		this.globalReloadEventHandler = globalReloadEventHandler;
	}

	/**
	 * @param name - the pragma definition name
	 * @param value - the pragma definition value
	 * @throws PropertyNotAvailableException
	 */
	protected void _setPragmaDefinition(String name, String value) throws PropertyNotAvailableException {
		_setPragmaDefinition(HABITAT4J_PROPERTY_LIST_NAME_DEFAULT,name,value);
	}

	/**
	 * @param propertyListName - the PropertyList
	 * @param name - the pragma definition name
	 * @param value - the pragma definition value
	 * @throws PropertyNotAvailableException
	 */
	protected void _setPragmaDefinition(String propertyListName, String name, String value) throws PropertyNotAvailableException {
		String _name = name + " (pragma definition)";
		
		if (!_isInitialized()) {
			throwPropertyNotAvailableExceptionBecauseListNotInitialized(propertyListName,_name);
			return;
		}

		checkThatPropertyListExists(propertyListName,_name);
			
		PropertyList propertyList = (PropertyList) get(propertyListName);
		
		propertyList.setPragmaDefinition(name,value);
	}

	/**
	 * @param propertyListName
	 * @param name
	 * @throws PropertyNotAvailableException
	 */
	protected void checkThatPropertyListExists(String propertyListName, String name) throws PropertyNotAvailableException {
		if (!containsKey(propertyListName)) {
			throw new PropertyNotAvailableException("PropertyList \"" + propertyListName + "\" not available, so Property \"" + name + "\" is likewise not available");
		}
	}

	protected void checkThatPropertyListExists(String propertyListName) throws PropertyNotAvailableException {
		if (!containsKey(propertyListName)) {
			throw new PropertyNotAvailableException("PropertyList \"" + propertyListName + "\" not available");
		}
	}

	/**
	 * @param propertyListName
	 * @param name
	 * @throws PropertyNotAvailableException
	 */
	protected void throwPropertyNotAvailableExceptionBecauseListNotInitialized(String propertyListName, String name) throws PropertyNotAvailableException {
		throw new PropertyNotAvailableException("PropertyList \"" + propertyListName + "\" not initialized; it must be loaded first, so Property \"" + name + "\" is not available");
	}

	/**
	 * 
	 */
	protected void _inhibitReload() {
		PropertyListReloadInhibitor.getInstance().enable();		
	}

	/**
	 * @param expiration
	 */
	protected void _inhibitReload(long expiration) {
		PropertyListReloadInhibitor.getInstance().enable(expiration);		
	}

	/**
	 * 
	 */
	protected void _uninhibitReload() {
		PropertyListReloadInhibitor.getInstance().disable();		
	}

	protected void _clearReloadablePropertyLists() {
		if (reloadablePropertyLists != null) {
			reloadablePropertyLists.clear();
			
			reloadablePropertyLists = null;	
		}
	}

	protected void _removeReloadablePropertyList(String propertyListName) {
		if (reloadablePropertyLists != null) {
			reloadablePropertyLists.remove(propertyListName);
		}
	}

	protected void _addReloadablePropertyList(String propertyListName) {
		if (reloadablePropertyLists == null) {
			reloadablePropertyLists = new HashSet();
		}
		
		reloadablePropertyLists.add(propertyListName);
	}
	
	protected boolean _isReloadablePropertyList(String propertyListName) {
		boolean result = false;
		
		if (reloadablePropertyLists == null || reloadablePropertyLists.isEmpty()) {
			result = true;
			
		} else if (reloadablePropertyLists.contains(propertyListName)) {
			result = true;
		}
		
		return result;
	}
}
