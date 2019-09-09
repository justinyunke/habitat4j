package org.productivity.java.habitat4j;

import java.util.Enumeration;
import java.util.Properties;

import org.productivity.java.habitat4j.common.AbstractPropertyListManager;
import org.productivity.java.habitat4j.common.PropertyBeanHash;
import org.productivity.java.habitat4j.common.PropertyHash;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.ServerIdentity;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.exception.PropertyNotAvailableException;
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
 * @version $Id: PropertyListManager.java,v 1.52 2007/02/25 04:23:10 cvs Exp $
 */

public class PropertyListManager extends AbstractPropertyListManager {
	private static final long serialVersionUID = 3617294523483828535L;
	private static final String PropertyListManagerVersion = "$Id: PropertyListManager.java,v 1.52 2007/02/25 04:23:10 cvs Exp $";
	
	private static PropertyListManager instance = null;

	private PropertyListManager() {
		super();
	}

	/**
	 * This method returns an Enumeration of properties for the default PropertyList.
	 * 
	 * @return Returns an Enumeration of properties for the default PropertyList.
	 */
	public static Enumeration getProperties() {
		if (instance == null) return null;

		try {
			return instance._getProperties();
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns an Enumeration of properties for a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns an Enumeration of properties for a particular PropertyList.
	 */
	public static Enumeration getProperties(String propertyListName) {
		if (instance == null) return null;

		try {
			return instance._getProperties(propertyListName);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method is used to obtain a property's String value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the value of a property from the default PropertyList.
	 */
	public static String getProperty(String name) {
		if (instance == null) return null;

		try {
			return instance._getProperty(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method is used to obtain properties from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the value of a property from a particular PropertyList.
	 */
	public static String getProperty(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getProperty(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method is used to obtain a property's primitive int value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the primitive int value of a property from the default PropertyList.
	 */
	public static int getPropertyAsInt(String name) {
		if (instance == null) throw new RuntimeException("PropertyListManager not initialized; cannot convert to int value.");

		try {
			return instance._getPropertyAsInt(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return -1;
		}
	}
	
	/**
	 * This method is used to obtain a property's primitive int value from a particular PropertyList. 
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the primitive int value of a property from a particular PropertyList.
	 */
	public static int getPropertyAsInt(String propertyListName, String name) {
		if (instance == null) throw new RuntimeException("PropertyListManager not initialized; cannot convert to int value.");

		try {
			return instance._getPropertyAsInt(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return -1;
		}
	}
	
	/**
	 * This method is used to obtain a property's primitive boolean value from the default PropertyList. 
	 * 
	 * @param name - the name of the property
	 * @return Returns the primitive boolean value of a property from the default PropertyList.
	 */
	public static boolean isPropertyTrue(String name) {
		if (instance == null) throw new RuntimeException("PropertyListManager not initialized; cannot convert to boolean value.");

		try {
			return instance._isPropertyTrue(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}
	
	/**
	 * This method is used to obtain a property's primitive boolean value from a particular PropertyList. 
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the primitive boolean value of a property from a particular PropertyList.
	 */
	public static boolean isPropertyTrue(String propertyListName, String name) {
		if (instance == null) throw new RuntimeException("PropertyListManager not initialized; cannot convert to boolean value.");

		try {
			return instance._isPropertyTrue(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}
	
	/**
	 * This method is used to obtain a property's array of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from the default PropertyList.
	 */
	public static String[] getPropertyArray(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyArray(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's array of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of String values from a property from a particular PropertyList.
	 */
	public static String[] getPropertyArray(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyArray(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's hash of String values from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from the default PropertyList.
	 */
	public static PropertyHash getPropertyHash(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHash(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's hash of String values from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a hash of String values from a property from a particular PropertyList.
	 */
	public static PropertyHash getPropertyHash(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHash(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns a java.util.Properties object derived from the hash of String values in a PropertyHash from the default PropertyList.
	 * It is useful for storing 3rd-party properties in Habitat4J.
	 * 
	 * @param name - the name of the property
	 * @return Returns a java.util.Properties object derived from the hash of String values in a PropertyHash from the default PropertyList.
	 */
	public static Properties getPropertyHashAsJavaProperties(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHashAsJavaProperties(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns a java.util.Properties object derived from the hash of String values in a PropertyHash from the default PropertyList.
	 * It is useful for storing 3rd-party properties in Habitat4J.
	 * 
	 * @param name - the name of the property
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns a java.util.Properties object derived from the hash of String values in a PropertyHash from the default PropertyList.
	 */
	public static Properties getPropertyHashAsJavaProperties(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHashAsJavaProperties(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a String value from a PropertyHash in a particular PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from a PropertyHash.
	 */
	public static String getPropertyHashValue(String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHashValue(name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a String value from a PropertyHash in a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from a PropertyHash in a specific propertyListName.
	 */
	public static String getPropertyHashValue(String propertyListName, String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyHashValue(propertyListName, name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to indicate whether a PropertyHash value is set in the
	 * default PropertyList.  When used with null-valued entries in the PropertyList
	 * it can provide a simple HashSet implementation.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a boolean value indicating a PropertyHash value is set.
	 */
	public static boolean isPropertyHashValueSet(String name, String key) {
		if (instance == null) return false;

		try {
			return instance._isPropertyHashValueSet(name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}

	/**
	 * This method is used to indicate whether a PropertyHash value is set for a 
	 * particular PropertyList.  When used with null-valued entries in the PropertyList
	 * it can provide a simple HashSet implementation.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns where a PropertyHash value is present.
	 */
	public static boolean isPropertyHashValueSet(String propertyListName, String name, String key) {
		if (instance == null) return false;

		try {
			return instance._isPropertyHashValueSet(propertyListName, name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}

	/**
	 * This method is used to obtain a PropertyBean Object from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a PropertyBean Object from the default PropertyList.
	 */
	public static Object getPropertyBean(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBean(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a PropertyBean Object from a
	 * particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a PropertyBean Object from a particular PropertyList.
	 */
	public static Object getPropertyBean(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBean(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method is used to obtain a PropertyBean Object from the default PropertyList.
	 * It returns a copy (clone) of the Property instance that can be modified by the application without
	 * altering the original loaded and stored property.
	 * 
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of a PropertyBean Object from a property from the default PropertyList.
	 */
	public static Object getPropertyBeanCopy(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanCopy(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a PropertyBean Object from a particular PropertyList.
	 * It returns a copy (clone) of the Property instance that can be modified by the application without
	 * altering the original loaded and stored property.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of a PropertyBean Object from a property from a particular PropertyList.
	 */
	public static Object getPropertyBeanCopy(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanCopy(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method is used to obtain a property's array of PropertyBean Objects from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns an array of PropertyBean Objects from a property from the default PropertyList.
	 */
	public static Object[] getPropertyBeanArray(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanArray(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's array of PropertyBean Objects from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns an array of PropertyBean Objects from a property from a particular PropertyList.
	 */
	public static Object[] getPropertyBeanArray(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanArray(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's hash of PropertyBean Objects from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a hash of PropertyBean Objects from a property from the default PropertyList.
	 */
	public static PropertyBeanHash getPropertyBeanHash(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHash(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a property's hash of PropertyBean Objects from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a hash of PropertyBean Objects from a property from a particular PropertyList.
	 */
	public static PropertyBeanHash getPropertyBeanHash(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHash(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a specific hash value from a PropertyHash in the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from a PropertyHash in the default PropertyList.
	 */
	public static Object getPropertyBeanHashValue(String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashValue(name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a specific hash value from a PropertyHash in a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a String value from a PropertyHash in a particular PropertyList.
	 */
	public static Object getPropertyBeanHashValue(String propertyListName, String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashValue(propertyListName, name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a PropertyBean Object from a property hash in the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a copy (clone) of a PropertyBean Object from a PropertyHash in the default PropertyList.
	 */
	public static Object getPropertyBeanHashValueCopy(String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashValueCopy(name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a PropertyBean Object from a property hash in a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @param key - the key to lookup in the PropertyHash
	 * @return Returns a copy (clone) of a PropertyBean Object from a PropertyHash in a particular PropertyList.
	 */
	public static Object getPropertyBeanHashValueCopy(String propertyListName, String name, String key) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashValueCopy(propertyListName, name, key);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a property's array of PropertyBean Objects from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of an array of PropertyBean Objects from a property from the default PropertyList.
	 */
	public static Object[] getPropertyBeanArrayCopy(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanArrayCopy(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a property's array of PropertyBean Objects from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of an array of PropertyBean Objects in a copied (cloned) property hash from a particular PropertyList.
	 */
	public static Object[] getPropertyBeanArrayCopy(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanArrayCopy(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a property's hash of PropertyBean Objects from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of PropertyBean Objects in a copied (cloned) property hash from the default PropertyList.
	 */
	public static PropertyBeanHash getPropertyBeanHashCopy(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashCopy(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method is used to obtain a copy (clone) of a property's hash of PropertyBean Objects from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns a copy (clone) of PropertyBean Objects in a copied (cloned) property hash from a particular PropertyList.
	 */
	public static PropertyBeanHash getPropertyBeanHashCopy(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyBeanHashCopy(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method gets a property's class from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns the class of the property from the default PropertyList.
	 */
	public static Class getPropertyClass(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyClass(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}
	
	/**
	 * This method gets a property's class from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the class of the property from a particular PropertyList.
	 */
	public static Class getPropertyClass(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyClass(propertyListName,name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method gets a property's class name from the default PropertyList.
	 * 
	 * @param name - the name of the property
	 * @return Returns the class name of the property from the default PropertyList.
	 */
	public static String getPropertyClassName(String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyClassName(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method gets a property's class name from a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns the class name of the property from a particular PropertyList.
	 */
	public static String getPropertyClassName(String propertyListName, String name) {
		if (instance == null) return null;

		try {
			return instance._getPropertyClassName(propertyListName,name);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns the default PropertyList instance.
	 * 
	 * @return Returns the default PropertyList object.
	 */
	public static PropertyList getPropertyList() {
		if (instance == null) return null;
		
		return instance._getPropertyList();
	}

	/**
	 * This method returns a particular PropertyList instance.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns a PropertyList object for a particular PropertyList.
	 */
	public static PropertyList getPropertyList(String propertyListName) {
		if (instance == null) return null;

		return instance._getPropertyList(propertyListName);
	}
	
	
	/**
	 * This method renames a particular PropertyList instance.
	 * 
	 * @param oldPropertyListName - the name of the PropertyList to rename
	 * @param newPropertyListName - the new name of the PropertyList
	 * @throws PropertyListHandlerException
	 */
	public static void renamePropertyList(String oldPropertyListName, String newPropertyListName) throws PropertyListHandlerException {
		if (instance == null) return;
		
		instance._renamePropertyList(oldPropertyListName,newPropertyListName);
	}
	
	/**
	 * This method removes a PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList to remove
	 * @throws PropertyListHandlerException
	 */
	public static void removePropertyList(String propertyListName) throws PropertyListHandlerException {
		if (instance == null) return;
		
		instance._removePropertyList(propertyListName);
	}	
	
	/**
	 * This method returns the singleton instance of PropertyListManager.
	 * 
	 * @return Returns the singleton instance of PropertyListManager.
	 * @throws PropertyListHandlerException
	 */
	public static PropertyListManager getPropertyListManager() throws PropertyListHandlerException {
		if (!isInitialized()) {
			throw new PropertyListHandlerException();
		}

		return instance;
	}

	/**
	 * This method returns the "modified by" of the default PropertyList.
	 * 
	 * @return Returns the "modified by" of the default PropertyList.
	 */
	public static String getPropertyListModifiedBy() {
		if (instance == null) return null;

		try {
			return instance._getPropertyListModifiedBy();
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns the "modified by" of the PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the "modified by" of the PropertyList.
	 */
	public static String getPropertyListModifiedBy(String propertyListName) {
		if (instance == null) return null;

		try {
			return instance._getPropertyListModifiedBy(propertyListName);
		
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns the reload serial enumeration of the default PropertyList.
	 * 
	 * @return Returns the reload serial enumeration of the default PropertyList.
	 */
	public static int getPropertyListReloadSerial() {
		if (instance == null) return -1;

		try {
			return instance._getPropertyListReloadSerial();
		
		} catch (PropertyNotAvailableException pnae) {
			return -1;
		}
	}

	/**
	 * This method returns the reload serial enumeration of a particular PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the reload serial enumeration of a particular PropertyList.
	 */
	public static int getPropertyListReloadSerial(String propertyListName) {
		if (instance == null) return -1;

		try {
			return instance._getPropertyListReloadSerial(propertyListName);
		
		} catch (PropertyNotAvailableException pnae) {
			return -1;
		}
	}

	/**
	 * This method returns the version of the default PropertyList.
	 * 
	 * @return Returns the version of the default PropertyList.
	 */
	public static String getPropertyListVersion() {
		if (instance == null) return null;

		try {
			return instance._getPropertyListVersion();
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}

	}

	/**
	 * This method returns the version of the PropertyList.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns the version of the PropertyList.
	 */
	public static String getPropertyListVersion(String propertyListName) {
		if (instance == null) return null;

		try {
			return instance._getPropertyListVersion(propertyListName);
			
		} catch (PropertyNotAvailableException pnae) {
			return null;
		}
	}

	/**
	 * This method returns the one and only ServerIdentity instance.
	 * 
	 * @return Returns the ServerIdentity object used by this PropertyListManager instance.
	 */
	public static ServerIdentity getServerIdentity() {
		if (instance == null) return null;
		
		return instance._getServerIdentity();
	}

	/**
	 * This method initializes the PropertyListManager singleton object.
	 * 
	 * @param applicationName - the name of the application
	 * @param mode - the mode (SERVER_IDENTITY_MODE_FILE, SERVER_IDENTITY_MODE_JVM, or SERVER_IDENTITY_MODE_NULL)
	 * @param map - ServerIdentityXMLMap definition to use
	 * @return Returns the singleton instance of the PropertyListManager object.
	 */
	public static PropertyListManager initialize(String applicationName, String mode, ServerIdentityXMLMap map) {
		if (instance == null) {
			instance = new PropertyListManager();
			ServerIdentityManager serverIdentityManager = ServerIdentityManager.initialize(applicationName,mode);
			instance._initialize(applicationName,serverIdentityManager,mode,map);
			instance.logger.info("Habitat4J Stock PropertyListManager Version " + PropertyListManagerVersion + " initialized");
		}
		
		return instance;
	}

	/**
	 * @param applicationName - the name of the application
	 * @param mode - the mode (SERVER_IDENTITY_MODE_FILE, SERVER_IDENTITY_MODE_JVM, or SERVER_IDENTITY_MODE_NULL)
	 * @return Returns the singleton instance of the PropertyListManager object.
	 */
	public static PropertyListManager initialize(String applicationName, String mode) {
		return initialize(applicationName,mode,null);
	}

	/**
	 * This method initializes the PropertyListManager singleton object.
	 * 
	 * @param applicationName - the name of the application
	 * @return Returns the singleton instance of the PropertyListManager object.
	 */
	public static PropertyListManager initialize(String applicationName) {
		initialize(applicationName,SERVER_IDENTITY_MODE_FILE,null);
		
		return instance;
	}

	/**
	 * This method returns whether this singleton instance of PropertyListManager is initialized for the default PropertyList.
	 * 
	 * @return Returns whether this singleton instance of PropertyListManager is initialized for the default PropertyList.
	 */
	public static boolean isInitialized() {
		if (instance == null) {
			return false;		
		}
		
		return instance._isInitialized();
	}

	/**
	 * This method returns whether this singleton instance of PropertyListManager is initialized for a particular Propertylist.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @return Returns whether this singleton instance of PropertyListManager is initialized for a particular Propertylist.
	 */
	public static boolean isInitialized(String propertyListName) {
		if (instance == null) return false;
		
		return instance._isInitialized(propertyListName);
	}

	/**
	 * This method returns whether a property contains an instance of a PropertyBean Object.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an instance of a PropertyBean Object.
	 */
	public static boolean isPropertyABean(String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyABean(name);		
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}

	/**
	 * This method returns whether a property contains an instance of a PropertyBean Object.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an instance of a PropertyBean Object.
	 */
	public static boolean isPropertyABean(String propertyListName, String name) {
		if (instance == null) return false;
		
		try {
			return instance._isPropertyABean(propertyListName,name);		
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}
	}

	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 */
	public static boolean isPropertyABeanArray(String name) {
		if (instance == null) return false;
		
		try {
			return instance._isPropertyABeanArray(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}

	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 */
	public static boolean isPropertyABeanArray(String propertyListName, String name) {
		if (instance == null) return false;
		
		try {
			return instance._isPropertyABeanArray(propertyListName,name);		
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}
	
	/**
	 * This method returns whether a property contains a hash of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains a hash of PropertyBean Objects.
	 */
	public static boolean isPropertyABeanHash(String name) {
		if (instance == null) return false;
		
		try {
			return instance._isPropertyABeanHash(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}

	/**
	 * This method returns whether a property contains an hash of PropertyBean Objects.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains a hash of PropertyBean Objects.
	 */
	public static boolean isPropertyABeanHash(String propertyListName, String name) {
		if (instance == null) return false;
		
		try {
			return instance._isPropertyABeanHash(propertyListName,name);		
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}
	
	/**
	 * This method returns whether a property from the default PropertyList contains an array of String values.
	 * 
	 * @param name - the name of the property
	 * @return Returns whether a property from the default PropertyList contains an array of String values.
	 */
	public static boolean isPropertyAnArray(String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAnArray(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}

	/**
	 * This method returns whether a property from a particular PropertyList contains an array of String values.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether a property from a particular PropertyList contains an array of String values.
	 */
	public static boolean isPropertyAnArray(String propertyListName, String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAnArray(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}
	
	/**
	 * This method returns whether a property from the default PropertyList contains a hash of String values.
	 * 
	 * @param name - the name of the property
	 * @return Returns whether a property from the default PropertyList contains a hash of String values.
	 */
	public static boolean isPropertyAHash(String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAHash(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}

	/**
	 * This method returns whether a property from a particular PropertyList contains a hash of String values.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether a property from a particular PropertyList contains a hash of String values.
	 */
	public static boolean isPropertyAHash(String propertyListName, String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAHash(propertyListName, name);
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}
	
	/**
	 * This method returns whether a property contains a String value.
	 * 
	 * @param name - the name of the property
	 * @return Returns whether a property from the default PropertyList contains a String value.
	 */
	public static boolean isPropertyAString(String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAString(name);
		
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}

	/**
	 * This method returns whether a property contains a String value.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param name - the name of the property
	 * @return Returns whether a property from a particular PropertyList contains a String value.
	 */
	public static boolean isPropertyAString(String propertyListName, String name) {
		if (instance == null) return false;

		try {
			return instance._isPropertyAString(propertyListName, name);
			
		} catch (PropertyNotAvailableException pnae) {
			return false;
		}	
	}
	
	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyList(String propertyListPath, PropertyListXMLMap map) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyList(null,propertyListPath,map);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyList(String propertyListName, String propertyListPath, PropertyListXMLMap map) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyList(propertyListName,propertyListPath,map);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML file using
	 * a PropertyListXMLMap.
	 * 
	 * @param propertyListFilePath - the file path for this instance of PropertyList
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromFile(String propertyListFilePath, PropertyListXMLMap map) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromFile(propertyListFilePath,map);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML file using
	 * a PropertyListXMLMap.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the file path for this instance of PropertyList
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromFile(String propertyListName, String propertyListFilePath, PropertyListXMLMap map)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyListFromFile(propertyListName, propertyListFilePath, map);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML classpath resource using
	 * a PropertyListXML map.
	 * 
	 * @param propertyListResourcePath
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromResource(String propertyListResourcePath, PropertyListXMLMap map)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromResource(propertyListResourcePath,map);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML classpath resource using
	 * a PropertyListXMLMap.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListResourcePath
	 * @param map - PropertyListXMLMap definition to use
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromResource(String propertyListName, String propertyListResourcePath, PropertyListXMLMap map)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromResource(propertyListName,propertyListResourcePath,map);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyList(String propertyListPath) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyList(null,propertyListPath);
	}

	/**
	 * This wrapper method tries to find a given propertyListPath as a classpath resource, then
	 * as a file resource, and then loads it.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListPath - the classloader or file path of the PropertyList file
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyList(String propertyListName, String propertyListPath) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyList(propertyListName,propertyListPath);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML file.
	 * 
	 * @param propertyListFilePath - the file path for this instance of PropertyList
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromFile(String propertyListFilePath) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromFile(propertyListFilePath);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML file.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListFilePath - the file path for this instance of PropertyList
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromFile(String propertyListName, String propertyListFilePath)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._loadPropertyListFromFile(propertyListName, propertyListFilePath, false);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML classpath resource.
	 * 
	 * @param propertyListResourcePath
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromResource(String propertyListResourcePath)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromResource(propertyListResourcePath);
	}

	/**
	 * This method loads a PropertyList from the contents of an XML classpath resource.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 * @param propertyListResourcePath
	 * @throws PropertyListHandlerException
	 */
	public static void loadPropertyListFromResource(String propertyListName, String propertyListResourcePath)
			throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");
		
		instance._loadPropertyListFromResource(propertyListName,propertyListResourcePath);
	}

	/**
	 * This method reloads a file-based XML document based on file change.
	 */
	public static void reloadOnFileChange() {
		if (instance == null) return;

		instance._reloadOnFileChange();
	}
	
	/**
	 * This method reloads a file-based XML document based on file change.
	 * 
	 * @param propertyListName - the name of the PropertyList
	 */
	public static void reloadOnFileChange(String propertyListName) {
		if (instance == null) return;

		instance._reloadOnFileChange(propertyListName);
	}
	
	/**
	 * This method resets this singleton instance.  Not recommended for use except
	 * for JUnit testing of Habitat4J.
	 */
	public static void reset() {
		if (instance == null) return;
		
		ServerIdentityManager.reset();
		instance = null;
	}
	
	/**
	 * This method sets a Habitat4J feature.
	 * 
	 * @param name - the name of the feature
	 * @param value - the value of the feature
	 * @throws PropertyListHandlerException
	 */
	public static void setFeature(String name, boolean value) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._setFeature(name,value);
	}

	/**
	 * This method sets a Habitat4J feature.
	 * 
	 * @param name - the name of the feature
	 * @param value - the value of the feature
	 * @throws PropertyListHandlerException
	 */
	public static void setFeature(String name, int value) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._setFeature(name,value);
	}

	/**
	 * This method sets a Habitat4J feature.
	 * 
	 * @param name - the name of the feature
	 * @param value - the value of the feature
	 * @throws PropertyListHandlerException
	 */
	public static void setFeature(String name, String value) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		instance._setFeature(name,value);
	}
	
	/**
	 * @param propertyListName
	 * @param name
	 * @param value
	 * @throws PropertyListHandlerException
	 */
	public static void setPragmaDefinition(String propertyListName, String name, String value) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		try {
			instance._setPragmaDefinition(propertyListName,name,value);
			
		} catch (PropertyNotAvailableException pnae) {
			return;
		}
	}

	/**
	 * @param name
	 * @param value
	 * @throws PropertyListHandlerException
	 */
	public static void setPragmaDefinition(String name, String value) throws PropertyListHandlerException {
		if (instance == null) throw new PropertyListHandlerException("PropertyListManager not initialized.");

		try {
			instance._setPragmaDefinition(name,value);
			
		} catch (PropertyNotAvailableException pnae) {
			return;
		}
	}

	/**
	 * @param id - the id for the DecryptorIF implementation
	 * @param decoderObject - the instance for the DecoderIF implementation
	 */
	public static void addGlobalDecoderDefinition(String id, Object decoderObject) {
		if (instance == null) return;
		
		instance._addGlobalDecoderDefinition(id,decoderObject);
	}

	/**
	 * @param id - the id for the DecoderIF implementation
	 * @param decoderClass - the class for the DecoderIF implementation
	 */
	public static void addGlobalDecoderDefinition(String id, Class decoderClass) {
		if (instance == null) return;
		
		instance._addGlobalDecoderDefinition(id,decoderClass);
	}
	
	/**
	 * @param id - the id for the DecryptorIF implementation
	 * @param decryptorObject - the instance for the DecryptorIF implementation
	 */
	public static void addGlobalDecryptorDefinition(String id, Object decryptorObject) {
		if (instance == null) return;
		
		instance._addGlobalDecryptorDefinition(id,decryptorObject);
	}

	/**
	 * @param id - the id for the DecryptorIF implementation
	 * @param decryptorClass - the class for the DecryptorIF implementation
	 */
	public static void addGlobalDecrytorDefinition(String id, Class decryptorClass) {
		if (instance == null) return;

		instance._addGlobalDecryptorDefinition(id,decryptorClass);
	}

	/**
	 * @param id - the id for the PropertyBean Object
	 * @param propertyBeanClass - the class for the PropertyBean Object
	 */
	public static void addGlobalPropertyBeanDefinition(String id, Class propertyBeanClass) {
		if (instance == null) return;

		instance._addGlobalPropertyBeanDefinition(id,propertyBeanClass);
	}

	/**
	 * This method forces Habitat4J to inhibit all reload actions.
	 */
	public static void inhibitReload() {
		if (instance == null) return;

		instance._inhibitReload();		
	}

	/**
	 * This method forces Habitat4J to inhibit all reload actions.
	 * 
	 * @param expiration - specifies the duration of the reload inhibition.
	 */
	public static void inhibitReload(long expiration) {
		if (instance == null) return;
		
		instance._inhibitReload(expiration);		
	}

	/**
	 * This method releases Habitat4J's reload inhibition.
	 */
	public static void uninhibitReload() {
		if (instance == null) return;

		instance._uninhibitReload();		
	}
	
	public static void clearReloadablePropertyLists() {
		if (instance == null) return;
		
		instance._clearReloadablePropertyLists();
	}

	public static void removeReloadablePropertyList(String propertyListName) {
		if (instance == null) return;
		
		instance._removeReloadablePropertyList(propertyListName);
	}

	public static void addReloadablePropertyList(String propertyListName) {
		if (instance == null) return;
		
		instance._addReloadablePropertyList(propertyListName);
	}

	public static boolean isReloadablePropertyList(String propertyListName) {
		if (instance == null) return false;
		
		return instance._isReloadablePropertyList(propertyListName);
	}
	
	/**
	 * This method is used for debugging purposes.
	 * 
	 * @return Returns a human-readable display string containing all PropertyList instances and their properties managed by this PropertyListManager.
	 */
	public static String toDisplayString() {
		if (instance == null) return null;
		
		return instance._toDisplayString();
	}
}
