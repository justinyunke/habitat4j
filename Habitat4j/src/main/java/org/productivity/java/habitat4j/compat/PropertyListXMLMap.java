package org.productivity.java.habitat4j.compat;

import org.productivity.java.habitat4j.compat.common.AbstractXMLMap;

/**
 * PropertyListXMLMap is used for mapping foreign schemas to the Habitat4J schema.
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
 * @version $Id: PropertyListXMLMap.java,v 1.3 2004/11/10 08:00:51 cvs Exp $
 */
public class PropertyListXMLMap extends AbstractXMLMap {
	public final static String PROPERTY_LIST_ELEMENT													= "property-list";
	public final static String PROPERTY_LIST_ELEMENT_NAME_ATTRIBUTE										= "name";
	public final static String PROPERTY_LIST_ELEMENT_VERSION_ATTRIBUTE									= "version";
	public final static String PROPERTY_LIST_ELEMENT_MODIFIEDBY_ATTRIBUTE								= "modifiedBy";
	public final static String PROPERTY_LIST_ELEMENT_RELOADSERIAL_ATTRIBUTE								= "reloadSerial";

	public final static String PROPERTY_LIST_DEFINITIONS_ELEMENT										= "property-list/definitions";
	public final static String PROPERTY_LIST_DEFINITIONS_ELEMENT_PACKAGE_ATTRIBUTE						= "package";

	public final static String PROPERTY_LIST_DEFINITIONS_PROPERTYBEAN_ELEMENT							= "property-list/definitions/property-bean";
	public final static String PROPERTY_LIST_DEFINITIONS_PROPERTYBEAN_ELEMENT_ID_ATTRIBUTE				= "id";
	public final static String PROPERTY_LIST_DEFINITIONS_PROPERTYBEAN_ELEMENT_CLASS_ATTRIBUTE			= "class";
	
	public final static String PROPERTY_LIST_DEFINITIONS_DECODER_ELEMENT								= "property-list/definitions/decoder";
	public final static String PROPERTY_LIST_DEFINITIONS_DECODER_ELEMENT_ID_ATTRIBUTE					= "id";
	public final static String PROPERTY_LIST_DEFINITIONS_DECODER_ELEMENT_CLASS_ATTRIBUTE				= "class";
	public final static String PROPERTY_LIST_DEFINITIONS_DECODER_ELEMENT_ALWAYSNEWINSTANCE_ATTRIBUTE	= "alwaysNewInstance";
	
	public final static String PROPERTY_LIST_DEFINITIONS_DECRYPTOR_ELEMENT								= "property-list/definitions/decryptor";
	public final static String PROPERTY_LIST_DEFINITIONS_DECRYPTOR_ELEMENT_ID_ATTRIBUTE					= "id";
	public final static String PROPERTY_LIST_DEFINITIONS_DECRYPTOR_ELEMENT_CLASS_ATTRIBUTE				= "class";
	public final static String PROPERTY_LIST_DEFINITIONS_DECRYPTOR_ELEMENT_ALWAYSNEWINSTANCE_ATTRIBUTE	= "alwaysNewInstance";
	
	public final static String PROPERTY_LIST_DEFINITIONS_RELOADEVENTHANDLER_ELEMENT						= "property-list/definitions/reload-event-handler";
	

	public final static String PROPERTY_LIST_CONTEXT_ELEMENT											= "property-list/context";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ENVIRONMENT_ATTRIBUTE						= "environment";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_INSTANCENAME_ATTRIBUTE						= "instanceName";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_INSTANCEDESCRIPTION_ATTRIBUTE				= "instanceDescription";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_INSTANCEENUMERATION_ATTRIBUTE				= "instanceEnumeration";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLE_ATTRIBUTE								= "role";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLETYPE_ATTRIBUTE							= "roleType";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLEVENDOR_ATTRIBUTE						= "roleVendor";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLEPRODUCT_ATTRIBUTE						= "roleProduct";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLEVERSION_ATTRIBUTE						= "roleVersion";
	public final static String PROPERTY_LIST_CONTEXT_ELEMENT_ROLEPATCHLEVEL_ATTRIBUTE					= "rolePatchLevel";
	
	public final static String PROPERTY_LIST_PROPERTY_ELEMENT											= "property-list/context/property";
	public final static String PROPERTY_LIST_PROPERTY_ELEMENT_NAME_ATTRIBUTE							= "name";
	public final static String PROPERTY_LIST_PROPERTY_ELEMENT_DECODERID_ATTRIBUTE						= "decoderId";
	public final static String PROPERTY_LIST_PROPERTY_ELEMENT_DECRYPTORID_ATTRIBUTE						= "decryptorId";

	// This is an alternate for the body-based "property" element value
	public final static String PROPERTY_LIST_PROPERTY_ELEMENT_VALUE_ATTRIBUTE							= "value";

	// These are alternates for the attribute-based "property" element
	public final static String PROPERTY_LIST_PROPERTY_NAME_ELEMENT										= "property-list/context/property/name";
	public final static String PROPERTY_LIST_PROPERTY_VALUE_ELEMENT										= "property-list/context/property/value";

	public final static String PROPERTY_LIST_SYSTEMPROPERTY_ELEMENT										= "property-list/context/system-property";
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_ELEMENT_NAME_ATTRIBUTE						= "name";
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_ELEMENT_DECODERID_ATTRIBUTE					= "decoderId";
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_ELEMENT_DECRYPTORID_ATTRIBUTE				= "decryptorId";

	// This is an alternate for the body-based "property" element value
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_ELEMENT_VALUE_ATTRIBUTE						= "value";

	// These are alternates for the attribute-based "property" element
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_NAME_ELEMENT								= "property-list/context/system-property/name";
	public final static String PROPERTY_LIST_SYSTEMPROPERTY_VALUE_ELEMENT								= "property-list/context/system-property/value";

	public final static String PROPERTY_LIST_SECURITYPROPERTY_ELEMENT									= "property-list/context/security-property";
	public final static String PROPERTY_LIST_SECURITYPROPERTY_ELEMENT_NAME_ATTRIBUTE					= "name";
	public final static String PROPERTY_LIST_SECURITYPROPERTY_ELEMENT_DECODERID_ATTRIBUTE				= "decoderId";
	public final static String PROPERTY_LIST_SECURITYPROPERTY_ELEMENT_DECRYPTORID_ATTRIBUTE				= "decryptorId";

	// This is an alternate for the body-based "property" element value
	public final static String PROPERTY_LIST_SECURITYPROPERTY_ELEMENT_VALUE_ATTRIBUTE					= "value";

	// These are alternates for the attribute-based "property" element
	public final static String PROPERTY_LIST_SECURITYPROPERTY_NAME_ELEMENT								= "property-list/context/security-property/name";
	public final static String PROPERTY_LIST_SECURITYPROPERTY_VALUE_ELEMENT								= "property-list/context/security-property/value";

	public final static String PROPERTY_LIST_PROPERTYARRAY_ELEMENT										= "property-list/context/property-array";
	public final static String PROPERTY_LIST_PROPERTYARRAY_NAME_ATTRIBUTE								= "name";
	
	public final static String PROPERTY_LIST_PROPERTYARRAY_ITEM_ELEMENT									= "property-list/context/property-array/item";
	public final static String PROPERTY_LIST_PROPERTYARRAY_ITEM_ELEMENT_DECODERID_ATTRIBUTE				= "decoderId";
	public final static String PROPERTY_LIST_PROPERTYARRAY_ITEM_ELEMENT_DECRYPTORID_ATTRIBUTE			= "decryptorId";
	public final static String PROPERTY_LIST_PROPERTYARRAY_ITEM_ELEMENT_INSTANCE_ATTRIBUTE				= "instance";

	public final static String PROPERTY_LIST_PROPERTYBEAN_ELEMENT										= "property-list/context/property-bean";
	public final static String PROPERTY_LIST_PROPERTYBEAN_NAME_ATTRIBUTE								= "name";
	public final static String PROPERTY_LIST_PROPERTYBEAN_ID_ATTRIBUTE									= "id";
	public final static String PROPERTY_LIST_PROPERTYBEAN_FIELD_ELEMENT_DECODERID_ATTRIBUTE				= "decoderId";
	public final static String PROPERTY_LIST_PROPERTYBEAN_FIELD_ELEMENT_DECRYPTORID_ATTRIBUTE			= "decryptorId";
	public final static String PROPERTY_LIST_PROPERTYBEAN_FIELD_ELEMENT_INSTANCE_ATTRIBUTE				= "instance";
	
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ELEMENT									= "property-list/context/property-bean-array";
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ELEMENT_NAME_ATTRIBUTE					= "name";
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ELEMENT_ID_ATTRIBUTE						= "id";
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ELEMENT_INSTANCE_ATTRIBUTE				= "instance";
	
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ITEM_ELEMENT								= "property-list/context/property-bean-array/item";
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ITEM_FIELD_ELEMENT_DECODERID_ATTRIBUTE	= "decoderId";
	public final static String PROPERTY_LIST_PROPERTYBEANARRAY_ITEM_FIELD_ELEMENT_DECRYPTORID_ATTRIBUTE	= "decryptorId";	
}
