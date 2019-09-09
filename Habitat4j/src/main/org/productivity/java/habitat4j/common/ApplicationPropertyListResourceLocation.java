package org.productivity.java.habitat4j.common;

/**
 * This PropertyBean provides filepath/classpath information
 * for Habitat4JBootstrapServlet and ApplicationPropertyListLoader
 * classes.
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
 * @version $Id: ApplicationPropertyListResourceLocation.java,v 1.6 2005/09/21 07:13:37 cvs Exp $
 */
public class ApplicationPropertyListResourceLocation implements Habitat4JConstants {
	protected static final String DEFAULT_BASE_PATH				= HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
	protected static final String DEFAULT_PREFIX				= "properties-";
	protected static final String DEFAULT_SUFFIX				= ".xml";
	protected static final boolean DEFAULT_USE_USER_DIR_PATH	= false;
	
	protected boolean useUserDirPath	= DEFAULT_USE_USER_DIR_PATH; 
	protected String basePath			= DEFAULT_BASE_PATH;
	protected String prefix				= DEFAULT_PREFIX;
	protected String suffix				= DEFAULT_SUFFIX;
	
	/**
	 * @return Returns the useUserDirPath.
	 */
	public boolean isUseUserDirPath() {
		return useUserDirPath;
	}
	/**
	 * @param useUserDirPath The useUserDirPath to set.
	 */
	public void setUseUserDirPath(boolean useUserDirPath) {
		this.useUserDirPath = useUserDirPath;
	}
	/**
	 * @return Returns the basePath.
	 */
	public String getBasePath() {
		return basePath;
	}
	/**
	 * @param basePath The basePath to set.
	 */
	public void setBasePath(String basePath) {
		// Remove any trailing delimiters.
		if (basePath != null && basePath.length() > 1) {
			char lastChar = basePath.charAt(basePath.length()-1);
			
			if (lastChar == '/' || lastChar == '\\') {
				this.basePath = basePath.substring(0,basePath.length()-1);
				return;
			}
		}
		
		this.basePath = basePath;
	}
	/**
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		if (prefix == null) {
			return HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
		}
		
		return prefix;
	}
	/**
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return Returns the suffix.
	 */
	public String getSuffix() {
		if (suffix == null) {
			return HABITAT4J_GENERIC_VALUE_EMPTY_STRING;
		}
		
		return suffix;
	}
	/**
	 * @param suffix The suffix to set.
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
