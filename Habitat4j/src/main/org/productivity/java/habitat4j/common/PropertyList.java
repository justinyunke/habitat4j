package org.productivity.java.habitat4j.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.productivity.java.habitat4j.common.exception.DecoderException;
import org.productivity.java.habitat4j.common.exception.DecryptorException;
import org.productivity.java.habitat4j.common.exception.PropertyNotAvailableException;
import org.productivity.java.habitat4j.common.interfaces.DecoderIF;
import org.productivity.java.habitat4j.common.interfaces.DecryptorIF;
import org.productivity.java.habitat4j.common.interfaces.ReloadEventHandlerIF;

/**
 * This class provides name/value pair Hashtables for property lists
 * maintained by Habitat4J.  Access directly to a PropertyList is
 * not typical - the getters and setters in PropertyListManager
 * are used instead.
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
 * @version $Id: PropertyList.java,v 1.60 2007/02/25 18:52:04 cvs Exp $
 */
public class PropertyList extends Hashtable implements Habitat4JConstants {
	private static final long serialVersionUID = 4123103965777901619L;

	public static final Object[] emptyObjectArray = new Object[] {};
	public static final Class[] emptyClassArray = new Class[] {};
	public static final Class[] singleStringClassArray = new Class[] { String.class };
	public static final Class[] singleCharClassArray = new Class[] { char.class };
	
	private String listName = null;

	private String listVersion = null;
	private String listModifiedBy = null;
	private int listReloadSerial = -1;
	
	private String propertyListFilePath = null;
	private long propertyListFileLastModified = -1;
	private long propertyListFileLastLoaded = -1;
	
	private Hashtable globalPropertyBeanDefinitions = null;
	private Hashtable globalDecoderDefinitions = null;
	private Hashtable globalDecryptorDefinitions = null;
	private Object globalReloadEventHandler = null;

	private Hashtable propertyBeanDefinitions = null;
	private Hashtable propertyBeanCopyMethodDefinitions = null;
	private Hashtable decoderDefinitions = null;
	private Hashtable decryptorDefinitions = null;
	
	private Object reloadEventHandler = null;
	
	private Hashtable pragmaDefinitions = null;

	private HashSet decodedProperties = null;
	private HashSet decryptedProperties = null;
	
	private boolean reloadBlock = false;
	
	private boolean shouldCopyStringObjects = false;
	private boolean shouldCopyWrapperObjects = false;
	private boolean shouldCopyCloneableObjects = false;
	private boolean shouldSuppressPropertyBeanCopyWarnings = false;
	
	private int toStringItemsPerLine = HABITAT4J_PROPERTY_LIST_TO_STRING_ITEMS_PER_LINE_DEFAULT;
	
	protected Habitat4JLogger logger = null;
	
	public PropertyList(String listName, Hashtable globalPropertyBeanDefinitions, Hashtable globalDecoderDefinitions, Hashtable globalDecryptorDefinitions, Object globalReloadEventHandler) {
		super();
		this.listName = listName;
		this.globalDecoderDefinitions = globalDecoderDefinitions;
		this.globalDecryptorDefinitions = globalDecryptorDefinitions;
		this.globalPropertyBeanDefinitions = globalPropertyBeanDefinitions;
		this.globalReloadEventHandler = globalReloadEventHandler;
		this.logger = Habitat4JLogger.getInstance();
	}
	
	/**
	 * This method sets a property name/value pair.
	 * 
	 * @param name - the name of the property to get
	 * @param value - the value of the property to set
	 */
	public void setProperty(String name, String value) {
		put(name,value);
	}

	/**
	 * This method gets a property consisting of a String value.
	 * 
	 * @param name - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public String getProperty(String name) throws PropertyNotAvailableException {		
		checkThatPropertyExists(name);
		
		if (isPropertyAString(name)) {
			return (String) get(name);
		}

		throwPropertyNotAvailableExceptionForBadType(name,"String");
		return null;
	}

	/**
	 * @param name - the name of the property to get
	 * @return Returns a primitive int value.
	 * @throws PropertyNotAvailableException
	 */
	public int getPropertyAsInt(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);

		if (isPropertyAString(name)) {
			String _value = (String) get(name);
				
			if (_value == null) {
				throwPropertyNotAvailableException(name,"cannot convert to int value (value is null)");
			}
				
			String value = _value.trim();

			if (value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				throwPropertyNotAvailableException(name,"cannot convert to int value (value is an empty String)");
			}
			
			try {				
				return new Integer(value).intValue();
				
			} catch (NumberFormatException nfe) {
				throwPropertyNotAvailableException(name,nfe.toString());
				return -1;
			}
		}
		throwPropertyNotAvailableExceptionForBadType(name,"String; cannot convert to int value.");
		return -1;
	}

	/**
	 * @param name - the name of the property to get
	 * @return Returns a primitive boolean value if the the value of the property matches trimmed/case-insensitive "true," "on," or "1".
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyTrue(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);

		if (isPropertyAString(name)) {
			String _value = (String) get(name);
			
			if (_value == null) {
				throwPropertyNotAvailableException(name,"cannot convert to boolean value (value is null).");
			}
			
			String value = _value.trim();
			
			if (value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				throwPropertyNotAvailableException(name,"cannot convert to boolean value (value is an empty String)");				
			}
			
			if ((value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_TRUE) || value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_ON) || value.equals(HABITAT4J_GENERIC_VALUE_ONE) || value.equals(HABITAT4J_GENERIC_VALUE_YES))) {
				return true;
				
			} else if ((value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_FALSE) || value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_OFF) || value.equals(HABITAT4J_GENERIC_VALUE_ZERO) || value.equals(HABITAT4J_GENERIC_VALUE_NO))) {
				return false;
					
			} else {
				throwPropertyNotAvailableException(name,"cannot convert to boolean value (is not recognizable by boolean text, such as true, false, on, off, ..)");
				return false;
			}
		}

		throwPropertyNotAvailableExceptionForBadType(name,"String; cannot convert to int value.");
		return false;
	}

	/**
	 * This method gets a property consisting of an array of String values.
	 * 
	 * @param name - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public String[] getPropertyArray(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyAnArray(name)) {
			PropertyArrayVector vector = (PropertyArrayVector) get(name);
			
			String[] propStringArray = new String[vector.size()];
			vector.copyInto(propStringArray);				

			return propStringArray;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"String array");
		return null;
	}

	/**
	 * This method gets a property consisting of a hash of String values.
	 * 
	 * @param name - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public PropertyHash getPropertyHash(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyAHash(name)) {
			PropertyHash propHashTable = (PropertyHash) get(name);

			return propHashTable;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"String Hash");
		return null;
	}

	/**
	 * This method gets a keyed property from a hash of String values.
	 * 
	 * @param name - the name of the property to get
	 * @param key - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public String getPropertyHashValue(String name, String key) throws PropertyNotAvailableException {
		PropertyHash propHashTable = getPropertyHash(name);
		
		if (propHashTable == null) {
			throwPropertyNotAvailableException(name,"No PropertyHash found");
		}
		
		if (key == null) {
			throw new PropertyNotAvailableException("Hash key value cannot be null");
		}
		
		return (String) propHashTable.get(key);
	}

	/**
	 * This method gets a keyed property from a hash of String values.
	 * 
	 * @param name - the name of the property to get
	 * @param key - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyHashValue(String name, String key) throws PropertyNotAvailableException {
		PropertyHash propHashTable = getPropertyHash(name);
		
		if (propHashTable == null) {
			throwPropertyNotAvailableException(name,"No PropertyHash found");
		}
		
		if (key == null) {
			throw new PropertyNotAvailableException("Hash key value cannot be null");
		}
		
		return propHashTable.containsKey(key);
	}

	/**
	 * @param table - the PropertyHashTable to convert
	 * @return Returns a HashSet version of the PropertyHash
	 */
	protected HashSet convertPropertyHashtableToHashSet(PropertyHash table) {
		Enumeration enumeration = table.elements();
		HashSet set = new HashSet();
		
		while (enumeration.hasMoreElements()) {
			set.add(enumeration.nextElement());
		}
		
		return set;
	}
	
	/**
	 * This method gets a property consisting of a hash of String values.
	 * 
	 * @param name - the name of the property to get
	 * @return Returns the value of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public HashSet getPropertyHashSet(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyAHash(name)) {
			PropertyHash propHashTable = (PropertyHash) get(name);

			return convertPropertyHashtableToHashSet(propHashTable);
		}

		throwPropertyNotAvailableExceptionForBadType(name,"String Hash");
		return null;
	}

	/**
	 * @param vector - the PropertyBeanArrayVector to copy (clone) 
	 * @return Returns a new instance of PropertyBeanArrayVector which includes copies (clones) of all PropertyBean Objects within the provided vector
	 */
	protected PropertyBeanArrayVector getPropertyBeanArrayCopy(PropertyBeanArrayVector vector) {
		String methodName = "getPropertyBeanArrayCopy(PropertyBeanArrayVector): ";
		
		PropertyBeanArrayVector newVector = new PropertyBeanArrayVector();
		
		for (int i=0; i<vector.size(); i++) {
			Object bean = vector.get(i);

			try {
				Object newBean = getPropertyBeanCopy(bean);
				newVector.add(newBean);
				
			} catch (PropertyNotAvailableException pnae) {
				logger.warn(methodName + "Could not copy Object: " + pnae + ", skipping");
			}			
		}
		
		return newVector;
	}

	/**
	 * @param hash - the PropertyBeanHash to copy (clone) 
	 * @return Returns a new instance of PropertyBeanHash which includes copies (clones) of all PropertyBean Objects within the provided hash
	 */
	protected PropertyBeanHash getPropertyBeanHashCopy(PropertyBeanHash hash) {
		String methodName = "getPropertyBeanHashCopy(PropertyBeanHash): ";
		
		PropertyBeanHash newHash = new PropertyBeanHash();

		Enumeration enumeration = hash.keys();
		
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			
			Object bean = hash.get(key);

			try {
				Object newBean = getPropertyBeanCopy(bean);
				newHash.put(key,newBean);
				
			} catch (PropertyNotAvailableException pnae) {
				logger.warn(methodName + "Could not copy Object: " + pnae + ", skipping");
			}			
		}
		
		return newHash;
	}

	/**
	 * @param clazz - the Class to check
	 * @return Returns whether the given Class is supported for copying
	 */
	private boolean isWrapperClassSupportedForCopying(Class clazz) {
		return 
			(clazz == Short.class) ||
			(clazz == Integer.class) ||
			(clazz == Long.class) ||
			
			(clazz == Double.class) ||
			(clazz == Float.class) ||
			
			(clazz == Boolean.class) ||
			
			(clazz == Character.class) ||

			(clazz == BigInteger.class) ||
			(clazz == BigDecimal.class);
	}
	/**
	 * @param object - the Object to clone
	 * @param methodName - the methodName to use in logging
	 * @return Returns a clone of an Object, if it's either a supported Habitat4J type, or an implementation of Cloneable with a working "clone()" method.
	 */
	protected Object cloneObject(Object object, String methodName) {
		if (object == null) {
			return object;
		}

		Class objectType = object.getClass();
		String objectTypeName = objectType.getName();
		
		if (shouldCopyStringObjects && objectType == String.class) {
			String s = (String) object;
			
			Object clonedObject = new String(s);
			
			return clonedObject;
			
		} else if (shouldCopyWrapperObjects && isWrapperClassSupportedForCopying(objectType)) {
			try {
				Object clonedObject = null;
				
				// Character has no toString() method, so this must use the primitive "char" for the clone
				if (objectType == Character.class) {
					Character c = (Character) object;

					clonedObject = new Character(c.charValue());

				// All other supported types have a toString() method, which will be used to facilitate the clone
				} else {
					Method toStringMethod = object.getClass().getMethod("toString",emptyClassArray);
					String value = (String) toStringMethod.invoke(object,emptyObjectArray);
					Constructor constructor = objectType.getConstructor(singleStringClassArray);
					clonedObject = constructor.newInstance(new Object[] { value });
				}
				
				return clonedObject;
				
			} catch (NoSuchMethodException nsme) {
				logger.error(methodName + "object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + nsme);
				return object;
				
			} catch (InvocationTargetException ite) {
				logger.error(methodName + "object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + ite);
				return object;
				
			} catch (IllegalAccessException iae) {
				logger.error(methodName + "object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + iae);
				return object;
				
			} catch (Exception e) {
				logger.error(methodName + "object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + e);
				return object;				
			}
		}
		
		if (shouldCopyCloneableObjects && object instanceof Cloneable) {
			Object clonedObject = null;
			
			try {
				Method cloneMethod = objectType.getMethod("clone",emptyClassArray);
				
				// Attempt to invoke the clone() method.
				clonedObject = cloneMethod.invoke(object,emptyObjectArray);
				
				return clonedObject;
				
			} catch (NoSuchMethodException nsme) {
				logger.error(methodName + "clone of Cloneable object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value: " + nsme);
				return object;
				
			} catch (InvocationTargetException ite) {
				logger.error(methodName + "clone of Cloneable object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + ite);
				return object;
				
			} catch (IllegalAccessException iae) {
				logger.error(methodName + "clone of Cloneable object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + iae);
				return object;
				
			} catch (Exception e) {
				logger.error(methodName + "clone of Cloneable object of type \"" + objectTypeName + "\" failed cloning, so using original (uncloned) value " + e);
				return object;				
			}
		}
		
		return object;
	}
	
	/**
	 * @param bean - the Object instance to copy (clone)
	 * @return Returns a copy (clone) of a Object.
	 * @throws PropertyNotAvailableException
	 */
	protected Object getPropertyBeanCopyViaReflection(Object bean) throws PropertyNotAvailableException {
		String methodName = "getPropertyBeanCopyViaReflection(Object): ";

		Class beanClass = bean.getClass();
		
		methodName += " Object class \"" + beanClass.getName() + "\" ";
		
		Method[] beanMethods = beanClass.getMethods();
		Object newBean = null;
		
		try {
			newBean = beanClass.newInstance();
			
		} catch (InstantiationException ia) {
			throw new PropertyNotAvailableException(methodName + ia);
			
		} catch (IllegalAccessException iae) {
			throw new PropertyNotAvailableException(methodName + iae);			
		}
		
		for (int i=0; i< beanMethods.length; i++) {
			Method beanMethodGetter = beanMethods[i];
			String beanMethodGetterName = beanMethodGetter.getName();
			
			if (!beanMethodGetterName.equals("getClass")) { 			
				if (beanMethodGetterName.toLowerCase().startsWith("get") || beanMethodGetterName.toLowerCase().startsWith("is")) {
					Class[] beanMethodGetterParameterTypes = beanMethodGetter.getParameterTypes();
					
					if (beanMethodGetterParameterTypes.length == 0) {
						Class[] beanMethodGetterReturnTypes = new Class[] { beanMethodGetter.getReturnType() };
	
						int startPos = (beanMethodGetterName.substring(0,1).equalsIgnoreCase("i")) ? 2 : 3; 
						
						String fieldName = beanMethodGetterName.substring(startPos,beanMethodGetterName.length());
						
						String beanMethodSetterName = "set" + fieldName;
						Object getterValue = null;

						Method hasMethod = null;
						boolean hasValue = true;
						
						try {
							hasMethod = beanClass.getMethod("has" + fieldName, emptyClassArray);
							
							if (hasMethod != null && (hasMethod.getReturnType() == boolean.class || hasMethod.getReturnType() == Boolean.class)) {
								Boolean _hasValue = (Boolean) hasMethod.invoke(bean,emptyObjectArray);
								
								if (_hasValue != null) {
									hasValue = _hasValue.booleanValue();
								}
							}							
						
						} catch (NoSuchMethodException nsme) {
							//
							
						} catch (InvocationTargetException ite) {
							//

						} catch (IllegalAccessException iae) {
							//
						}
												
						if (hasValue) {
							try {
								Method beanMethodSetter = beanClass.getMethod(beanMethodSetterName,beanMethodGetterReturnTypes);
								Class[] beanMethodSetterParameterTypes = beanMethodSetter.getParameterTypes();
								
								if (beanMethodSetterParameterTypes.length == 1) {
									getterValue = beanMethodGetter.invoke(bean,emptyObjectArray);
									
									// Attempt to clone the Object
									getterValue = cloneObject(getterValue,methodName);
									
									beanMethodSetter.invoke(newBean,new Object[] { getterValue });
									
								} else {
									logger.debug(methodName + "setter \"" + beanMethodSetterName + "\" requires more than one parameter, skipping (this field may not be copied)");
								}
								
							} catch (NoSuchMethodException nsme) {
								logger.log(
									isShouldSuppressPropertyBeanCopyWarnings() ? Habitat4JLogger.DEBUG : Habitat4JLogger.WARN, 
									methodName + "setter \"" + beanMethodSetterName + "(" + beanMethodGetterReturnTypes[0].getName() + ")\" does not exist, skipping (this field will not be copied)"
								);
		
							} catch (InvocationTargetException ite) {
								logger.log(
									isShouldSuppressPropertyBeanCopyWarnings() ? Habitat4JLogger.DEBUG : Habitat4JLogger.WARN, 
									methodName + "getter \"" + beanMethodGetterName + "\" exception: " + ite
								);
								
							} catch (IllegalAccessException iae) {
								logger.log(
									isShouldSuppressPropertyBeanCopyWarnings() ? Habitat4JLogger.DEBUG : Habitat4JLogger.WARN, 
									methodName + "getter \"" + beanMethodGetterName + "\" or setter \"" + beanMethodSetterName + "\" exception: " + iae
								);							
							}
						}
						
					} else {
						logger.debug(methodName + "getter \"" + beanMethodGetterName + "\" requires " + ((beanMethodGetterParameterTypes.length==1) ? "a parameter" : "parameters") + ", skipping");
					}
				}
			}
		}
		
		return newBean;
	}
	
	/**
	 * getPropertyBeanCopyViaSerialization(Object) uses serialization to copy (clone) an Object.
	 * 
	 * @param bean - the Object instance to copy (clone)
	 * @return Returns a copy (clone) of a Object.
	 * @throws PropertyNotAvailableException
	 */
	protected Object getPropertyBeanCopyViaSerialization(Object bean) throws PropertyNotAvailableException {
		String methodName = "getPropertyBeanCopyViaSerialization(Object): ";
		
		Object newBean = null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(bean);
			
			oos.close();
			
		} catch (Exception e) {
			throw new PropertyNotAvailableException(methodName + e);
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
		
			newBean = ois.readObject();
			
			ois.close();
			
		} catch (Exception e) {
			throw new PropertyNotAvailableException(methodName + e);
		}
		
		return newBean;
	}

	/**
	 * @param bean - the Object instance to copy (clone)
	 * @return Returns a copy (clone) of a Object.
	 * @throws PropertyNotAvailableException
	 */
	protected Object getPropertyBeanCopy(Object bean) throws PropertyNotAvailableException {
		String methodName = "getPropertyBeanCopy(Object): ";

		if (bean == null) {
			throw new PropertyNotAvailableException(methodName + "cannot copy null Object");
		}
		
		Object newBean = null;
		
		String copyMethod = null;
		
		if (propertyBeanCopyMethodDefinitions != null && propertyBeanCopyMethodDefinitions.containsKey(bean.getClass())) {
			copyMethod = (String) propertyBeanCopyMethodDefinitions.get(bean.getClass());
		}
		
		if (copyMethod != null && copyMethod.equals(HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_SERIALIZE)) {
			newBean = getPropertyBeanCopyViaSerialization(bean);
			
		} else {
			newBean = getPropertyBeanCopyViaReflection(bean);
		}
		
		return newBean;
	}
	
	/**
	 * This method gets a property consisting of a PropertyBean.
	 * 
	 * @param name - the name of the property to get
	 * @param returnCopy - whether or not to return a copy of the PropertyBean
	 * @return Returns an instance of a PropertyBean based on the Object interface.
	 * @throws PropertyNotAvailableException
	 */
	public Object getPropertyBean(String name, boolean returnCopy) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyABean(name)) {
			Object bean = get(name);
			
			if (returnCopy) {
				bean = getPropertyBeanCopy(bean);
			}
			
			return bean;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"PropertyBean");
		return null;
	}

	/**
	 * This method gets a property consisting of an array of PropertyBean Objects.
	 * 
	 * @param name - the name of the property
	 * @param returnCopy - whether or not to return a copy of the PropertyBean array
	 * @return Returns an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	public Object[] getPropertyBeanArray(String name, boolean returnCopy) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyABeanArray(name)) {
			PropertyBeanArrayVector vector = (PropertyBeanArrayVector) get(name);
			
			if (returnCopy) {
				vector = getPropertyBeanArrayCopy(vector);
			}

			if (vector.size() < 1) {
				throw new PropertyNotAvailableException("PropertyBeanArray \"" + name + "\" is empty");
			}
			
			Object firstPropBean = vector.get(0);
			Class clazz = firstPropBean.getClass();
			
			Object[] propBeanArray = (Object[]) Array.newInstance(clazz,vector.size());			
			vector.copyInto(propBeanArray);
			
			return propBeanArray;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"PropertyBean array");
		return null;
	}

	/**
	 * This method gets a property consisting of an array of PropertyBean Objects.
	 * 
	 * @param name - the name of the property
	 * @param returnCopy - whether or not to return a copy of the PropertyBean array
	 * @return Returns an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	public PropertyBeanHash getPropertyBeanHash(String name, boolean returnCopy) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyABeanHash(name)) {
			PropertyBeanHash propBeanHash = (PropertyBeanHash) get(name);
			
			if (returnCopy) {
				propBeanHash = getPropertyBeanHashCopy(propBeanHash);
			}

			if (propBeanHash.size() < 1) {
				throw new PropertyNotAvailableException("PropertyBeanHash \"" + name + "\" is empty");
			}
			
			return propBeanHash;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"PropertyBeanHash");
		return null;
	}

	/**
	 * This method gets a property consisting of an array of PropertyBean Objects.
	 * 
	 * @param name - the name of the property
	 * @param value - the key to lookup in the hashtable
	 * @param returnCopy - whether or not to return a copy of the PropertyBean array
	 * @return Returns an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	public Object getPropertyBeanHashValue(String name, String value, boolean returnCopy) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		if (isPropertyABeanHash(name)) {
			PropertyBeanHash propBeanHash = (PropertyBeanHash) get(name);
			
			if (propBeanHash.size() < 1) {
				throw new PropertyNotAvailableException("PropertyBeanHash \"" + name + "\" is empty");
			}
			
			Object bean = propBeanHash.get(value);

			if (bean == null) {
				throw new PropertyNotAvailableException("PropertyBeanHash \"" + name + "\" does not contain key \"" + value + "\"");
			}
		
			if (returnCopy) {
				bean = getPropertyBeanCopy(bean);
			}

			return bean;
		}

		throwPropertyNotAvailableExceptionForBadType(name,"PropertyBeanHash");
		return null;
	}

	/**
	 * This method gets a property's class.
	 * 
	 * @param name the name of the property for which to get the class
	 * @return Returns the class of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public Class getPropertyClass(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		Object value = get(name);
		
		return value.getClass();
	}

	/**
	 * This method gets a property's classname.
	 * 
	 * @param name the name of the property for which to get the classname
	 * @return Returns the classname of the property based on the provided name.
	 * @throws PropertyNotAvailableException
	 */
	public String getPropertyClassName(String name) throws PropertyNotAvailableException {
		Class clazz = getPropertyClass(name);
		
		return clazz.getName();
	}

	/**
	 * This method returns whether a property contains a String (name/value pair).
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains a String.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyAString(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		Object value = get(name);
		
		return value instanceof String;
	}

	/**
	 * This method returns whether a property contains an array of Strings.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of Strings.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyAnArray(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		Object value = get(name);
		
		return value instanceof PropertyArrayVector;
	}

	/**
	 * This method returns whether a property contains a hash of Strings.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of Strings.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyAHash(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		Object value = get(name);
		
		return value instanceof PropertyHash;
	}

	/**
	 * This method is used to determine whether a property contains an implementation of
	 * the Object interface.
	 * 
	 * @param name - the name of the property
	 * @return Returns whether a property contains a PropertyBean instance.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyABean(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);
		
		Object value = get(name);
		
		if (propertyBeanDefinitions != null) {
			if (propertyBeanDefinitions.containsValue(value.getClass())) {
				return true;
			}
		}
		
		if (globalPropertyBeanDefinitions.containsValue(value.getClass())) {
			return true;
		}
		
		return false;
	}

	/**
	 * This method returns whether a property contains an array of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains an array of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyABeanArray(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);

		Object value = get(name);
		
		return value instanceof PropertyBeanArrayVector;
	}

	/**
	 * This method returns whether a property contains a hash of PropertyBean Objects.
	 * 
	 * @param name the name of the property
	 * @return Returns whether the property specified by name contains a hash of PropertyBean Objects.
	 * @throws PropertyNotAvailableException
	 */
	public boolean isPropertyABeanHash(String name) throws PropertyNotAvailableException {
		checkThatPropertyExists(name);

		Object value = get(name);
		
		return value instanceof PropertyBeanHash;
	}

	/**
	 * This method returns an Enumeration of all property names.
	 * 
	 * @return Returns an Enumeration of all property names.
	 */
	public Enumeration getProperties() {
		return keys();
	}
	
	/**
	 * @return Returns the file path for this PropertyList object.
	 */
	public String getPropertyListFilePath() {
		return propertyListFilePath;
	}

	/**
	 * @param propertyListFilePath
	 */
	public void setPropertyListFilePath(String propertyListFilePath) {
		this.propertyListFilePath = propertyListFilePath;
	}

	/**
	 * @return Returns the propertyListFileLastModified.
	 */
	public long getPropertyListFileLastModified() {
		return propertyListFileLastModified;
	}
	
	/**
	 * @param propertyListFileLastModified The propertyListFileLastModifiedDate to set.
	 */
	public void setPropertyListFileLastModified(long propertyListFileLastModified) {
		this.propertyListFileLastModified = propertyListFileLastModified;
	}
		
	/**
	 * @return Returns whether this PropertyList is able to be reloaded.
	 */
	public boolean isReloadable() {
		return propertyListFilePath != null; 
	}
	
	/**
	 * @return Returns whether this PropertyList should be reloaded, based on the last modified time/date stamp of the file.
	 */
	public boolean shouldReload() {
		if (!isReloadable()) {
			return false;
		}
		
		if (propertyListFileLastModified == -1) {
			return true;
		}
		
		return (propertyListFileLastModified != getCurrentLastModified(propertyListFilePath));
	}

	/**
	 * @param fileName
	 * @return Returns the last modified file date/time stamp from the operating system.
	 */
	public static long getCurrentLastModified(String fileName) {
		try {
			File file = new File(fileName);
			return file.lastModified();
			
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * @return Returns the propertyListFileLastLoaded.
	 */
	public long getPropertyListFileLastLoaded() {
		return propertyListFileLastLoaded;
	}
	
	/**
	 * @param propertyListFileLastLoaded The propertyListFileLastLoaded to set.
	 */
	public void setPropertyListFileLastLoaded(long propertyListFileLastLoaded) {
		this.propertyListFileLastLoaded = propertyListFileLastLoaded;
	}
	
	/**
	 * This method handles the addition of a PropertyBean definition.
	 * 
	 * @param id - the identifier of the property bean definition
	 * @param object - the Class or Object of the property bean definition
	 */
	public void addPropertyBeanDefinition(String id, Object object) {
		if (propertyBeanDefinitions == null) {
			propertyBeanDefinitions = new Hashtable();
		}
		
		if (!propertyBeanDefinitions.containsKey(id)) {
			propertyBeanDefinitions.put(id,object);
		}
	}

	/**
	 * This method handles the addition of a PropertyBean copyMethod definition.
	 * 
	 * @param clazz - the class of the property bean definition
	 * @param byte - the Class or Object of the property bean definition
	 */
	public void addPropertyBeanCopyMethodDefinition(Class clazz, String cloneMethodType) {
		if (propertyBeanCopyMethodDefinitions == null) {
			propertyBeanCopyMethodDefinitions = new Hashtable();
		}
		
		if (!propertyBeanCopyMethodDefinitions.containsKey(clazz)) {
			propertyBeanCopyMethodDefinitions.put(clazz,cloneMethodType);
		}
	}

	/**
	 * This method handles the addition of a Decoder definition.
	 * 
	 * @param id - the identifier of the decoding definition
	 * @param object - the Class or Object of the decoding definition
	 */
	public void addDecoderDefinition(String id, Object object) {
		if (decoderDefinitions == null) {
			decoderDefinitions = new Hashtable();
		}

		if (!decoderDefinitions.containsKey(id)) {
			decoderDefinitions.put(id,object);
		}
	}
	
	/**
	 * This method handles the addition of a Decryptor definition.
	 * 
	 * @param id - the identifier of the decryptor definition
	 * @param object - the Class or Object of the decryptor definition
	 */
	public void addDecryptionDefinition(String id, Object object) {
		if (decryptorDefinitions == null) {
			decryptorDefinitions = new Hashtable();
		}

		if (!decryptorDefinitions.containsKey(id)) {
			decryptorDefinitions.put(id,object);
		}
	}
	
	/**
	 * This method handles the setting of a PropertyList-local pragma definition.
	 * If null or "" is passed in via the value parameter, the pragma definition
	 * will be removed.
	 * 
	 * @param name - the name of the pragma definition
	 * @param value - the value of the pragma definition
	 */
	public void setPragmaDefinition(String name, String value) {
		if (pragmaDefinitions == null) {
			pragmaDefinitions = new Hashtable();
		}

		if (value == null || value.equals("")) {
			pragmaDefinitions.remove(name);
			
		} else {
			pragmaDefinitions.put(name,value);
		}
	}
	
	/**
	 * This method handles the retrieval of a PropertyList-local pragma definition.
	 * 
	 * @param name - the name of the pragma definition
	 * @return Returns the pragma definition value.
	 */
	public String getPragmaDefinition(String name) {
		String value = null;
		
		if (pragmaDefinitions != null && pragmaDefinitions.containsKey(name)) {
			value = (String) pragmaDefinitions.get(name);
		}
		
		return value;
	}
	
	/**
	 * @return Returns all PropertyList-specific pragma definitions.
	 */
	public Hashtable getPragmaDefinitions() {
		if (pragmaDefinitions == null) {
			pragmaDefinitions = new Hashtable();
		}
		
		return pragmaDefinitions;
	}
	
	/**
	 * This method is used to determine whether a PropertyBean identifier is valid.
	 * 
	 * @param id - the identifier of the property bean definition
	 * @return Returns whether a PropertyBean identifier is valid.
	 */
	public boolean isPropertyBeanIdValid(String id) {
		if (globalPropertyBeanDefinitions.containsKey(id)) {
			return true;
			
		} else if (propertyBeanDefinitions == null) {
			return false;
			
		} else {
			return propertyBeanDefinitions.containsKey(id);
		}
	}

	/**
	 * This method is used to determine whether a Decoder identifier is valid.
	 * 
	 * @param id - the identifier of the decoding definition
	 * @return Returns whether a decoding identifier is valid.
	 */
	public boolean isDecoderIdValid(String id) {
		if (globalDecoderDefinitions.containsKey(id)) {
			return true;
			
		} else if (decoderDefinitions == null) {
			return false;
			
		} else {
			return decoderDefinitions.containsKey(id);
		}
	}

	/**
	 * This method is used to determine whether a Decryptor identifier is valid.
	 * 
	 * @param id - the identifier of the decryptor definition
	 * @return Returns whether a decryptor identifier is valid.
	 */
	public boolean isDecryptorIdValid(String id) {
		if (globalDecryptorDefinitions.containsKey(id)) {
			return true;
			
		} else if (decryptorDefinitions == null) {
			return false;
			
		} else {
			return decryptorDefinitions.containsKey(id);
		}
	}

	/**
	 * @param id - the PropertyBean identifier
	 * @return Returns a new instance of a PropertyBean Object.
	 */
	public Object getNewPropertyBeanInstance(String id) {
		if (!isPropertyBeanIdValid(id)) {
			return null;
		}
		
		try {
			Class propertyBeanClass = null;
			
			if (propertyBeanDefinitions != null && propertyBeanDefinitions.containsKey(id)) {
				propertyBeanClass = (Class) propertyBeanDefinitions.get(id);
				
			} else {
				propertyBeanClass = (Class) globalPropertyBeanDefinitions.get(id);				
			}
			
			return propertyBeanClass.newInstance();
			
		} catch (InstantiationException ie) {
			return null;
			
		} catch (IllegalAccessException iae) {
			return null;
		}
	}

	/**
	 * @param appName - the name of the calling application
	 * @param id - the Decoder identifier
	 * @return Returns an instance of a DecoderIF implementation.
	 * @throws DecoderException
	 */
	public DecoderIF getDecoderInstance(String appName, String id) throws DecoderException {
		if (!isDecoderIdValid(id)) {
			return null;
		}
		
		try {
			Object decoderObject = null;
			
			if (decoderDefinitions != null && decoderDefinitions.containsKey(id)) {
				decoderObject = decoderDefinitions.get(id);
				
			} else {
				decoderObject = globalDecoderDefinitions.get(id);
			}
			
			DecoderIF decoder = null;
			
			if (decoderObject instanceof Class) {
				// First time around, there will be a Class in the decoderDefinitions
				Class decoderClass = (Class) decoderObject;
				decoderObject = decoderClass.newInstance();

				decoder = (DecoderIF) decoderObject;
				decoder.initialize(appName,getListName());
				
			} else {
				decoder = (DecoderIF) decoderObject;
			}

			return decoder;
			
		} catch (DecoderException de) {
			throw de;
			
		} catch (InstantiationException ie) {
			throw new DecoderException(ie.toString());
			
		} catch (IllegalAccessException iae) {
			throw new DecoderException(iae.toString());
		}
	}

	/**
	 * @param appName - the name of the calling application
	 * @param id - the Decryptor identifier
	 * @return Returns an instance of a DecryptorIF implementation.
	 * @throws DecryptorException
	 */
	public DecryptorIF getDecryptorInstance(String appName, String id) throws DecryptorException {
		if (!isDecryptorIdValid(id)) {
			return null;
		}
		
		try {
			Object decryptorObject = null;
			
			if (decryptorDefinitions != null && decryptorDefinitions.containsKey(id)) {
				decryptorObject = decryptorDefinitions.get(id);
				
			} else {
				decryptorObject = globalDecryptorDefinitions.get(id);
			}
			
			DecryptorIF decryptor = null;
			
			if (decryptorObject instanceof Class) {
				// First time around, there will be a Class in the decryptorDefinitions
				Class decryptorClass = (Class) decryptorObject;
				decryptorObject = decryptorClass.newInstance();
				decryptor = (DecryptorIF) decryptorObject;
				decryptor.initialize(appName,getListName());
				
			} else {
				decryptor = (DecryptorIF) decryptorObject;
			}

			return decryptor;
			
		} catch (DecryptorException de) {
			throw de;
			
		} catch (InstantiationException ie) {
			throw new DecryptorException(ie.toString());
			
		} catch (IllegalAccessException iae) {
			throw new DecryptorException(iae.toString());
		}
	}
	
	/**
	 * This method sets the flag that indicates a property was decoded at load time.
	 * 
	 * @param name - the property name
	 */
	public void flagDecodedProperty(String name) {
		if (decodedProperties == null) {
			decodedProperties = new HashSet();
		}
		
		decodedProperties.add(name);
	}

	/**
	 * This method sets the flag that indicates a property was decrypted at load time.
	 * 
	 * @param name - the property name
	 */
	public void flagDecryptedProperty(String name) {
		if (decryptedProperties == null) {
			decryptedProperties = new HashSet();
		}
		
		decryptedProperties.add(name);
	}
	
	/**
	 * @param name - the property name
	 * @return Returns whether a property was decoded at load time.
	 */
	public boolean isDecodedProperty(String name) {
		if (decodedProperties == null) return false;
		
		return decodedProperties.contains(name);
	}

	/**
	 * @param name
	 * @return Returns whether a property was decrypted at load time.
	 */
	public boolean isDecryptedProperty(String name) {
		if (decryptedProperties == null) return false;

		return decryptedProperties.contains(name);
	}
	
	/**
	 * @param name - the name of the property
	 * @param delimitedName - the name of the property which may contain a delimiter (for arrays and PropertyBean/PropertyBean arrays)
	 * @param value - the value of the property
	 * @param hashKey - if present, will prepend before ENCODED/ENCRYPTED field
	 * @return Returns a String containing a displayable version of the property value provided.
	 */
	private String getDisplayablePropertyValue(String name, String delimitedName, String value, String hashKey) {
		boolean decoded = false;
		boolean decrypted = false;
		
		if (delimitedName == null) {
			delimitedName = name;
		}
		
		try {
			checkThatPropertyExists(name);
			
		} catch (PropertyNotAvailableException pnae) {
			return "null";
		}

		decoded = isDecodedProperty(delimitedName);
		
		decrypted = isDecryptedProperty(delimitedName);
		
		String _value = (hashKey != null) ? (hashKey + "=") : HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
		
		if (decoded && !decrypted) {
			_value += HABITAT4J_HIDDEN_FIELD_DECODED;
			
		} else if (!decoded && decrypted) {
			_value += HABITAT4J_HIDDEN_FIELD_DECRYPTED;
			
		} else if (decoded && decrypted) {
			_value += HABITAT4J_HIDDEN_FIELD_DECODED_DECRYPTED;
			
		} else {
			_value += value;
		}
		
		return _value;
	}

	/**
	 * @param name - the name of the property
	 * @param delimitedName - the name of the property which may contain a delimiter (for arrays and PropertyBean/PropertyBean arrays)
	 * @param value - the value of the property
	 * @return Returns a String containing a displayable version of the property value provided.
	 */
	private String getDisplayablePropertyValue(String name, String delimitedName, String value) {
		return getDisplayablePropertyValue(name,delimitedName,value,null);
	}

	/**
	 * @param name - the name of the property
	 * @param value - the value of the property
	 * @return Returns a String containing a displayable version of the property value provided.
	 */
	private String getDisplayablePropertyValue(String name, String value) {
		return getDisplayablePropertyValue(name,null,value);
	}
	
	/**
	 * @param name - the name of the property
	 * @param arrayIndex - the index of the property
	 * @param value - the value of the property
	 * @return Returns a String containing a displayable version of the property value provided.
	 */
	private String getDisplayablePropertyValue(String name,int arrayIndex,String value) {
		return getDisplayablePropertyValue(name,name + HABITAT4J_PROPERTY_TYPE_DELIMITER + arrayIndex,value);
	}
	
	/**
	 * @param set - an instance of the Set class to sort
	 * @return Returns a sorted Iterator from the provided Set instance.
	 */
	private Iterator getSortedIteratorFromSet(Set set) {
		ArrayList sortedKeys = new ArrayList(set);
		Collections.sort(sortedKeys);
		
		return sortedKeys.iterator();		
	}
	
	/**
	 * This method provides a human-readable dump of all properties, but will NOT display
	 * any values that were decoded or decrypted at load time.
	 * 
	 * @return Returns a human-readable dump of all properties in the form of a String.
	 */
	public String toString() {	
		StringBuffer response = new StringBuffer();

		Iterator sortedKeysIterator = getSortedIteratorFromSet(keySet());
		
		while (sortedKeysIterator.hasNext()) {
			String name = (String) sortedKeysIterator.next();
			String value = null;

			try {
				if (isPropertyAString(name)) {
					value = getDisplayablePropertyValue(name,getProperty(name));
					
				} else if (isPropertyAnArray(name)) {
					String[] valueArray = getPropertyArray(name);
					
					StringBuffer valueStringBuffer = new StringBuffer("{");
					
					if (valueArray.length > 0) valueStringBuffer.append("\n  ");
					
					for (int i=0; i<valueArray.length; i++) {
						valueStringBuffer.append(getDisplayablePropertyValue(name,i,valueArray[i]));
						
						if (i!=valueArray.length-1) valueStringBuffer.append(",");
						
						if (((i+1) % toStringItemsPerLine == 0) || (i==valueArray.length-1)) valueStringBuffer.append("\n  ");
					}
					
					valueStringBuffer.append("}");
					
					value = valueStringBuffer.toString();
					
				} else if (isPropertyAHash(name)) {
					PropertyHash propHashTable = getPropertyHash(name);

					StringBuffer valueStringBuffer = new StringBuffer("{");
					
					int n = propHashTable.keySet().size();
					
					if (n > 0) valueStringBuffer.append("\n  ");
					
					Iterator propHashIterator = getSortedIteratorFromSet(propHashTable.keySet());					

					int i = -1;

					while (propHashIterator.hasNext()) {
						i++;
						
						String key = (String) propHashIterator.next();
						String _value = (String) propHashTable.get(key);
						
						valueStringBuffer.append(getDisplayablePropertyValue(name,name + HABITAT4J_PROPERTY_TYPE_DELIMITER + key,_value,key));

						if (propHashIterator.hasNext()) valueStringBuffer.append(",");

						if (((i+1) % toStringItemsPerLine == 0) || (!propHashIterator.hasNext())) valueStringBuffer.append("\n  ");
					}
					
					valueStringBuffer.append("}");
					
					value = valueStringBuffer.toString();
					
				} else if (isPropertyABean(name)) {
					Object bean = getPropertyBean(name,false);
					
					value = "[" + dumpPropertyBean(bean,name) +"]";
					
				} else if (isPropertyABeanArray(name)) {
					Object[] beanArray = getPropertyBeanArray(name,false);
					
					StringBuffer valueStringBuffer = new StringBuffer("{");
					
					if (beanArray.length > 0) valueStringBuffer.append("\n  ");

					for (int i=0; i<beanArray.length; i++) {
						valueStringBuffer.append("[");
						valueStringBuffer.append(dumpPropertyBean(beanArray[i],name,i));
						valueStringBuffer.append("]");

						if (i!=beanArray.length-1) valueStringBuffer.append(",");

						if (((i+1) % toStringItemsPerLine == 0) || (i==beanArray.length-1)) valueStringBuffer.append("\n  ");
					}
					
					valueStringBuffer.append("}");
					
					value = valueStringBuffer.toString();
					
				} else if (isPropertyABeanHash(name)) {
					PropertyBeanHash propHashTable = getPropertyBeanHash(name,false);

					StringBuffer valueStringBuffer = new StringBuffer("{");
					
					int n = propHashTable.keySet().size();
					
					if (n > 0) valueStringBuffer.append("\n  ");
					
					Iterator propHashIterator = getSortedIteratorFromSet(propHashTable.keySet());					

					int i = -1;
					
					while (propHashIterator.hasNext()) {
						i++;
						
						String key = (String) propHashIterator.next();
						Object bean = propHashTable.get(key);
						String _value = key + "=" + "[" + dumpPropertyBean(bean,name,key) +"]";
						
						valueStringBuffer.append(getDisplayablePropertyValue(name,key,_value));

						if (propHashIterator.hasNext()) valueStringBuffer.append(",");

						if (((i+1) % toStringItemsPerLine == 0) || (!propHashIterator.hasNext())) valueStringBuffer.append("\n  ");
					}
					valueStringBuffer.append("}");
					
					value = valueStringBuffer.toString();					
				}
			} catch (PropertyNotAvailableException pnae) {
				// This block shouldn't be reached, since this method cycles through all current properties
			}

			response.append(name);
			response.append("=");				
			response.append(value);
			response.append(EOL_SEQUENCE);
		}
		
		if (decodedProperties != null) {
			response.append("Decoded properties: ");
			dumpHashSet(response,decodedProperties);
			response.append(EOL_SEQUENCE);
		}
		
		if (decryptedProperties != null) {
			response.append("Decrypted properties: ");
			dumpHashSet(response,decryptedProperties);
			response.append(EOL_SEQUENCE);
		}

		return response.toString();
	}
	
	/**
	 * @param buffer - the StringBuffer to send the dump of the hashset
	 * @param hashset - the hashset to output
	 */
	private void dumpHashSet(StringBuffer buffer, HashSet hashset) {
		Iterator iterator = hashset.iterator();
		
		while(iterator.hasNext()) {
			String name = (String) iterator.next();
			buffer.append(name);
			if (iterator.hasNext()) buffer.append(",");
		}		
	}
	
	/**
	 * @param bean - the PropertyBean
	 * @param name - the property name
	 * @param arrayIndex - the index, if the PropertyBean is in an array, else supply -1 for PropertyBean and -2 for PropertyBeanHash
	 * @param hashKey - the hashKey to use, arrayIndex must be -2 else this is ignored
	 * @return Returns a String value of comma-separated values from the PropertyBean.
	 */
	private String dumpPropertyBean(Object bean, String name, int arrayIndex, String hashKey) {
		Method[] methods = bean.getClass().getMethods();
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<methods.length; i++) {
			Object value = null;
			String methodName = methods[i].getName();
			Class methodReturnType = methods[i].getReturnType();
			Class[] methodParameterTypes = methods[i].getParameterTypes();
			
			boolean startsWithGet = methodName.startsWith("get");
			boolean startsWithIs = methodName.startsWith("is");
			
			int positionOfFirstChar = startsWithGet ? 3 : 2;
			
			if ((startsWithGet || startsWithIs) && (methodName.length()>positionOfFirstChar) && (!methodName.equals("getClass")) && (methodReturnType != null) && (methodParameterTypes.length == 0)) {
				String parameter = methodName.substring(positionOfFirstChar,positionOfFirstChar+1).toLowerCase() + methodName.substring(positionOfFirstChar+1,methodName.length());
				try {
					value = methods[i].invoke(bean,null);
					if (value == null) { value = "null"; }
					buffer.append(parameter);
					buffer.append("=");
					if (arrayIndex == -1) {
						buffer.append(getDisplayablePropertyValue(name,name + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter,value.toString()));
					} else if (arrayIndex == -2) {
						buffer.append(getDisplayablePropertyValue(name,name + HABITAT4J_PROPERTY_TYPE_DELIMITER + hashKey + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter,value.toString()));
					} else {
						buffer.append(getDisplayablePropertyValue(name,name + HABITAT4J_PROPERTY_TYPE_DELIMITER + arrayIndex + HABITAT4J_PROPERTY_TYPE_DELIMITER + parameter,value.toString()));
					}
					buffer.append(",");
					
				} catch (Exception e) {
					//
				}				
			}
		}
		
		if (buffer.length() > 0) buffer.delete(buffer.length()-1,buffer.length());
		
		return buffer.toString();
	}

	/**
	 * @param bean - the PropertyBean
	 * @param name - the property name
	 * @return Returns a String value of comma-separated values from the PropertyBean.
	 */
	private String dumpPropertyBean(Object bean, String name) {
		return dumpPropertyBean(bean,name,-1,null);
	}

	/**
	 * @param bean - the PropertyBean
	 * @param name - the property name
	 * @param arrayIndex - the index, if the PropertyBean is in an array, else supply -1 for PropertyBean and -2 for PropertyBeanHash
	 * @return Returns a String value of comma-separated values from the PropertyBean.
	 */
	private String dumpPropertyBean(Object bean, String name, int arrayIndex) {
		return dumpPropertyBean(bean,name,arrayIndex,null);
	}

	/**
	 * @param bean - the PropertyBean
	 * @param name - the property name
	 * @param hashKey - the hashKey to use
	 * @return Returns a String value of comma-separated values from the PropertyBean.
	 */
	private String dumpPropertyBean(Object bean, String name, String hashKey) {
		return dumpPropertyBean(bean,name,-2,hashKey);
	}

	/**
	 * @param reloadEventHandler - the reloadEventHandler to set.
	 */
	public void setReloadEventHandler(Object reloadEventHandler) {
		this.reloadEventHandler = reloadEventHandler;
	}

	/**
	 * @returns Returns the reloadEventHandler.
	 */
	public Object getReloadEventHandler() {
		return reloadEventHandler;
	}
	
	/**
	 * @returns Returns whether this PropertyList has a reloadEventHandler defined.
	 */
	public boolean hasReloadEventHandler() {
		return (reloadEventHandler != null);
	}

	/**
	 * @return Returns the implementation of ReloadEventHandlerIF associated with this Property-List.
	 */
	public ReloadEventHandlerIF getReloadDefinitionInstance() {
		if (reloadEventHandler == null && globalReloadEventHandler == null) return null;
		
		Object _reloadEventHandler = null;
		
		if (reloadEventHandler != null) {
			_reloadEventHandler = reloadEventHandler;
			
		} else {
			_reloadEventHandler = globalReloadEventHandler;
		}
		
		try {
			ReloadEventHandlerIF object = null;
			
			if (_reloadEventHandler instanceof Class) {
				// First time around, there will be a Class in the decryptorDefinitions
				Class clazz = (Class) _reloadEventHandler;
				object = (ReloadEventHandlerIF) clazz.newInstance();
				
			} else {
				object = (ReloadEventHandlerIF) _reloadEventHandler;
			}
			
			return object;
			
		} catch (InstantiationException ie) {
			return null;
			
		} catch (IllegalAccessException iae) {
			return null;
		}
	}
	/**
	 * @return Returns the reloadBlock.
	 */
	public boolean isReloadBlock() {
		return reloadBlock;
	}
	/**
	 * @param reloadBlock - the reloadBlock to set.
	 */
	public void setReloadBlock(boolean reloadBlock) {
		this.reloadBlock = reloadBlock;
	}
	/**
	 * @return Returns the listModifiedBy.
	 */
	public String getListModifiedBy() {
		return listModifiedBy;
	}
	/**
	 * @param listModifiedBy - the listModifiedBy to set.
	 */
	public void setListModifiedBy(String listModifiedBy) {
		this.listModifiedBy = listModifiedBy;
	}
	/**
	 * @return Returns the listReloadSerial.
	 */
	public int getListReloadSerial() {
		return listReloadSerial;
	}
	/**
	 * @param listReloadSerial - the listReloadSerial to set.
	 */
	public void setListReloadSerial(int listReloadSerial) {
		this.listReloadSerial = listReloadSerial;
	}
	/**
	 * @return Returns the listVersion.
	 */
	public String getListVersion() {
		return listVersion;
	}
	/**
	 * @param listVersion - the listVersion to set.
	 */
	public void setListVersion(String listVersion) {
		this.listVersion = listVersion;
	}
	/**
	 * @return Returns the listName.
	 */
	public String getListName() {
		return listName;
	}
	/**
	 * @param listName - the listName to set.
	 */
	public void setListName(String listName) {
		this.listName = listName;
	}
	
	/**
	 * @param name - name of the Property to describe in the PropertyNotAvailableException
	 * @param correctType
	 * @throws PropertyNotAvailableException
	 */
	protected void throwPropertyNotAvailableExceptionForBadType(String name, String correctType) throws PropertyNotAvailableException {
		throw new PropertyNotAvailableException("Property \"" + name + "\" is not a " + correctType);
	}

	/**
	 * @param name - name of the Property to describe in the PropertyNotAvailableException
	 * @param reason - textual reason to describe in the PropertyNotAvailableException
	 * @throws PropertyNotAvailableException
	 */
	protected void throwPropertyNotAvailableException(String name, String reason) throws PropertyNotAvailableException {
		throw new PropertyNotAvailableException("Property \"" + name + "\" not available; " + reason);
	}

	/**
	 * @param name - name of the Property to describe in the PropertyNotAvailableException
	 * @throws PropertyNotAvailableException
	 */
	protected void checkThatPropertyExists(String name) throws PropertyNotAvailableException {
		if (!containsKey(name)) {
			throw new PropertyNotAvailableException("Property \"" + name + "\" does not exist");
		}
	}

	/**
	 * @return Returns whether the cloneObject() method will clone Objects implementing the Cloneable interface.
	 */
	public boolean isShouldCopyCloneableObjects() {
		return shouldCopyCloneableObjects;
	}

	/**
	 * @param shouldCopyCloneableObjects - sets whether the cloneObject() method should clone Objects implementing the Cloneable interface 
	 */
	public void setShouldCopyCloneableObjects(boolean shouldCopyCloneableObjects) {
		this.shouldCopyCloneableObjects = shouldCopyCloneableObjects;
	}

	/**
	 * @return Returns whether the cloneObject() method will clone Wrapper Objects.
	 */
	public boolean isShouldCopyWrapperObjects() {
		return shouldCopyWrapperObjects;
	}

	/**
	 * @param shouldCopyWrapperObjects - sets whether the cloneObject() method should clone Wrapper Objects
	 */
	public void setShouldCopyWrapperObjects(boolean shouldCopyWrapperObjects) {
		this.shouldCopyWrapperObjects = shouldCopyWrapperObjects;
	}

	/**
	 * @return Returns whether the cloneObject() method will clone String Objects.
	 */
	public boolean isShouldCopyStringObjects() {
		return shouldCopyStringObjects;
	}

	/**
	 * @param shouldCopyStringObjects - sets whether the cloneObject() method should clone String Objects
	 */
	public void setShouldCopyStringObjects(boolean shouldCopyStringObjects) {
		this.shouldCopyStringObjects = shouldCopyStringObjects;
	}
	
	/**
	 * @return Returns whether "WARN" logging during property bean copy should be suppressed.
	 */
	public boolean isShouldSuppressPropertyBeanCopyWarnings() {
		return shouldSuppressPropertyBeanCopyWarnings;
	}
	
	/**
	 * @param shouldSuppressPropertyBeanCopyWarnings - sets whether "WARN" logging during property bean copy should be suppressed
	 */
	public void setShouldSuppressPropertyBeanCopyWarnings(boolean shouldSuppressPropertyBeanCopyWarnings) {
		this.shouldSuppressPropertyBeanCopyWarnings = shouldSuppressPropertyBeanCopyWarnings; 
	}

	/**
	 * @return Returns number of toString() items per line.
	 */
	public int getToStringItemsPerLine() {
		return toStringItemsPerLine;
	}

	/**
	 * @param toStringItemsPerLine - the number of toString() items per line.
	 */
	public void setToStringItemsPerLine(int toStringItemsPerLine) {
		this.toStringItemsPerLine = toStringItemsPerLine;
	}
}
