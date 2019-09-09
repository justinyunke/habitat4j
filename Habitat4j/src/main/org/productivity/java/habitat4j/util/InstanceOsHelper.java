package org.productivity.java.habitat4j.util;

import java.util.StringTokenizer;

import org.productivity.java.habitat4j.common.Habitat4JConstants;

/**
 * Utility used to determine the "simplified" operating system, i.e.
 * "windows", "unix", or "other".
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
 * @version $Id: InstanceOsHelper.java,v 1.1 2006/08/16 13:33:36 cvs Exp $
 */
public class InstanceOsHelper {
	public static final String[] UNIX_OS_LIST = {
		"Linux",
		"Solaris",
		"AIX",
		"Digital Unix",
		"FreeBSD",
		"HP-UX",
		"Irix",
		"Mac OS"
	};
	
	public static final String[] WINDOWS_OS_LIST = {
		"Windows"
	};
	
	protected static boolean matchList(String osName, String entries) {
		String lowerOsName = osName.toLowerCase();

		StringTokenizer tokenizer = new StringTokenizer(entries,",");
		
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			
			if (lowerOsName.startsWith(token.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}

	protected static boolean matchDefault(String osName, String[] entries) {
		String lowerOsName = osName.toLowerCase();
		
		for (int i=0; i<entries.length; i++) {
			if (lowerOsName.startsWith(entries[i].toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}
	
	protected static boolean match(String osName, String listSysPropName, String[] entries) {
		boolean match = false;
		
		String list = System.getProperty(listSysPropName);
		
		if (list != null) {
			match = matchList(osName, list);
			
			return match;
		}
		
		match = matchDefault(osName,entries);
		
		return match;
	}

	public static String getInstanceOS() {
		String osName = System.getProperty(Habitat4JConstants.SYSTEM_PROPERTY_OS_NAME);
		
		boolean match = match(osName,Habitat4JConstants.HABITAT4J_WINDOWS_OS_NAME_LIST_PROPERTY_NAME,WINDOWS_OS_LIST);
		
		if (match) {
			return Habitat4JConstants.SERVER_IDENTITY_INSTANCE_OS_WINDOWS;
		}
		
		match = match(osName,Habitat4JConstants.HABITAT4J_UNIX_OS_NAME_LIST_PROPERTY_NAME,UNIX_OS_LIST);
		
		if (match) {
			return Habitat4JConstants.SERVER_IDENTITY_INSTANCE_OS_UNIX;
		}

		return Habitat4JConstants.SERVER_IDENTITY_INSTANCE_OS_OTHER;
	}
}
