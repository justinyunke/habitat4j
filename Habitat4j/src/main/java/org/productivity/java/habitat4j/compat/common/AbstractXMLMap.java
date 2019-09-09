package org.productivity.java.habitat4j.compat.common;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;

import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * AbstractXMLMap is an abstract class used by PropertyListXMLMap and ServerIdentityXMLMap
 * to facilitate mapping elements and attributes between a foreign XML schema and
 * the Habitat4J XML schema.
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
 * @version $Id: AbstractXMLMap.java,v 1.4 2005/09/03 22:45:22 cvs Exp $
 */
public abstract class AbstractXMLMap implements Habitat4JConstants {
	private final static String ELEMENT_ATTRIBUTE_KEY_DELIMITER_DEFAULT = "|";
	private final static String ELEMENT_DELIMITER_DEFAULT = "/";
	
	private Hashtable elementMap = null;
	private Hashtable attributeMap = null;
	
	private String elementAttributeKeyDelimiter = ELEMENT_ATTRIBUTE_KEY_DELIMITER_DEFAULT;
	private String elementDelimiter = ELEMENT_DELIMITER_DEFAULT;
	
	public AbstractXMLMap() {
		elementMap = new Hashtable();
		attributeMap = new Hashtable();
	}
	
	public void addElementMapping(String elementName, String habitat4jElementName, String habitat4jElementValue) {
		if (elementName == null || elementName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return;
		}
		
		ElementMapping elementMapping = new ElementMapping();
		elementMapping.setElementName(habitat4jElementName);
		elementMapping.setElementValue(habitat4jElementValue);
		
		elementMap.put(elementName,elementMapping);
	}
	
	public void addElementMapping(String elementName, String habitat4jElementName) {
		addElementMapping(elementName,habitat4jElementName,null);
	}

	public ElementMapping getElementMapping(String elementName) {
		if (elementName == null || elementName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return null;
		}
		
		return (ElementMapping) elementMap.get(elementName);
	}
	
	private String generateAttributeMappingKey(String elementName, String attributeName, String attributeValue) {
		StringBuffer mapKey = new StringBuffer(elementName);
	
		mapKey.append(getElementAttributeKeyDelimiter());
		mapKey.append(attributeName);

		if (attributeValue != null && !attributeValue.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			mapKey.append(getElementAttributeKeyDelimiter());
			mapKey.append(attributeValue);
		}
		
		return mapKey.toString();
	}

	private String generateAttributeMappingKey(String elementName, String attributeName) {
		return generateAttributeMappingKey(elementName,attributeName,null);
	}

	public AttributeMapping getAttributeMapping(String elementName, String attributeName, String attributeValue) {
		if (elementName == null || elementName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || attributeName == null || attributeName.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)
				|| attributeValue == null || attributeValue.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			return null;
		}
		
		String mapKey = null;
		AttributeMapping mapping = null;

		// Check complete match (attributeName + attributeValue)
		mapKey = generateAttributeMappingKey(elementName,attributeName,attributeValue);
		mapping = (AttributeMapping) attributeMap.get(mapKey);
		
		// If no match, check for attributeName only
		if (mapping == null) {
			mapKey = generateAttributeMappingKey(elementName,attributeName);
			mapping = (AttributeMapping) attributeMap.get(mapKey);
		}
		
		return mapping;
	}

	public AttributeMapping getAttributeMapping(String elementName, String attributeName) {
		return getAttributeMapping(elementName,attributeName,null);
	}

	public void addAttributeMapping(String elementName, String attributeName, String attributeValue, String habitat4jAttributeName, String habitat4jAttributeValue) {
		if (elementName == null || attributeName == null) {
			return;
		}

		// Build the mapping
		AttributeMapping attributeMapping = new AttributeMapping();
		attributeMapping.setAttributeName(habitat4jAttributeName);
		attributeMapping.setAttributeValue(habitat4jAttributeValue);
		
		// The mapKey will be elementName|attributeName or elementName|attributeName|attributeValue
		String mapKey = generateAttributeMappingKey(elementName,attributeName,attributeValue);
		
		attributeMap.put(mapKey,attributeMapping);
	}

	public void addAttributeMapping(String elementName, String attributeName, String attributeValue, String habitat4jAttributeName) {
		addAttributeMapping(elementName,attributeName,attributeValue,habitat4jAttributeName,null);
	}

	public void addAttributeMapping(String elementName, String attributeName, String habitat4jAttributeName) {
		addAttributeMapping(elementName,attributeName,null,habitat4jAttributeName,null);
	}

	/**
	 * @return Returns the attributeMap.
	 */
	public Hashtable getAttributeMap() {
		return attributeMap;
	}
	/**
	 * @return Returns the elementAttributeKeyDelimiter.
	 */
	public String getElementAttributeKeyDelimiter() {
		return elementAttributeKeyDelimiter;
	}
	/**
	 * @param elementAttributeKeyDelimiter The elementAttributeKeyDelimiter to set.
	 */
	public void setElementAttributeKeyDelimiter(String elementAttributeKeyDelimiter) {
		if (elementAttributeKeyDelimiter != null && !elementAttributeKeyDelimiter.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING))
			this.elementAttributeKeyDelimiter = elementAttributeKeyDelimiter;
	}
	/**
	 * @return Returns the elementMap.
	 */
	public Hashtable getElementMap() {
		return elementMap;
	}
	
	public Attributes getMappedAttributes(Attributes atts, StringBuffer elementPath) {
		AttributesImpl newAtts = new AttributesImpl();
		
		for (int i=0; i<atts.getLength(); i++) {
			String attributeName = atts.getLocalName(i);
			String attributeValue = atts.getValue(i);
			
			AttributeMapping mapping = getAttributeMapping(elementPath.toString(),attributeName,attributeValue);
			if (mapping != null) {
				if (mapping.getAttributeName() != null) {
					attributeName = mapping.getAttributeName();
					String mappedAttributeValue = mapping.getAttributeValue();
					if (mappedAttributeValue != null && !mappedAttributeValue.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
						attributeValue = mappedAttributeValue;
					}
					
					newAtts.addAttribute(atts.getURI(i),attributeName,attributeName,atts.getType(i),attributeValue);
				}
			} else {
				newAtts.addAttribute(atts.getURI(i),attributeName,attributeName,atts.getType(i),attributeValue);
			}
		}
		
		return newAtts;
	}
	
	public Stack getMappedElementStack(Stack stack, StringBuffer elementBuffer) {
		elementBuffer.setLength(0);

		Enumeration enumeration = stack.elements();
		while (enumeration.hasMoreElements()) {
			String element = (String) enumeration.nextElement();
			elementBuffer.append(element);
			
			if (enumeration.hasMoreElements()) {
				elementBuffer.append(getElementDelimiter());
			}
		}
		
		ElementMapping mapping = getElementMapping(elementBuffer.toString());
		if (mapping != null) {
			Stack newStack = new Stack();
			String mappedElementPath = mapping.getElementName();
			
			StringTokenizer tokenizer = new StringTokenizer(mappedElementPath,getElementDelimiter());
			
			while (tokenizer.hasMoreTokens()) {
				newStack.push(tokenizer.nextToken());
			}
			
			return newStack;
		}

		return null;
	}
	
	/**
	 * @return Returns the elementDelimiter.
	 */
	public String getElementDelimiter() {
		return elementDelimiter;
	}
	/**
	 * @param elementDelimiter The elementDelimiter to set.
	 */
	public void setElementDelimiter(String elementDelimiter) {
		this.elementDelimiter = elementDelimiter;
	}
}
