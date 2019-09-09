package org.productivity.java.habitat4j.common;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Habitat4JFeatures provides static variables for several options available
 * within ServerIdentity and PropertyList. 
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
 * @version $Id: Habitat4JFeatures.java,v 1.32 2007/02/25 18:58:59 cvs Exp $
 */
public final class Habitat4JFeatures implements Habitat4JConstants {
	public static final String SERVER_IDENTITY_FEATURE_MODE								= "featureServerIdentityMode"; 

	public static final String SERVER_IDENTITY_FEATURE_XSD_VALIDATION					= "featureServerIdentityXSDValidation";
	
	public static final String SERVER_IDENTITY_FEATURE_LAX_ROLENAMES					= "featureServerIdentityLaxRoleNames";

	public static final String SERVER_IDENTITY_FEATURE_CAN_BE_RELOADED					= "featureServerIdentityCanBeReloaded";

	public static final String PROPERTY_LIST_FEATURE_XSD_VALIDATION						= "featurePropertyListXSDValidation";

	public static final String PROPERTY_LIST_FEATURE_DATE_RELOAD						= "featurePropertyListDateReload";
	public static final String PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY				= "featurePropertyListDateReloadOnTheFly";
	public static final String PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL				= "featurePropertyListDateReloadInterval";
	public static final String PROPERTY_LIST_FEATURE_DATE_RELOAD_DISABLE_HANDLER_TRANSFER = "featurePropertyListReloadDisableHandlerTransfer";
	
	public static final String PROPERTY_LIST_FEATURE_SYSTEM_PROPERTY_OVERRIDE			= "featurePropertyListSystemPropertyOverride";
	public static final String PROPERTY_LIST_FEATURE_SECURITY_PROPERTY_OVERRIDE			= "featurePropertyListSecurityPropertyOverride";

	public static final String PROPERTY_LIST_FEATURE_SERVER_IDENTITY_PRAGMA_OVERRIDE	= "featurePropertyListServerIdentityPragmaOverride";

	public static final String PROPERTY_LIST_FEATURE_CASE_SENSITIVE_CONTEXT_MATCHING	= "featurePropertyListCaseSensitiveContextMatching";
	public static final String PROPERTY_LIST_FEATURE_DISABLE_WILDCARD_CONTEXT_MATCHING	= "featurePropertyListDisableWildcardContextMatching";
	
	public static final String PROPERTY_LIST_FEATURE_PROPERTY_BEAN_STRING_OBJECT_COPY	= "featurePropertyListStringObjectCopy";
	public static final String PROPERTY_LIST_FEATURE_PROPERTY_BEAN_WRAPPER_OBJECT_COPY	= "featurePropertyListWrapperObjectCopy";
	public static final String PROPERTY_LIST_FEATURE_PROPERTY_BEAN_CLONEABLE_OBJECT_COPY	= "featurePropertyListCloneableObjectCopy";
	public static final String PROPERTY_LIST_FEATURE_SUPPRESS_PROPERTY_BEAN_COPY_WARNINGS	= "featurePropertyListSuppressPropertyBeanCopyWarning";
	
	public static final String PROPERTY_LIST_FEATURE_TO_STRING_ITEMS_PER_LINE			= "featurePropertyListToStringItemsPerLine";

	private static final String[] SERVER_IDENTITY_FEATURES_LIST = {
			SERVER_IDENTITY_FEATURE_MODE,
			
			SERVER_IDENTITY_FEATURE_XSD_VALIDATION,
			SERVER_IDENTITY_FEATURE_LAX_ROLENAMES
	};
	
	private static final String[] PROPERTY_LIST_FEATURES_LIST = {
			PROPERTY_LIST_FEATURE_XSD_VALIDATION,

			PROPERTY_LIST_FEATURE_DATE_RELOAD,
			PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY,
			PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL,
			PROPERTY_LIST_FEATURE_DATE_RELOAD_DISABLE_HANDLER_TRANSFER,

			PROPERTY_LIST_FEATURE_SYSTEM_PROPERTY_OVERRIDE,
			PROPERTY_LIST_FEATURE_SECURITY_PROPERTY_OVERRIDE,

			PROPERTY_LIST_FEATURE_SERVER_IDENTITY_PRAGMA_OVERRIDE,

			PROPERTY_LIST_FEATURE_CASE_SENSITIVE_CONTEXT_MATCHING,
			PROPERTY_LIST_FEATURE_DISABLE_WILDCARD_CONTEXT_MATCHING,
			
			PROPERTY_LIST_FEATURE_PROPERTY_BEAN_STRING_OBJECT_COPY,
			PROPERTY_LIST_FEATURE_PROPERTY_BEAN_WRAPPER_OBJECT_COPY,
			PROPERTY_LIST_FEATURE_PROPERTY_BEAN_CLONEABLE_OBJECT_COPY,
			PROPERTY_LIST_FEATURE_SUPPRESS_PROPERTY_BEAN_COPY_WARNINGS,
			
			PROPERTY_LIST_FEATURE_TO_STRING_ITEMS_PER_LINE
	};
	
	public static HashSet SERVER_IDENTITY_FEATURES = new HashSet();
	public static HashSet PROPERTY_LIST_FEATURES = new HashSet();
	
	/**
	 * This static initializer creates convenient Hashmap instances in order
	 * to quickly look up allowed features.
	 */
	static {
		int i;
		for (i=0; i<SERVER_IDENTITY_FEATURES_LIST.length; i++) {
			SERVER_IDENTITY_FEATURES.add(SERVER_IDENTITY_FEATURES_LIST[i]);
		}
		for (i=0; i<PROPERTY_LIST_FEATURES_LIST.length; i++) {
			PROPERTY_LIST_FEATURES.add(PROPERTY_LIST_FEATURES_LIST[i]);
		}
	}

	/**
	 * Tbis static method provides a convenient way to check whether a
	 * feature resolves to true.  Developers can use "true," "on," or "1" for
	 * a feature to resolve to true.  Anything else resolves to false,
	 * including a feature not being set at all.  If the status of a feature
	 * being set is needed, isFeatureSet(...) should be used.
	 * 
	 * @param hashtable
	 * @param name
	 * @return Returns whether the feature is set to a 'true' value.
	 */
	public static boolean isFeatureTrue(Hashtable hashtable, String name) {
		String value = (String) hashtable.get(name);
		
		if (value != null && (value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_TRUE) || value.equalsIgnoreCase(HABITAT4J_GENERIC_VALUE_ON) || value.equals(HABITAT4J_GENERIC_VALUE_ONE) || value.equals(HABITAT4J_GENERIC_VALUE_YES))) {
			return true;
		}

		return false;
	}

	/**
	 * This static method provides a convenient way to check whether a
	 * feature has been set.
	 * 
	 * @param hashtable
	 * @param name
	 * @return Returns whether a feature has been set.
	 */
	public static boolean isFeatureSet(Hashtable hashtable, String name) {
		if (hashtable == null) {
			return false;
		}
		
		return hashtable.containsKey(name);
	}

	/**
	 * This static method provides a convenient way to return the integer
	 * value of a feature.  If the feature isn't set or doesn't contain
	 * an appropriate integer value, this method will return a value of -1.
	 * 
	 * @param hashtable
	 * @param name
	 * @return Returns a feature's integer value.
	 */
	public static int getFeatureInt(Hashtable hashtable, String name) {
		if (hashtable == null) {
			return -1;
		}
		
		String value = (String) hashtable.get(name);
		
		if (value == null) {
			return -1;
		}
		
		try {
			return new Integer(value).intValue();
			
		} catch (NumberFormatException nfe) {
			return -1;
		}
	}
}
