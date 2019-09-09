package org.productivity.java.habitat4j.common;

import org.productivity.java.habitat4j.PropertyListManager;
import org.productivity.java.habitat4j.common.exception.ApplicationPropertyListLoaderException;
import org.productivity.java.habitat4j.common.exception.PropertyListHandlerException;
import org.productivity.java.habitat4j.common.interfaces.ApplicationPropertyListLoaderIF;

/**
 * This ApplicationPropertyListLoaderIF implementation provides a
 * way for an application to load a set of PropertyList files.
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
 * @version $Id: ApplicationPropertyListLoader.java,v 1.5 2007/02/25 04:23:10 cvs Exp $
 */
public class ApplicationPropertyListLoader implements Habitat4JConstants, ApplicationPropertyListLoaderIF {	
	protected static final String BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RELOAD_CONFIGURATION	= "fileSystemPropertyListReloadConfiguration";

	protected static final String BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RELOAD_PROPERTY_LISTS	= "fileSystemPropertyListReloadPropertyLists";

	protected static final String BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RESOURCE_LOCATION		= "fileSystemPropertyListResourceLocation";
	protected static final String BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RESOURCES				= "fileSystemPropertyListResources";

	protected static final String BOOTSTRAP_PROPERTY_NAME_CLASS_PATH_PROPERTY_LIST_RESOURCE_LOCATION		= "classPathPropertyListResourceLocation";
	protected static final String BOOTSTRAP_PROPERTY_NAME_CLASS_PATH_PROPERTY_LIST_RESOURCES				= "classPathPropertyListResources";
	
	protected Habitat4JLogger log = null;
	
	protected static final String SYSTEM_PROPERTY_FILE_SEPARATOR	= "file.separator";
	protected static final String SYSTEM_PROPERTY_USER_DIR			= "user.dir";
	
	protected static final String DEFAULT_FILE_SEPARATOR			= "/";
	protected static final String CLASS_PACKAGE_SEPARATOR			= "/";
	protected String fileSeparator									= DEFAULT_FILE_SEPARATOR;
	
	protected static final int SEPARATED_ITEM_DIR					= 0;
	protected static final int SEPARATED_ITEM_FILE					= 1;	
	
	/**
	 * 
	 */
	public ApplicationPropertyListLoader() {
		log = Habitat4JLogger.getInstance();
		
		String _fileSeparator = System.getProperty(SYSTEM_PROPERTY_FILE_SEPARATOR);
		if (_fileSeparator != null && !_fileSeparator.trim().equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			fileSeparator = _fileSeparator;
		}
	}

	/**
	 * 
	 */
	protected void configureFileSystemPropertyListReload() {
		ApplicationPropertyListReloadConfiguration aplrc = 
			(ApplicationPropertyListReloadConfiguration) PropertyListManager.getPropertyBean(BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RELOAD_CONFIGURATION);
		
		if (aplrc != null) {
			try {
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD,aplrc.isEnabled());
	
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_ON_THE_FLY,aplrc.isOnTheFly());
				PropertyListManager.setFeature(Habitat4JFeatures.PROPERTY_LIST_FEATURE_DATE_RELOAD_INTERVAL,aplrc.getInterval());

			} catch (PropertyListHandlerException plhe) {
				log.debug(plhe.toString());
				return;
			}
		} else {
			log.info("No ApplicationPropertyListReloadConfiguration PropertyBean found; skipping configureFileSystemPropertyListReload()");
		}
		
		String[] aplrpl = 
			PropertyListManager.getPropertyArray(BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RELOAD_PROPERTY_LISTS);
		
		if (aplrpl != null) {
			PropertyListManager.clearReloadablePropertyLists();
			
			for (int i=0; i<aplrpl.length; i++) {
				PropertyListManager.addReloadablePropertyList(aplrpl[i]);
			}
		}
	}
	
	/**
	 * @param item
	 * @param separator
	 * @return Returns a two-entry String array that contains a directory and file separately.
	 */
	protected String[] getSeparatedItem(String item, String separator) {
		if (item == null) {
			return new String[] { null, null };	
		}
		
		int fsIndex = item.lastIndexOf(separator);
		
		if (fsIndex == -1 || fsIndex == item.length()) {
			return new String[] { null, item };		
		}
		
		return new String[] { item.substring(0,fsIndex), item.substring(fsIndex + 1, item.length()) };
	}

	protected String getPropertyPath(String[] item, ApplicationPropertyListResourceLocation plrl, String separator) {
		StringBuffer buffer = new StringBuffer();

		if (item != null && (item[SEPARATED_ITEM_DIR] == null || item[SEPARATED_ITEM_DIR].equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING) || !item[SEPARATED_ITEM_DIR].startsWith(separator))) {
			if (plrl.isUseUserDirPath()) {
				String userDirPath = System.getProperty(SYSTEM_PROPERTY_USER_DIR);
				
				buffer.append(userDirPath);
				buffer.append(separator);
			}
			
			String basePath = plrl.getBasePath();
			if (basePath != null && !basePath.equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
				buffer.append(basePath);
				if (!basePath.equals(separator)) {
					buffer.append(separator);
				}
			}
		}
		
		if (item[SEPARATED_ITEM_DIR] != null && !item[SEPARATED_ITEM_DIR].equals(HABITAT4J_GENERIC_VALUE_EMPTY_STRING)) {
			buffer.append(item[SEPARATED_ITEM_DIR]);
			buffer.append(separator);
		}
		
		buffer.append(plrl.getPrefix());
		buffer.append(item[SEPARATED_ITEM_FILE]);
		buffer.append(plrl.getSuffix());
		
		return buffer.toString();
	}
	
	/**
	 * @throws PropertyListHandlerException
	 */
	protected void loadFileSystemPropertyLists() throws PropertyListHandlerException {
		ApplicationPropertyListResourceLocation plrl = (ApplicationPropertyListResourceLocation) PropertyListManager.getPropertyBean(BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RESOURCE_LOCATION);		
		if (plrl == null) {
			log.info("No ApplicationPropertyListResourceLocation PropertyBean found; skipping loadFileSystemProperties()");
			return;
		}

		String[] list = PropertyListManager.getPropertyArray(BOOTSTRAP_PROPERTY_NAME_FILE_SYSTEM_PROPERTY_LIST_RESOURCES);
		if (list == null || list.length < 1) {
			log.info("No PropertyArray found containing a list of PropertyList files to load; skipping loadFileSystemProperties()");
			return;
		}

		for(int i=0; i<list.length; i++) {
			String[] item = getSeparatedItem(list[i],fileSeparator);
			
			PropertyListManager.loadPropertyListFromFile(
					item[SEPARATED_ITEM_FILE],
					getPropertyPath(item,plrl,fileSeparator)
			);
		}
	}
	
	/**
	 * @throws PropertyListHandlerException
	 */
	protected void loadClassPathPropertyLists() throws PropertyListHandlerException {
		ApplicationPropertyListResourceLocation plrl = (ApplicationPropertyListResourceLocation) PropertyListManager.getPropertyBean(BOOTSTRAP_PROPERTY_NAME_CLASS_PATH_PROPERTY_LIST_RESOURCE_LOCATION);		
		if (plrl == null) {
			log.info("No ApplicationPropertyListResourceLocation PropertyBean found; skipping loadClassPathProperties()");
			return;
		}

		String[] list = PropertyListManager.getPropertyArray(BOOTSTRAP_PROPERTY_NAME_CLASS_PATH_PROPERTY_LIST_RESOURCES);
		if (list == null || list.length < 1) {
			log.info("No PropertyArray found containing a list of PropertyList files to load; skipping loadClassPathProperties()");
			return;
		}

		for(int i=0; i<list.length; i++) {
			String[] item = getSeparatedItem(list[i],CLASS_PACKAGE_SEPARATOR);

			PropertyListManager.loadPropertyListFromResource(
					item[SEPARATED_ITEM_FILE],
					getPropertyPath(item,plrl,CLASS_PACKAGE_SEPARATOR)
			);
		}		
	}
		
	/* (non-Javadoc)
	 * @see org.productivity.java.habitat4j.common.interfaces.ApplicationPropertyListLoaderIF#load(java.lang.String)
	 */
	public void load(String applicationName) throws ApplicationPropertyListLoaderException {
		try {
			configureFileSystemPropertyListReload();
			loadFileSystemPropertyLists();
			loadClassPathPropertyLists();
			
		} catch (PropertyListHandlerException plhe) {
			throw new ApplicationPropertyListLoaderException(plhe);
		}
	}
	
/*
 	public static void main(String[] args) {
		ApplicationPropertyListLoader apll = new ApplicationPropertyListLoader();
		ApplicationPropertyListResourceLocation plrl = new ApplicationPropertyListResourceLocation();
		String[] item = null;
		
		plrl.setBasePath(CLASS_PACKAGE_SEPARATOR);
		item = apll.getSeparatedItem("b/c","/");
		String filePath = apll.getFileSystemPropertyPath(item,plrl,"/");
		System.out.println("FilePath = " + filePath);
	}
*/
}
