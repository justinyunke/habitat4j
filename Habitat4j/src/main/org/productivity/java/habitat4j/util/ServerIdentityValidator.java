package org.productivity.java.habitat4j.util;

import org.productivity.java.habitat4j.ServerIdentityManager;
import org.productivity.java.habitat4j.common.Habitat4JConstants;
import org.productivity.java.habitat4j.common.Habitat4JFeatures;
import org.productivity.java.habitat4j.common.exception.ServerIdentityHandlerException;

/**
 * Command-line utility used for validating ServerIdentity files.
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
 * @version $Id: ServerIdentityValidator.java,v 1.1 2004/07/28 08:07:47 cvs Exp $
 */
public class ServerIdentityValidator implements Habitat4JConstants {
	private static final String APP_NAME		= "ServerIdentityValidatorApp";
	
	private static final String MESSAGE_FAIL	= "!! SERVERIDENTITY XML FILE FAILED TO LOAD :( !!";
	private static final String MESSAGE_SUCCESS	= "** SERVERIDENTITY XML FILE SUCCESSFULLY LOADED :) **";
	private static final String MESSAGE_USAGE	= "Usage: ServerIdentityValidator <file>";
	
	private static void logInfo(String error) {
		System.out.println(error);
	}

	private static void logFatal(String error) {
		System.err.println(error);
	}
	
	private static void performValidation(String filePath) {
		ServerIdentityManager.initialize(APP_NAME);
		
		// Set the location of the server-identity.xml file
		System.setProperty(
			Habitat4JConstants.HABITAT4J_SERVER_IDENTITY_FILE_PATH_SYSTEM_PROPERTY_NAME,
			filePath
		);		

		try {
			// Turn on XSD validation
			ServerIdentityManager.setFeature(Habitat4JFeatures.SERVER_IDENTITY_FEATURE_XSD_VALIDATION,true);

			// Attempt load
			ServerIdentityManager.loadServerIdentity();
			
		} catch (ServerIdentityHandlerException sihe) {
			logFatal(MESSAGE_FAIL);
			logFatal("");
			logFatal(sihe.toString());
			sihe.printStackTrace();
			
			return;
		}
		
		logInfo(MESSAGE_SUCCESS);
		logInfo("");
		logInfo(ServerIdentityManager.toDisplayString());
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			logFatal(MESSAGE_FAIL);
			logFatal("");
			logFatal(MESSAGE_USAGE);
			return;
		}
		
		performValidation(args[0]);
	}
}
