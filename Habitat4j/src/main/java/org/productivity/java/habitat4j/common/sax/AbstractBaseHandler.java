package org.productivity.java.habitat4j.common.sax;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JLogger;
import org.productivity.java.habitat4j.common.PropertyList;
import org.productivity.java.habitat4j.common.exception.BaseHandlerException;
import org.productivity.java.habitat4j.compat.common.AbstractXMLMap;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Abstract class used for handling the parsing of XML files and resources.
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
 * @version $Id: AbstractBaseHandler.java,v 1.34 2006/01/25 07:30:51 cvs Exp $
 */
public abstract class AbstractBaseHandler extends DefaultHandler implements Habitat4JConstants {
	protected Habitat4JLogger logger = null;
	protected AbstractXMLMap map = null;
	
	private XMLReader xmlReader = null;

	private Stack lastElement = new Stack();
	private StringBuffer lastElementInitializer = new StringBuffer();
	private StringBuffer lastElementSetter = new StringBuffer();
	private StringBuffer lastElementPath = new StringBuffer();
	private StringBuffer lastElementData = new StringBuffer();
	private boolean lastElementDataNull = true;
	
	private static final String NULL_EXCEPTION_MESSAGE = "Cannot set a null value to the primitive type bean parameter: ";

	/**
	 * @throws BaseHandlerException
	 */
	public AbstractBaseHandler() throws BaseHandlerException {		
		super();
		String logMethodName = this.getClass().getName() + " constructor - ";
		
		logger = Habitat4JLogger.getInstance();

		String saxDriver = System.getProperty(HABITAT4J_SAX_DRIVER_SYSTEM_PROPERTY_NAME);
		if (saxDriver == null || saxDriver.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			saxDriver = HABITAT4J_SAX_DRIVER_DEFAULT;
			logger.debug(
				logMethodName + "SAX Driver - SAX Driver not found in System.getProperty(\"" +
				HABITAT4J_SAX_DRIVER_SYSTEM_PROPERTY_NAME + "\"), using hardcoded default: " +
				saxDriver
			);
		}

		logger.debug(logMethodName + "SAX Driver - Preparing");
		
		try {
			// XML Parsing initialization
			xmlReader = XMLReaderFactory.createXMLReader(saxDriver);
		
		} catch (SAXException se) {
			logger.warn(logMethodName + "SAX Driver - XMLReaderFactory: " + se);
		}
		
		if (xmlReader == null) {
			try {
				Class xmlReaderClass = Class.forName(saxDriver);
				
				xmlReader = (XMLReader) xmlReaderClass.newInstance();

			} catch (ClassNotFoundException cnfe) {
				throw new BaseHandlerException(logMethodName + "SAX Driver - newInstance(): " + cnfe);
				
			} catch (InstantiationException ie) {
				throw new BaseHandlerException(logMethodName + "SAX Driver - newInstance(): " + ie);
				
			} catch (IllegalAccessException iae) {
				throw new BaseHandlerException(logMethodName + "SAX Driver - newInstance(): " + iae);
			}
		}

		if (xmlReader == null) {
			throw new BaseHandlerException(logMethodName + "SAX Driver - newInstance(): xmlReader is null");
		}

		xmlReader.setContentHandler(this);
		xmlReader.setErrorHandler(this);
		
		logger.debug(logMethodName + "SAX Driver - XMLReader instantiated");
	}

	/**
	 * This method returns the XSD validation resource path based on Habitat4JFeatures information.
	 * 
	 * @param features - the Habitat4JFeatures Hashtable
	 * @param featureXsdValidation - the name of the Habitat4JFeature to look up
	 * @param defaultXsdPath - the default XSD path
	 * @return Returns the XSD validation resource path.
	 */
	protected String getXsdValidationPath(Hashtable features, String featureXsdValidation, String defaultXsdPath) {
		// Get XSD feature setting
		String featureXsd = (String) features.get(featureXsdValidation);
		
		StringBuffer xsdValidationPath = new StringBuffer();	
		if (featureXsd != null) {
			xsdValidationPath.append(featureXsd);
		}

		// If the feature given is "false" then return null
		if (xsdValidationPath.length() <= 0
			|| HABITAT4J_GENERIC_VALUE_FALSE.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_OFF.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_ZERO.equalsIgnoreCase(xsdValidationPath.toString())
			) {
			return null;
		}

		// If the feature given is "true" or "default" then use the default path found in the Constants interface.
		if (HABITAT4J_GENERIC_VALUE_DEFAULT.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_TRUE.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_ON.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_ONE.equalsIgnoreCase(xsdValidationPath.toString())
			|| HABITAT4J_GENERIC_VALUE_YES.equalsIgnoreCase(xsdValidationPath.toString())) {
			xsdValidationPath.setLength(0);
			xsdValidationPath.append(defaultXsdPath);
		}

		// Check to see if the path given can be looked up via the class loader.
		URL xsdValidationResourceURL = this.getClass().getClassLoader().getResource(xsdValidationPath.toString());
		if (xsdValidationResourceURL != null) {
			xsdValidationPath.setLength(0);
			xsdValidationPath.append(xsdValidationResourceURL.toString());
		}
		
		return xsdValidationPath.toString();
	}
		
	/**
	 * This method sets up the XSD validator.
	 * 
	 * @param xsdPath
	 */
	private void configureXSDValidator(String xsdPath) {
		String logMethodName = this.getClass().getName() + ".configureXSDValidator(String xsdPath) - ";
		
		boolean activate = (xsdPath != null);
		try {
			// Sets the XML Parser features for XSD validation
			xmlReader.setFeature(HABITAT4J_SAX_VALIDATION_FEATURE,activate);
			xmlReader.setFeature(HABITAT4J_SAX_SCHEMA_FEATURE,activate);
			if (activate) {
				xmlReader.setProperty(HABITAT4J_SAX_XSD_LOCATION,xsdPath);
			}
			
		} catch (SAXNotRecognizedException snre) {
			logger.debug(logMethodName + "Could not set XML parser features: " + snre);
			
		} catch (SAXNotSupportedException snse) {
			logger.debug(logMethodName + "Could not set XML parser features: " + snse);			
		}
	}

	/**
	 * This method invokes a parameter-less method using reflection.
	 * 
	 * @param methodName - the name of the method to invoke
	 */
	protected void invokeElementInitializer(String methodName) {
		String logMethodName = this.getClass().getName() + ".invokeElementInitializer(String methodName) - ";
		Method meth = null;
		
		// Define the method and its parameters
		try {
            meth = this.getClass().getMethod(methodName,null);

		} catch (NoSuchMethodException nsme) {
			logger.debug(logMethodName + "Skipped invoking non-existent method: " + methodName + "()");
			return;
		}

		// Invoke the method
		try {
            meth.invoke(this, null);
			logger.debug(logMethodName + "Successfully invoked " + methodName + "()");
            
		} catch (Exception e) {
			logger.error(logMethodName + "Unexpected Exception on methodName \"" + methodName + "\": " + e);
		}				
	}
	
	/**
	 * This method invokes a method using inflection.
	 * 
	 * @param methodName - the name of the method to invoke
	 * @param atts - the attributes to send the method
	 */
	protected void invokeSetter(String methodName,Attributes atts) {
		if (HABITAT4J_GENERIC_VALUE_EMPTY_STRING.equals(methodName)) {
			return;
		}
	
		Method meth = null;
		String logMethodName = this.getClass().getName() + ".invokeSetter(String methodName,Attributes atts) - ";
		
		// Define the method and its parameters
		try {
			Class partypes[] = new Class[1];
            partypes[0] = Attributes.class;
            meth = this.getClass().getMethod(methodName, partypes);

		} catch (NoSuchMethodException nsme) {
			logger.debug(logMethodName + "Skipped invoking non-existent method: " + methodName + "(Attributes atts)");
			return;
		}

		// Invoke the method
		try {
            Object arglist[] = new Object[1];
            arglist[0] = atts;
            meth.invoke(this, arglist);
			logger.debug(logMethodName + "Successfully invoked " + methodName + "(Attributes atts)");
            
		} catch (Exception e) {
			logger.error(logMethodName + "Unexpected Exception on methodName \"" + methodName + "\": " + e);
		}		
	}

	/**
	 * This method invokes a method using inflection.
	 * 
	 * @param methodName - name of method to invoke
	 * @param data - String data to send the method
	 */
	protected void invokeSetter(String methodName,String data) {
		if (HABITAT4J_GENERIC_VALUE_EMPTY_STRING.equals(methodName)) {
			return;
		}

		Method meth = null;
		String logMethodName = this.getClass().getName() + ".invokeSetter(String methodName,String data) - ";
		
		// Define the method and its parameters
		try {
			Class partypes[] = new Class[1];
            partypes[0] = String.class;
            meth = this.getClass().getMethod(methodName, partypes);
            
		} catch (NoSuchMethodException nsme) {
			logger.debug(logMethodName + "Skipped invoking non-existent method: " + methodName + "(String data)");
			return;
		}
		
		// Invoke the method
   		try {
            Object arglist[] = new Object[1];
            arglist[0] = data;
            meth.invoke(this, arglist);
			logger.debug(logMethodName + "Successfully invoked " + methodName + "(String data)");
            
		} catch (IllegalAccessException iae) {
			logger.error(logMethodName + "Unexpected Exception on methodName \"" + methodName + "\" with data \"" + data + "\": " + iae);
			
		} catch (IllegalArgumentException iarge) {
			logger.error(logMethodName + "Unexpected Exception on methodName \"" + methodName + "\" with data \"" + data + "\": " + iarge);
			
		} catch (InvocationTargetException ite) {
			logger.error(logMethodName + "Unexpected Exception on methodName \"" + methodName + "\" with data \"" + data + "\": " + ite);
    	}				
	}

	/**
	 * This method indicates whether a PropertyBean parameter is supported by Habitat4J.
	 * 
	 * @param clazz = the class to verify
	 * @return Returns whether a class is a supported PropertyBean parameter.
	 */
	private boolean isClassASupportedBeanParameter(Class clazz) {
		return 
			(clazz == short.class) || (clazz == Short.class) ||
			(clazz == int.class) || (clazz == Integer.class) ||
			(clazz == long.class) || (clazz == Long.class) ||
			
			(clazz == double.class) || (clazz == Double.class) ||
			(clazz == float.class) || (clazz == Float.class) ||
			
			(clazz == boolean.class) || (clazz == Boolean.class) ||
			
			(clazz == char.class) || (clazz == Character.class) ||

			(clazz == BigInteger.class) ||
			(clazz == BigDecimal.class) ||

			(clazz == String.class);			
	}
	
	/**
	 * This method converts a parameter from a String to the appropriate Class type,
	 * and stores it in an Object array for use in invoking a method during reflection.
	 * 
	 * @param parameterClass - the parameter's Class designation
	 * @param _parameter - the name of the parameter
	 * @param value - the value of the parameter
	 * @return Returns an array of Objects that contains a single parameter instance.
	 * @throws IllegalArgumentException
	 */
	private Object[] getBeanArgumentList(Class parameterClass, String _parameter, String value) throws IllegalArgumentException {
		Object arglist[] = new Object[1];
		String parameter = _parameter.substring(0,1).toLowerCase() + _parameter.substring(1,_parameter.length());

		if (parameterClass == short.class || parameterClass == Short.class) {
			arglist[0] = convertShortParameter(parameter,value,parameterClass == short.class);
				
		} else if (parameterClass == int.class || parameterClass == Integer.class) {
			arglist[0] = convertIntegerParameter(parameter,value,parameterClass == int.class);

		} else if (parameterClass == long.class || parameterClass == Long.class) {
			arglist[0] = convertLongParameter(parameter,value,parameterClass == long.class);
		
		} else if (parameterClass == double.class || parameterClass == Double.class) {
			arglist[0] = convertDoubleParameter(parameter,value,parameterClass == double.class);
	   				
		} else if (parameterClass == float.class || parameterClass == Float.class) {
			arglist[0] = convertFloatParameter(parameter,value,parameterClass == float.class);
	   				
		} else if (parameterClass == boolean.class || parameterClass == Boolean.class) {
			arglist[0] = convertBooleanParameter(parameter,value,parameterClass == boolean.class);
	   				
		} else if (parameterClass == char.class || parameterClass == Character.class) {
			arglist[0] = convertCharacterParameter(parameter,value,parameterClass == char.class);
	   				
		} else if (parameterClass == BigInteger.class) {
			arglist[0] = convertBigIntegerParameter(value);
	   				
		} else if (parameterClass == BigDecimal.class) {
			arglist[0] = convertBigDecimalParameter(value);
	   				
		} else if (parameterClass == String.class) {
			arglist[0] = value;
	   				
		} else {
			throw new IllegalArgumentException("Invalid parameter type [should have been caught earlier by isClassASupportedBeanParameter()]");
		}
		
		return arglist;
	}
	
	/**
	 * This method converts a String into a Short object (primitive or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Short.
	 * @throws IllegalArgumentException
	 */
	private Short convertShortParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Short result = null;
		
		try {
			result = new Short(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a Integer object (primitive or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Integer.
	 * @throws IllegalArgumentException
	 */
	private Integer convertIntegerParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Integer result = null;
		
		try {
			result = new Integer(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a BigInteger object (wrapped only). 
	 *  
	 * @param value - the String value fo the parameter to convert
	 * @return Returns an instance of BigInteger.
	 * @throws IllegalArgumentException
	 */
	private BigInteger convertBigIntegerParameter(String value) throws IllegalArgumentException {
		if (value == null) return null;
		
		BigInteger result = null;
		
		try {
			result = new BigInteger(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a Long object (primitive or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Long.
	 * @throws IllegalArgumentException
	 */
	private Long convertLongParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Long result = null;
		
		try {
			result = new Long(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a Double object (primitive or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Double.
	 * @throws IllegalArgumentException
	 */
	private Double convertDoubleParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Double result = null;
		
		try {
			result = new Double(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a Float object (primitive or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Float.
	 * @throws IllegalArgumentException
	 */
	private Float convertFloatParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Float result = null;
		
		try {
			result = new Float(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a BigDecimal object (wrapped only). 
	 *  
	 * @param value - the String value fo the parameter to convert
	 * @return Returns an instance of BigDecimal.
	 * @throws IllegalArgumentException
	 */
	private BigDecimal convertBigDecimalParameter(String value) throws IllegalArgumentException {
		if (value == null) return null;
		
		BigDecimal result = null;
		
		try {
			result = new BigDecimal(value);
			
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(e.toString());
		}

		return result;
	}

	/**
	 * This method converts a String into a Character object (primitive char or wrapped). 
	 *  
	 * @param parameter - the String value of the parameter to convert
	 * @param value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Character.
	 * @throws IllegalArgumentException
	 */
	private Character convertCharacterParameter(String parameter, String value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && value == null) return null;
		if (isPrimitive && value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		Character result = null;
		
		if (value.length() == 1) {
			result = new Character(value.charAt(0));
			
		} else if (value.length() != 0) {
			throw new IllegalArgumentException("Invalid character, since data contains more than one character.");
		}
			
		return result;
	}

	/**
	 * This method converts a String into a Boolean object (primitive or wrapped). 
	 *  
	 * @param parameter - the String name of the parameter to convert
	 * @param _value - the String value fo the parameter to convert
	 * @param isPrimitive - whether this value is a primitive type (int, char, etc.)
	 * @return Returns an instance of Boolean.
	 * @throws IllegalArgumentException
	 */
	private Boolean convertBooleanParameter(String parameter, String _value, boolean isPrimitive) throws IllegalArgumentException {
		if (!isPrimitive && _value == null) return null;
		if (isPrimitive && _value == null) throw new IllegalArgumentException(NULL_EXCEPTION_MESSAGE + parameter);
		
		boolean result;
		
		String value = _value.trim().toLowerCase();

		result = 
			value.equals(HABITAT4J_GENERIC_VALUE_TRUE) ||
			value.equals(HABITAT4J_GENERIC_VALUE_ON) ||
			value.equals(HABITAT4J_GENERIC_VALUE_ONE) ||
			value.equals(HABITAT4J_GENERIC_VALUE_YES);
			
		if (!result) {
			boolean falseResult =  
				value.equals(HABITAT4J_GENERIC_VALUE_FALSE) ||
				value.equals(HABITAT4J_GENERIC_VALUE_OFF) ||
				value.equals(HABITAT4J_GENERIC_VALUE_ZERO) ||
				value.equals(HABITAT4J_GENERIC_VALUE_NO) ||
				value.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING);
			
			if (!falseResult) {
				throw new IllegalArgumentException("Invalid boolean value.");				
			}
		}
		
		return new Boolean(result);
	}
	
	/**
	 * This method sets arbitrary bean parameters in an object using reflection.
	 * 
	 * @param bean - the bean object 
	 * @param parameter - the parameter that will be used to form a setter method
	 * @param value - the value to use in the setter method
	 */
	protected void setBeanParameter(Object bean, String parameter, String value) {
		String logMethodName = this.getClass().getName() + ".setPropertyBeanParameter(Object propertyBean, String parameter, String value) - ";
		
		if (bean == null) {
			logger.error(logMethodName + "Skipped setting the PropertyBean, since it is empty.");
			
			return;
		}

		Method meth = null;
		Class partype = null;
		String methodName = "set" + parameter;
		
		try {
			Method[] methods = bean.getClass().getMethods();
			
			// Cycle through all methods found in the PropertyBean
			for (int i=0; i<methods.length; i++) {
				String name = methods[i].getName();

				// Match the setter method name
				if (name.equals(methodName)) {
					Class partypes[] = methods[i].getParameterTypes();
					
					// Setter methods this code handles should only have one argument
					if (partypes.length == 1) {
						partype = partypes[0];
						
						// First, make sure it's a supported PropertyBean parameter type
						if (isClassASupportedBeanParameter(partype)) {
							meth = bean.getClass().getMethod(methodName, partypes);
							break;
						}
					}
				}
			}
			
		} catch (SecurityException se) {
			logger.warn(logMethodName + "Skipped invoking non-existent method: " + methodName + "(String data)");
			return;
			
		} catch (NoSuchMethodException nsme) {
			logger.warn(logMethodName + "Skipped invoking non-existent method: " + methodName + "(String data)");
			return;
		}
		
		if (meth == null) {
			logger.warn(logMethodName + "Skipped invoking non-existent method: " + methodName + "(String data)");
			return;
		}

		// Invoke the setter method
   		try {
   			// Get teh arguments (any required type conversion happens here)
   			Object arglist[] = getBeanArgumentList(partype,parameter,value);
			meth.invoke(bean, arglist);

			logger.debug(logMethodName + "Successfully invoked " + methodName + "(String data)");
            
		} catch (IllegalAccessException iae) {
			logger.error(logMethodName + "Unexpected Exception: " + iae);
			
		} catch (IllegalArgumentException iarge) {
			logger.warn(logMethodName + "Unexpected Exception: " + iarge);
			
		} catch (InvocationTargetException ite) {
			logger.error(logMethodName + "Unexpected Exception: " + ite);

		} catch (Exception e) {
			logger.error(logMethodName + "Unexpected Exception: " + e);
		}
	}
	
	
	/**
	 * @param buffer
	 * @param what
	 * @return Returns an integer.
	 */
	private int indexOfBuffer(StringBuffer buffer, char what) {
		int result = -1;
		
		for (int i=0; i<buffer.length(); i++) {
			char charToMatch = buffer.charAt(i);
			
			if (charToMatch == what) {
				result = i;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * This method replaces the "-" delimiter in a StringBuffer with Java-style uppercase
	 * Strings.  Example:  "this-string-here" will turn into "ThisStringHere"
	 * 
	 * @param buffer - the StringBuffer to modify
	 */
	private void replaceDelimiter(StringBuffer buffer) {
		// If the buffer is empty, skip this method
		if (buffer.length() == 0) return;

		// Remove delimiters found at the end
		while (buffer.length()!=0 && buffer.substring(buffer.length()-1).charAt(0) == HABITAT4J_XML_ELEMENT_DELIMITER) {
			buffer.setLength(buffer.length()-1);
		}
		
		// Find the first index
		// int dashIndex = buffer.indexOf(HABITAT4J_XML_ELEMENT_DELIMITER); - J2SE 1.4+
		int dashIndex = indexOfBuffer(buffer,HABITAT4J_XML_ELEMENT_DELIMITER);
		
		// Keep doing this until there's no more delimiter found
		while (dashIndex != -1) {
			int endReplaceIndex;
			String nextChar = null;

			// If it's not at the end of the buffer, grab the new following character
			// and set the endReplaceIndex
			if (dashIndex+2 < buffer.length()) {
				nextChar = buffer.substring(dashIndex+1,dashIndex+2).toUpperCase();
				endReplaceIndex = dashIndex+2;
				
			// It's at the end of the buffer 
			} else {
				nextChar = HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
				endReplaceIndex = dashIndex+1;
			}
			
			// Replace the section of buffer
			buffer.replace(dashIndex,endReplaceIndex,nextChar);

			// Find the next index
			dashIndex = indexOfBuffer(buffer,HABITAT4J_XML_ELEMENT_DELIMITER);
		}
	}
	
	/**
	 * This method puts together various element identifiers.
	 * 
	 * @param _stack - an element stack
	 * @param _atts - attributes
	 * @param elementPath - the element's path
	 * @param elementInitializer - an element initializer
	 * @param elementSetter - an element setter
	 * @return Returns the mapped attributes
	 */
	private Attributes setLastElementVariables(Stack _stack, StringBuffer elementPath, StringBuffer elementInitializer, StringBuffer elementSetter, Attributes _atts) {
		Stack stack = null;
		Attributes atts = _atts;
		
		if (map != null) {
			StringBuffer elementBuffer = new StringBuffer();
			stack = map.getMappedElementStack(_stack,elementBuffer);
			atts = map.getMappedAttributes(_atts,elementBuffer);
		}
		
		if (stack == null) {
			stack = _stack;
		}
		
		Iterator elements = stack.iterator();
		
		elementPath.setLength(0);
		elementInitializer.setLength(0);
		elementSetter.setLength(0);

		while (elements.hasNext()) {
			String element = (String)elements.next();
			
			if (elementSetter.length() != 0) {
				elementPath.append("/");
			}
			elementPath.append(element);
			
			String javaPath = element.substring(0,1).toUpperCase() + element.substring(1,element.length());

			if (elementInitializer.length() == 0) {
				elementInitializer.append("init");
			}
			elementInitializer.append(javaPath);

			if (elementSetter.length() == 0) {
				elementSetter.append("set");
			}
			elementSetter.append(javaPath);
		}

		replaceDelimiter(elementInitializer);
		replaceDelimiter(elementSetter);
		
		return atts;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String name, String qName, Attributes _atts) { 
		lastElementData.setLength(0);
		lastElementDataNull=true;

		lastElement.push(name);

		Attributes atts = setLastElementVariables(lastElement,lastElementPath,lastElementInitializer,lastElementSetter,_atts);
		
		invokeElementInitializer(lastElementInitializer.toString());
		invokeSetter(lastElementSetter.toString(),atts);			
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String name, String qName) {
		// Snag the last item off of the lastElement stack
		lastElement.pop();

		// IF the data's null, invoke the setter with a null value
		if (lastElementDataNull) {
			// Gotta send an explicit String null value to invokeSetter
			String value = null;
			invokeSetter(lastElementSetter.toString(),value);
			
		// IF the data's null, invoke the setter with a null value
		} else {
			invokeSetter(lastElementSetter.toString(),lastElementData.toString());			
		}
		
		lastElementSetter.setLength(0);
		lastElementPath.setLength(0);
		lastElementData.setLength(0);
		lastElementInitializer.setLength(0);
		lastElementDataNull = true;
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// Sets the 'null' flag to false
		lastElementDataNull=false;
		
		// Adds the data into a StringBuffer
		for (int i=arg1; i<(arg1 + arg2); i++) {
			lastElementData.append(arg0[i]);
		}
	}

	/**
	 * This method parses XML found in an InputStream with XSD validation.
	 * 
	 * @param inputStream
	 * @param xsdResourcePath
	 * @param resourceName - the location of the stream (for logging)
	 * @throws BaseHandlerException
	 */
	public void parse(InputStream inputStream, String xsdResourcePath, String resourceName) throws BaseHandlerException {
		String logMethodName = this.getClass().getName() + ".parse(InputStream inputStream,String xsdResourcePath) - ";
		InputStreamReader inputStreamReader = null;

		logger.debug(logMethodName + "Reading stream from: " + resourceName);
		inputStreamReader = new InputStreamReader(inputStream);
			
		try {
			configureXSDValidator(xsdResourcePath);
			xmlReader.parse(new InputSource(inputStreamReader));
			logger.debug(logMethodName + "XML parsing completed");
			
		} catch (IOException ioe) {
			throw new BaseHandlerException(logMethodName + ioe + " (" + resourceName + ")");
			
		} catch (SAXException se) {
			throw new BaseHandlerException(logMethodName + se + " (" + resourceName + ")");
		}		
	}

	/**
	 * This method parses an XML file with XSD validation.
	 * 
	 * @param filePath - the file path to parse
	 * @param xsdResourcePath - the resource path containing the XSD document
	 * @return Returns the last modified time.
	 * @throws BaseHandlerException
	 */
	public long parse(String filePath, String xsdResourcePath) throws BaseHandlerException {
		FileReader fileReader = null;
		String logMethodName = this.getClass().getName() + ".parse(String filePath,String xsdResourcePath) - ";
		long lastModified = -1;
		
		try {
			logger.debug(logMethodName + "Reading file: " + filePath);
			lastModified = PropertyList.getCurrentLastModified(filePath);
			fileReader = new FileReader(filePath);
			
		} catch (FileNotFoundException fnfe) {
			logger.error(logMethodName + fnfe);
			throw new BaseHandlerException(fnfe);
		}
		
		try {
			configureXSDValidator(xsdResourcePath);
			xmlReader.parse(new InputSource(fileReader));
			logger.debug(logMethodName + "XML parsing completed");
			return lastModified;
			
		} catch (IOException ioe) {
			logger.error(logMethodName + ioe);
			throw new BaseHandlerException(ioe);
			
		} catch (SAXException se) {
			logger.error(logMethodName + se);
			throw new BaseHandlerException(se);
		}
	}
	
	/**
	 * This method parses XML found in an InputStream.
	 * 
	 * @param inputStream - the stream containing XML to parse
	 * @param resourceName - the location of the stream (for logging)
	 * @throws BaseHandlerException
	 */
	public void parse(InputStream inputStream, String resourceName) throws BaseHandlerException {
		parse(inputStream,null,resourceName);
	}
	
	/**
	 * This method parses an XML file.
	 * 
	 * @param filePath - the file path to parse
	 * @return Returns the last modified time.
	 * @throws BaseHandlerException
	 */
	public long parse(String filePath) throws BaseHandlerException {
		return parse(filePath,null);
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
	 */
	public void warning(SAXParseException e) throws SAXException {
		logger.warn(saxParseExceptionDetails("SAX Parse Warning: ",e));
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
	 */
	public void error(SAXParseException e) throws SAXException {
		throw new SAXException(saxParseExceptionDetails("SAX Parse Error: ",e));
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
	 */
	public void fatalError(SAXParseException e) throws SAXException {
		throw new SAXException(saxParseExceptionDetails("SaxParse Fatal Error: ",e));
	}
	
	/**
	 * @param prefix - the prefix to place at the beginning of the returned String
	 * @param e - the SAX exception
	 * @return a formatted String value for purposes of debugging/logging
	 */
	private String saxParseExceptionDetails(String prefix, SAXParseException e) {
		StringBuffer details = new StringBuffer(prefix);
		
		details.append(" [PublicId:");
		details.append(e.getPublicId());
		details.append("] ");
		
		details.append("[SystemId:");
		details.append(e.getSystemId());
		details.append("] ");
		
		details.append("[Line/Column:");
		details.append(e.getLineNumber());
		details.append("/");
		details.append(e.getColumnNumber());
		details.append("] ");
				
		details.append("[Message:");
		details.append(e.getMessage());
		details.append("]");
		
		return details.toString();
	}
}
