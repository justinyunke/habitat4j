package org.productivity.java.habitat4j.common;

/**
 * Contains constant (public static final) entries used by Habitat4J.
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
 * @version $Id: Habitat4JConstants.java,v 1.43 2007/02/25 04:23:10 cvs Exp $
 */
public interface Habitat4JConstants {
	public static final String HABITAT4J_VERSION											= "1.0.0rc18 2007-03-09 jpy";
	
	public static final String SAX_DRIVER_SYSTEM_PROPERTY_NAME								= "org.xml.sax.driver";
	
	public static final String HABITAT4J_SAX_DRIVER_SYSTEM_PROPERTY_NAME 					= "org.productivity.java.habitat4j.sax.driver";
	public static final String HABITAT4J_SAX_DRIVER_DEFAULT									= "org.apache.xerces.parsers.SAXParser";

	public static final String HABITAT4J_WINDOWS_OS_NAME_LIST_PROPERTY_NAME					= "org.productivity.java.habitat4j.windows.os.name.list";
	public static final String HABITAT4J_UNIX_OS_NAME_LIST_PROPERTY_NAME					= "org.productivity.java.habitat4j.unix.os.name.list";

	public static final String HABITAT4J_SAX_VALIDATION_FEATURE								= "http://xml.org/sax/features/validation";
	public static final String HABITAT4J_SAX_SCHEMA_FEATURE									= "http://apache.org/xml/features/validation/schema";
	public static final String HABITAT4J_SAX_XSD_LOCATION									= "http://apache.org/xml/properties/schema/" + "external-noNamespaceSchemaLocation";

	public static final String HABITAT4J_SERVER_IDENTITY_XSD_RESOURCE_PATH_DEFAULT			= "org/productivity/java/habitat4j/common/xsd/server-identity.xsd";
	public static final String HABITAT4J_PROPERTY_LIST_XSD_RESOURCE_PATH_DEFAULT			= "org/productivity/java/habitat4j/common/xsd/property-list.xsd";
	
	public static final String HABITAT4J_GENERIC_VALUE_DEFAULT								= "default";	
	
	public static final String HABITAT4J_GENERIC_VALUE_EMPTY_STRING							= "";	

	public static final String HABITAT4J_GENERIC_VALUE_TRUE									= "true";
	public static final String HABITAT4J_GENERIC_VALUE_ON									= "on";
	public static final String HABITAT4J_GENERIC_VALUE_ONE									= "1";
	public static final String HABITAT4J_GENERIC_VALUE_YES									= "yes";
	
	public static final String HABITAT4J_GENERIC_VALUE_FALSE								= "false";
	public static final String HABITAT4J_GENERIC_VALUE_OFF									= "off";
	public static final String HABITAT4J_GENERIC_VALUE_ZERO									= "0";
	public static final String HABITAT4J_GENERIC_VALUE_NO									= "no";
	
	public static final String HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME		= "org.productivity.java.habitat4j.serverIdentityFilePath";
	public static final String HABITAT4J_SERVER_IDENTITY_FILE_PATH_DEFAULT					= "/server-identity.xml";
	
	public static final String HABITAT4J_APPNAME_DEFAULT									= "default";
	public static final String HABITAT4J_APPNAME_ATTRIBUTE_NAME								= "name";

	public static final String HABITAT4J_ROLE_ATTRIBUTE_NAME								= "name";

	public static final String HABITAT4J_PRAGMA_ATTRIBUTE_NAME								= "name";	
	public static final String HABITAT4J_PRAGMA_ATTRIBUTE_VALUE								= "value";
	
	public static final String HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_PREFIX					= "${";
	public static final String HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_DELIMITER				= ":";
	public static final String HABITAT4J_PROPERTY_VALUE_SUBSTITUTION_SUFFIX					= "}";

	public static final String HABITAT4J_DEFINITIONS_PACKAGE								= "package";
	
	public static final String HABITAT4J_PROPERTY_LIST_NAME_DEFAULT							= "default";	
	public static final String HABITAT4J_PROPERTY_NAME_ATTRIBUTE_NAME						= "name";	
	public static final String HABITAT4J_PROPERTY_VALUE_ATTRIBUTE_NAME						= "value";
	public static final String HABITAT4J_PROPERTY_DECODING_ATTRIBUTE_NAME					= "decoderId";
	public static final String HABITAT4J_PROPERTY_DECRYPTING_ATTRIBUTE_NAME					= "decryptorId";

	public static final String HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_NAME				= "instance";

	public static final String HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE	= "replace";
	public static final String HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND	= "prepend";
	public static final String HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND		= "append";
	public static final String HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT	= HABITAT4J_PROPERTY_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE; 

	public static final String HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_NAME				= "instance";
	public static final String HABITAT4J_PROPERTY_HASH_TYPE_ATTRIBUTE_NAME					= "type";
	public static final String HABITAT4J_PROPERTY_HASH_KEY_ATTRIBUTE_NAME					= "key";

	public static final String HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE		= "replace";
	public static final String HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REVISE		= "revise";
	public static final String HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_DEFAULT		= HABITAT4J_PROPERTY_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE; 

	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_ID_ATTRIBUTE_NAME				= "id";
	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_CLASS_ATTRIBUTE_NAME			= "class";
	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_ATTRIBUTE_NAME	= "copyMethod";

	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_REFLECT	= "reflect";
	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_SERIALIZE	= "serialize";
	public static final String HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_DEFAULT	= HABITAT4J_PROPERTY_BEAN_DESCRIPTOR_COPY_METHOD_VALUE_REFLECT;

	public static final String HABITAT4J_PROPERTY_BEAN_ID_ATTRIBUTE_NAME					= "id";
	public static final String HABITAT4J_PROPERTY_BEAN_NAME_ATTRIBUTE_NAME					= "name";
	public static final String HABITAT4J_PROPERTY_BEAN_VALUE_ATTRIBUTE_NAME					= "value";
	public static final String HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_NAME				= "instance";

	public static final String HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_REPLACE		= "replace";
	public static final String HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_REVISE		= "revise";
	public static final String HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_DEFAULT		= HABITAT4J_PROPERTY_BEAN_INSTANCE_ATTRIBUTE_VALUE_REPLACE; 

	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE		= "replace";
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_PREPEND		= "prepend";
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_APPEND		= "append";
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_DEFAULT		= HABITAT4J_PROPERTY_BEAN_ARRAY_INSTANCE_ATTRIBUTE_VALUE_REPLACE;

	public static final String HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE	= "replace";
	public static final String HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REVISE		= "revise";
	public static final String HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_DEFAULT	= HABITAT4J_PROPERTY_BEAN_HASH_INSTANCE_ATTRIBUTE_VALUE_REPLACE;

	public static final String HABITAT4J_CONTEXT_APPLICATION_NAME							= "applicationName";
	
	public static final String HABITAT4J_CONTEXT_PREFIX										= "prefix";

	public static final String HABITAT4J_CONTEXT_ENVIRONMENT								= "environment";

	public static final String HABITAT4J_CONTEXT_INSTANCE_NAME								= "instanceName";
	public static final String HABITAT4J_CONTEXT_INSTANCE_DESCRIPTION						= "instanceDescription";
	public static final String HABITAT4J_CONTEXT_INSTANCE_ENUMERATION						= "instanceEnumeration";
	public static final String HABITAT4J_CONTEXT_INSTANCE_OS								= "instanceOS";

	public static final String HABITAT4J_CONTEXT_ROLE_NAME									= "roleName";
	public static final String HABITAT4J_CONTEXT_ROLE_TYPE									= "roleType";
	public static final String HABITAT4J_CONTEXT_ROLE_VENDOR								= "roleVendor";
	public static final String HABITAT4J_CONTEXT_ROLE_PRODUCT								= "roleProduct";
	public static final String HABITAT4J_CONTEXT_ROLE_VERSION								= "roleVersion";
	public static final String HABITAT4J_CONTEXT_ROLE_PATCH_LEVEL							= "rolePatchLevel";

	public static final byte HABITAT4J_PROPERTY_KIND_HABITAT4J								= 1;
	public static final byte HABITAT4J_PROPERTY_KIND_SYSTEM									= 2;
	public static final byte HABITAT4J_PROPERTY_KIND_SECURITY								= 3;	
	
	public static final String[] HABITAT4J_CONTEXT_LEGAL_ROLE_NAMES							= {
			"appServer",
			"dbServer",
			"webServer"
	};

	public static final char HABITAT4J_XML_ELEMENT_DELIMITER								= '-';
	public static final String HABITAT4J_PROPERTY_TYPE_DELIMITER							= "~";
	
	public static final String HABITAT4J_TEMP_PROPERTY_LIST_PREFIX							= "temp_";
	
	public static final String HABITAT4J_PROPERTY_ARRAY_ITEM_METHOD_PREFIX					= "setPropertyListContextPropertyArrayItem";	

	public static final String HABITAT4J_PROPERTY_HASH_ITEM_METHOD_PREFIX					= "setPropertyListContextPropertyHashItem";	
	
	public static final String HABITAT4J_PROPERTY_BEAN_INIT_METHOD_PREFIX					= "initPropertyListContextPropertyBean";

	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_INIT_METHOD_PREFIX				= "initPropertyListContextPropertyBeanArray";
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_INIT_METHOD_PREFIX		= "initPropertyListContextPropertyBeanArrayItem";

	public static final String HABITAT4J_PROPERTY_BEAN_HASH_INIT_METHOD_PREFIX				= "initPropertyListContextPropertyBeanHash";
	public static final String HABITAT4J_PROPERTY_BEAN_HASH_ITEM_INIT_METHOD_PREFIX			= "initPropertyListContextPropertyBeanHashItem";

	public static final String HABITAT4J_PROPERTY_BEAN_SETTER_METHOD_PREFIX					= "setPropertyListContextPropertyBean";
	
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_SETTER_METHOD_PREFIX			= "setPropertyListContextPropertyBeanArray";
	public static final String HABITAT4J_PROPERTY_BEAN_ARRAY_ITEM_SETTER_METHOD_PREFIX		= "setPropertyListContextPropertyBeanArrayItem";

	public static final String HABITAT4J_PROPERTY_BEAN_HASH_SETTER_METHOD_PREFIX			= "setPropertyListContextPropertyBeanHash";
	public static final String HABITAT4J_PROPERTY_BEAN_HASH_ITEM_SETTER_METHOD_PREFIX		= "setPropertyListContextPropertyBeanHashItem";

	public static final String HABITAT4J_LOG4J_LOGMANAGER_CLASS								= "org.apache.log4j.LogManager";
	public static final String HABITAT4J_LOG4J_LOGMANAGER_GETLOGGER_METHOD					= "getLogger";
	public static final String HABITAT4J_LOG4J_LOGGER										= "org.productivity.java.habitat4j";
	
	public static final String EOL_SEQUENCE													= "\n";
	
	public static final String SERVER_IDENTITY_MODE_JVM_ENVIRONMENT							= "server-identity.application.environment";
	public static final String SERVER_IDENTITY_MODE_JVM_INSTANCE_NAME						= "server-identity.application.instance.name";
	public static final String SERVER_IDENTITY_MODE_JVM_INSTANCE_ENUMERATION				= "server-identity.application.instance.enumeration";
	public static final String SERVER_IDENTITY_MODE_JVM_INSTANCE_DESCRIPTION				= "server-identity.application.instance.description";
	
	public static final String SERVER_IDENTITY_MODE_FILE									= "F";
	public static final String SERVER_IDENTITY_MODE_JVM										= "J";
	public static final String SERVER_IDENTITY_MODE_NULL									= "N";
	
	public static final String SERVER_IDENTITY_INSTANCE_OS_WINDOWS							= "windows";
	public static final String SERVER_IDENTITY_INSTANCE_OS_UNIX								= "unix";
	public static final String SERVER_IDENTITY_INSTANCE_OS_OTHER							= "other";
	
	public static final String HABITAT4J_HIDDEN_FIELD_DECODED								= "##ENCODED##";
	public static final String HABITAT4J_HIDDEN_FIELD_DECRYPTED								= "##ENCRYPTED##";
	public static final String HABITAT4J_HIDDEN_FIELD_DECODED_DECRYPTED						= "##ENCODED/ENCRYPTED##";

	public static final int HABITAT4J_PROPERTY_LIST_TO_STRING_ITEMS_PER_LINE_DEFAULT		= 5;
	
	public static final String SYSTEM_PROPERTY_OS_NAME										= "os.name";
}
