/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom;

import java.util.logging.*;
import jtom.tools.*;
import tom.platform.*;

/**
 *
 */
public class Tom {

  /** The current version of the TOM compiler. */
  public final static String VERSION = "2.1 - under development";

  /** */
  private static Logger tomRootLogger;

  /** */
  private static Handler consoleHandler = null;

  public static int exec(String[] args) {
    try{

      String loggingConfigFile = System.getProperty("java.util.logging.config.file");

      if ( loggingConfigFile == null ) { // default > no custom file is used

	// create a configuration equivalent to the one defined in the normalLog.properties file

	tomRootLogger = Logger.getLogger("jtom", "jtom.TomMessageResources");
	tomRootLogger.setLevel(Level.WARNING);
  	tomRootLogger.setUseParentHandlers(false);
	
	Handler[] handlers = tomRootLogger.getHandlers();
	for(int i = 0; i < handlers.length; i++) { // remove all pre-existing handlers that might exist from prior uses
	  tomRootLogger.removeHandler(handlers[i]);
	}

	consoleHandler = new ConsoleHandler();
	consoleHandler.setLevel(Level.ALL); // by default, print everything that the logger sends
	consoleHandler.setFormatter( new TomBasicFormatter() );
	tomRootLogger.addHandler(consoleHandler);
      
      } else { // custom configuration file for LogManager is used
	LogManager.getLogManager().readConfiguration();

	tomRootLogger = Logger.getLogger("jtom", "jtom.TomMessageResources");
	tomRootLogger.setUseParentHandlers(true);

	Handler[] handlers = tomRootLogger.getHandlers();
	for(int i = 0; i < handlers.length; i++) { // remove all pre-existing handlers that might exist from prior uses
	  tomRootLogger.removeHandler(handlers[i]);
	}

	handlers = Logger.getLogger("").getHandlers();
	for(int i = 0; i < handlers.length; i++) { // search for the global console handler

	  /*
	   * OK, the following code is ugly, I could have made it prettier but it is more robust that way,
	   * since it handles all the basic handlers as well as the ones that might extend them.
	   * I wrote that because the LogManager won't refresh the formatters, although its properties
	   * are set at the appropriate values.
	   */

	  if( handlers[i] instanceof ConsoleHandler ) {

	    consoleHandler = handlers[i];

	    handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().
						getProperty("java.util.logging.ConsoleHandler.formatter")).newInstance());
	  } else if( handlers[i] instanceof FileHandler ) {
	    handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().
					        getProperty("java.util.logging.FileHandler.formatter")).newInstance());
	  } else if( handlers[i] instanceof SocketHandler ) {
	    handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().
						getProperty("java.util.logging.SocketHandler.formatter")).newInstance());
	  } else if( handlers[i] instanceof MemoryHandler ) {
	    handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().
						getProperty("java.util.logging.MemoryHandler.formatter")).newInstance());
	  } else if( handlers[i] instanceof StreamHandler ) {
	    handlers[i].setFormatter((Formatter)Class.forName(LogManager.getLogManager().
						getProperty("java.util.logging.StreamHandler.formatter")).newInstance());
	  }

	  //System.out.println("Handler "+handlers[i]+" has formatter "+handlers[i].getFormatter());    
	}
      }

      OptionManager om = new TomOptionManager();

      return PluginPlatform.exec(args, om, "jtom");

    } catch(Exception e) {
      System.out.println("Error during the initialization of Tom : " + e.getMessage());
      e.printStackTrace();
      return 1; 
    }
  }

  public static void main(String[] args) {
    exec(args);
  }

  /**
   * This method should be used to change the Level of logging, instead of directly
   * accessing to the tomRootLogger via Logger.getLogger("jtom").
   * Indeed, this method respect the fact that the tomRootLogger's Level should
   * never ever be set higher than Level.WARNING, because it would cause the 
   * StatusHandler to malfunction.
   *
   * @param newLevel the Level to which we want to set the log output
   */
  public static void changeLogLevel(Level newLevel) {
    /* 
     * IMPORTANT : the rootLogger's Level can be lowered but shouldn't be risen.
     * indeed, if it is higher than Level.WARNING, the TomStatusHandler won't see warnings
     * that's why the noWarning option is handled by changing the ConsoleHandler's level
     * while the verbose option lowers the rootLogger's level to Level.INFO.
     */
    if( newLevel.intValue() <= Level.WARNING.intValue() ) {
      tomRootLogger.setLevel(newLevel);
    } else if( consoleHandler != null ) { // if we've found a global console handler
      consoleHandler.setLevel(newLevel);
      // that way, warnings are not printed, but still seen by the StatusHandler
    }
  }

}
