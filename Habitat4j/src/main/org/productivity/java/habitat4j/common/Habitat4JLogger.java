package org.productivity.java.habitat4j.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * The Habitat4JLogger Singleton class wraps a simple System.out/System.err logger
 * as well as log4j, and allows the developer/end user to switch between them as necessary.
 * No log4j classes are used at compile time; reflection is used to load the classes
 * dynamically at runtime.
 * 
 * <p>By default, Habitat4J logs to System.out/System.err.  To change this to
 * log4j:</p>
 * 
 * <pre>
 *   Habitat4JLogger logger = Habitat4JLogger.getInstance();
 *   logger.selectLog4j();
 * </pre>
 * 
 * <p>To switch back:</p>
 *
 * <pre> 
 *   Habitat4JLogger logger = Habitat4JLogger.getInstance();
 *   logger.selectSystem();
 * </pre>
 * 
 * <p>When using Habitat4JLogger in System.out/System.err mode, the default
 * logging level is INFO.  To change this to DEBUG, for instance, use:</p>
 * 
 * <pre>
 *   logger.setLevel(DEBUG):
 * </pre>
 *
 * <p>or:</p>
 *
 * <pre>
 *   logger.setLevel("debug"):
 * </pre>
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
 * @version $Id: Habitat4JLogger.java,v 1.3 2004/11/11 07:05:18 cvs Exp $
 */
public class Habitat4JLogger implements Habitat4JConstants {
	// Singleton instance
	private static Habitat4JLogger instance = null;
	
	// Habitat4JLogger modes
	protected static final byte MODE_SYSTEM = 0;
	protected static final byte MODE_LOG4J = 1;

	// Habitat4JLogger levels
	public static final byte DEBUG = 0;
	public static final byte INFO = 1;
	public static final byte WARN = 2;
	public static final byte ERROR = 3;
	public static final byte FATAL = 4;
	protected static final byte DEFAULT_LEVEL = INFO;
	
	// Text to display in System.out/System.err mode for above byte levels
	protected static final String[] LevelMethodNames = {
			"debug","info","warn","error","fatal"
	};
	protected static final String[] LevelLogText = {
			"[DEBUG]","[INFO] ","[WARN] ","[ERROR]","[FATAL]"
	};
	
	// Default error message indicating a default in case of problems with log4j
	private static final String DEFAULT_TO_SYSTEM_TEXT = "defaulting to System.out/System.err logging";
	
	// Log4j Object (org.apache.log4j.Logger) and its five Methods (debug,info,warn,error,fatal)
	private Object log4jLogger = null;
	private Method log4jLogMethod[] = new Method[LevelMethodNames.length];
	
	// Habitat4JLogger configuration variables
	private byte mode = MODE_SYSTEM;
	private byte level = DEFAULT_LEVEL;	
	private byte systemErrLevel = WARN;
		
	private Habitat4JLogger() {
		// Singleton
	}
	
	public static Habitat4JLogger getInstance() {
		if (instance == null) {
			instance = new Habitat4JLogger();
		}
		
		return instance;
	}
	
	/**
	 * This method selects the log4j mode of logging within Habitat4J.
	 */
	public void selectLog4j() {
		if (log4jLogger == null) {
			try {
				// Grab the org.apache.log4j.LogManager static class
				Class logManager = Habitat4JLogger.class.getClassLoader().loadClass(HABITAT4J_LOG4J_LOGMANAGER_CLASS);
				
				// Find the getLogger() method
				Class getLoggerParmClasses[] = { String.class }; 
				Method log4jGetLogger = logManager.getMethod(HABITAT4J_LOG4J_LOGMANAGER_GETLOGGER_METHOD,getLoggerParmClasses);
				
				// Invoke getLogger() to get an instance of org.apache.log4j.Logger
				Object getLoggerParms[] = { HABITAT4J_LOG4J_LOGGER };
				log4jLogger = log4jGetLogger.invoke(logManager,getLoggerParms);
				
				// The five methods (debug,info,warn,error,fatal) all take one parameter of type Object.
				Class logParmClasses[] = {Object.class};
				
				// Cache all five methods (debug,info,warn,error,fatal)
				for (int i=0; i < LevelMethodNames.length; i++) {
					log4jLogMethod[i] = log4jLogger.getClass().getMethod(LevelMethodNames[i],logParmClasses);
				}
								
				// If runtime has gotten this far without exceptions, flip the mode to log4j.
				setMode(MODE_LOG4J); 
				
			// On any exceptions generated from the above, flip back to System.out/System.err mode
			// and log what happened
			} catch (InvocationTargetException ite) {
				selectSystem();
				warn(ite.toString() + ": " + DEFAULT_TO_SYSTEM_TEXT);
				
			} catch (IllegalAccessException iae) {
				selectSystem();
				warn(iae.toString() + ": " + DEFAULT_TO_SYSTEM_TEXT);

			} catch (NoSuchMethodException nsme) {
				selectSystem();
				warn(nsme.toString() + ": " + DEFAULT_TO_SYSTEM_TEXT);
				
			} catch (ClassNotFoundException cnfe) {
				selectSystem();
				warn(cnfe.toString() + ": " + DEFAULT_TO_SYSTEM_TEXT);
			}
		}
	}

	/**
	 * This method selects the System.out/System.err mode of logging within Habitat4J.
	 */
	public void selectSystem() {
		log4jLogger = null;
		setMode(MODE_SYSTEM); 		
	}
	
	/**
	 * This method returns the current timestamp.
	 * 
	 * @return Returns the current timestamp.
	 */
	private String getTimeStamp() {
		Date date = new Date();		
		
		return date.toString();		
	}

	/**
	 * @param level - the level to indicate in the log
	 * @param logText - the text to display in the log
	 * @return Returns a formatted line of log text.
	 */
	private String logSystem(byte level, String logText) {
		String levelText = LevelLogText[FATAL];
		if (level >= DEBUG && level <= FATAL)
			levelText = LevelLogText[level];
		
		StringBuffer buffer = new StringBuffer(getTimeStamp());
		buffer.append(" ");
		buffer.append(levelText);
		buffer.append(" ");
		buffer.append(logText);
		
		return buffer.toString();
	}
	
	/**
	 * This method logs data to System.out.
	 * 
	 * @param level - the level to indicate in the log
	 * @param logText - the text to display in the log
	 */
	private void logSystemOut(byte level, String logText) {
		System.out.println(logSystem(level,logText));
	}

	/**
	 * This method logs data to System.err.
	 * 
	 * @param level - the level to indicate in the log
	 * @param logText - the text to display in the log
	 */
	private void logSystemErr(byte level, String logText) {
		System.err.println(logSystem(level,logText));
	}

	/**
	 * This method logs data to the appropriate log facility.
	 * 
	 * @param level - the level to indicate in the log
	 * @param logText - the text to display in the log
	 */
	public void log(byte level, String logText) {
		try {
			switch (getMode()) {
				case MODE_SYSTEM:
					if (getLevel() > level) return;
					
					if (getLevel() < systemErrLevel)
						logSystemOut(level,logText);
					else
						logSystemErr(level,logText);
					
					break;
	
				case MODE_LOG4J:
					Object parms[] = {logText};
					if (level >= DEBUG && level <= FATAL)
						log4jLogMethod[level].invoke(log4jLogger,parms);
					break;
			}
			
		} catch (InvocationTargetException ite) {
			logSystemErr(level,ite.toString());
			
		} catch (IllegalAccessException iae) {
			logSystemErr(level,iae.toString());
		}
	}

	/**
	 * @param logText - the debug text to display in the log
	 */
	public void debug(String logText) {
		log(DEBUG,logText);
	}

	/**
	 * @param logText - the info text to display in the log
	 */
	public void info(String logText) {
		log(INFO,logText);
	}

	/**
	 * @param logText - the warn text to display in the log
	 */
	public void warn(String logText) {
		log(WARN,logText);
	}

	/**
	 * @param logText - the error text to display in the log
	 */
	public void error(String logText) {
		log(ERROR,logText);
	}

	/**
	 * @param logText - the fatal text to display in the log
	 */
	public void fatal(String logText) {
		log(FATAL,logText);
	}

	/**
	 * @param throwable - the instance of Throwable to debug log
	 */
	public void debug(Throwable throwable) {
		if (throwable == null) return;

		debug(throwable.toString());
	}

	/**
	 * @param throwable - the instance of Throwable to info log
	 */
	public void info(Throwable throwable) {
		if (throwable == null) return;
		
		info(throwable.toString());
	}

	/**
	 * @param throwable - the instance of Throwable to warn log
	 */
	public void warn(Throwable throwable) {
		if (throwable == null) return;
		
		warn(throwable.toString());
	}

	/**
	 * @param throwable - the instance of Throwable to error log
	 */
	public void error(Throwable throwable) {
		if (throwable == null) return;

		error(throwable.toString());
	}

	/**
	 * @param throwable - the instance of Throwable to fatal log
	 */
	public void fatal(Throwable throwable) {
		if (throwable == null) return;
		
		fatal(throwable.toString());
	}

	/**
	 * @return Returns the mode.
	 */
	protected byte getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	protected void setMode(byte mode) {
		this.mode = mode;
	}
	
	/**
	 * @return Returns the level
	 */
	public byte getLevel() {
		return level;
	}

	/**
	 * @param level - the logging level to set
	 */
	public void setLevel(byte level) {
		if (level < DEBUG || level > FATAL) return;
		
		this.level = level;

		switch (getMode()) {
			case MODE_SYSTEM:
				info("Logging level set to: " + LevelLogText[level]);
				break;
				
			case MODE_LOG4J:
				error("Setting log level for log4j not possible via Habitat4JLogger.  Please use the log4j facility directly.");
				break;
		}		
	}
	
	/**
	 * Sets the logging level.  Must be a String containing one of the following (case insensitive):
	 * 
	 * <ul>
	 * 	<li>debug</li>
	 * 	<li>info</li>
	 * 	<li>warn</li>
	 * 	<li>error</li>
	 * 	<li>fatal</li>
	 * </ul>
	 * 
	 * @param level - the logging level to set
	 */
	public void setLevel(String level) {
		if (level.equalsIgnoreCase("debug")) {
			setLevel(DEBUG);
			
		} else if (level.equalsIgnoreCase("info")) {
			setLevel(INFO);
			
		} else if (level.equalsIgnoreCase("warn")) {
			setLevel(WARN);
			
		} else if (level.equalsIgnoreCase("error")) {
			setLevel(ERROR);
			
		} else if (level.equalsIgnoreCase("fatal")) {
			setLevel(FATAL);
			
		} else {
			error("Invalid level \"" + level + "\" selected.  Please use either:  debug, info, warn, error, fatal.");
		}
	}
	
	/**
	 * @return Returns the systemErrLevel.
	 */
	public byte getSystemErrLevel() {
		return systemErrLevel;
	}
	/**
	 * @param systemErrLevel The systemErrLevel to set.
	 */
	public void setSystemErrLevel(byte systemErrLevel) {
		if (systemErrLevel < DEBUG || systemErrLevel > FATAL) return;

		this.systemErrLevel = systemErrLevel;
	}
}
