package org.productivity.java.habitat4j.compat;

import org.productivity.java.habitat4j.compat.common.AbstractXMLMap;

/**
 * ServerIdentityXMLMap is used for mapping foreign schemas to the Habitat4J schema.
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
 * @version $Id: ServerIdentityXMLMap.java,v 1.3 2004/11/10 08:00:51 cvs Exp $
 */
public class ServerIdentityXMLMap extends AbstractXMLMap {
	public final static String SERVER_IDENTITY_ELEMENT										= "server-identity";
	public final static String SERVER_IDENTITY_ELEMENT_VERSION_ATTRIBUTE					= "version";
	public final static String SERVER_IDENTITY_ELEMENT_MODIFIEDBY_ATTRIBUTE					= "modifiedBy";
	
	public final static String SERVER_IDENTITY_APPLICATION_ELEMENT							= "server-identity/application";
	public final static String SERVER_IDENTITY_APPLICATION_ELEMENT_NAME_ATTRIBUTE			= "name";

	public final static String SERVER_IDENTITY_APPLICATION_ENVIRONMENT_ELEMENT				= "server-identity/application/environment";

	public final static String SERVER_IDENTITY_APPLICATION_INSTANCE_NAME_ELEMENT			= "server-identity/application/instance/name";
	public final static String SERVER_IDENTITY_APPLICATION_INSTANCE_DESCRIPTION_ELEMENT		= "server-identity/application/instance/description";
	public final static String SERVER_IDENTITY_APPLICATION_INSTANCE_ENUMERATION_ELEMENT		= "server-identity/application/instance/enumeration";
	
	public final static String SERVER_IDENTITY_APPLICATION_ROLES_ELEMENT					= "server-identity/application/roles";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_ELEMENT						= "server-identity/application/role";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_NAME_ATTRIBUTE				= "name";
	
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_TYPE_ELEMENT				= "server-identity/application/role/type";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_VENDOR_ELEMENT				= "server-identity/application/role/vendor";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_PRODUCT_ELEMENT				= "server-identity/application/role/product";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_VERSION_ELEMENT				= "server-identity/application/role/version";
	public final static String SERVER_IDENTITY_APPLICATION_ROLE_PATCHLEVEL_ELEMENT			= "server-identity/application/role/patchLevel";

	public final static String SERVER_IDENTITY_APPLICATION_PRAGMA_ELEMENT					= "server-identity/application/pragma";
	public final static String SERVER_IDENTITY_APPLICATION_PRAGMA_ELEMENT_NAME_ATTRIBUTE	= "name";

	// These is an alternate for using a "value" attribute to store the value in lieu of the body of the "pragma" element
	public final static String SERVER_IDENTITY_APPLICATION_PRAGMA_ELEMENT_VALUE_ATTRIBUTE	= "value";

	// These are alternates for the attribute-based "pragma" element, storing the "name" and "value" into separate elements
	public final static String SERVER_IDENTITY_APPLICATION_PRAGMA_NAME_ELEMENT				= "server-identity/application/pragma/name";
	public final static String SERVER_IDENTITY_APPLICATION_PRAGMA_VALUE_ELEMENT				= "server-identity/application/pragma/value";
}
